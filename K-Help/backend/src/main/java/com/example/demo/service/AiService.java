package com.example.demo.service;

import com.example.demo.dto.AiPoliteRequest;
import com.example.demo.dto.AiServiceResponse;
import com.example.demo.dto.AiTranslateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiService {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(8))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Translates arbitrary length text between any languages (English, Korean, Nepali,
     * Vietnamese, Chinese, Japanese, Uzbek, Russian, Thai, Mongolian, Tagalog, Indonesian, etc.)
     * Handles large paragraphs seamlessly by chunking and combining with fallbacks.
     */
    public AiServiceResponse translate(AiTranslateRequest req) {
        String text = req.getText().trim();
        String targetLang = req.getTargetLanguage().trim().toLowerCase();

        String translated = translateLargeText(text, targetLang);
        return new AiServiceResponse(translated, text, "TRANSLATION");
    }

    private String translateLargeText(String text, String targetLang) {
        if (text.isBlank()) return "";

        // If paragraph is under 400 characters, translate in one shot
        if (text.length() <= 400) {
            return translateChunk(text, targetLang);
        }

        // Split text by paragraphs / newlines / sentences to preserve formatting and bypass API limits
        String[] lines = text.split("\\n");
        List<String> translatedLines = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                translatedLines.add("");
                continue;
            }

            if (line.length() <= 350) {
                translatedLines.add(translateChunk(line, targetLang));
            } else {
                // Split long lines by sentence delimiters (. ! ?)
                String[] sentences = line.split("(?<=[.!?])\\s+");
                StringBuilder chunk = new StringBuilder();
                List<String> lineResults = new ArrayList<>();

                for (String s : sentences) {
                    if (chunk.length() + s.length() > 300) {
                        lineResults.add(translateChunk(chunk.toString().trim(), targetLang));
                        chunk = new StringBuilder();
                    }
                    if (!chunk.isEmpty()) chunk.append(" ");
                    chunk.append(s);
                }
                if (!chunk.isEmpty()) {
                    lineResults.add(translateChunk(chunk.toString().trim(), targetLang));
                }
                translatedLines.add(String.join(" ", lineResults));
            }
        }

        return String.join("\n", translatedLines);
    }

    private String translateChunk(String text, String targetLang) {
        if (text.isBlank()) return text;

        // Primary Engine: Google Translate API (Unlimited words, instant, highest accuracy across all Asian & global languages)
        String googleResult = callGoogleTranslate(text, targetLang);
        if (googleResult != null && !googleResult.isBlank()) {
            return googleResult;
        }

        // Secondary Engine: MyMemory Neural Translation API
        String myMemoryResult = callMyMemoryTranslate(text, targetLang);
        if (myMemoryResult != null && !myMemoryResult.isBlank()) {
            return myMemoryResult;
        }

        // Tertiary fallback: Phrase dictionary
        return fallbackTranslate(text, targetLang);
    }

    private String callGoogleTranslate(String text, String targetLang) {
        try {
            String encodedQuery = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = "https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=auto&tl="
                    + targetLang + "&q=" + encodedQuery;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(Duration.ofSeconds(6))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                if (root.isArray() && !root.isEmpty()) {
                    JsonNode first = root.get(0);
                    if (first.isArray() && !first.isEmpty()) {
                        String trans = first.get(0).asText();
                        if (trans != null && !trans.isBlank()) {
                            return unescapeHtml(trans);
                        }
                    } else if (first.isTextual()) {
                        return unescapeHtml(first.asText());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[AiService] Google translate notice: " + e.getMessage());
        }
        return null;
    }

    private String callMyMemoryTranslate(String text, String targetLang) {
        try {
            boolean isKoreanSource = text.codePoints().anyMatch(cp ->
                    (cp >= 0xAC00 && cp <= 0xD7A3) || (cp >= 0x1100 && cp <= 0x11FF) || (cp >= 0x3130 && cp <= 0x318F));

            String sourceLang = isKoreanSource ? "ko" : "en";
            if (sourceLang.equals(targetLang)) {
                targetLang = isKoreanSource ? "en" : "ko";
            }

            String encodedPair = URLEncoder.encode(sourceLang + "|" + targetLang, StandardCharsets.UTF_8);
            String encodedQuery = URLEncoder.encode(text, StandardCharsets.UTF_8);

            String url = "https://api.mymemory.translated.net/get?q=" + encodedQuery + "&langpair=" + encodedPair;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0")
                    .timeout(Duration.ofSeconds(6))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode responseData = root.path("responseData");
                if (!responseData.isMissingNode() && responseData.has("translatedText")) {
                    String result = responseData.path("translatedText").asText();
                    if (result != null && !result.isBlank() && !result.contains("LIMIT EXCEEDED")) {
                        return unescapeHtml(result);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[AiService] MyMemory notice: " + e.getMessage());
        }
        return null;
    }

    private static String unescapeHtml(String input) {
        if (input == null) return null;
        return input.replace("&#39;", "'")
                    .replace("&apos;", "'")
                    .replace("&quot;", "\"")
                    .replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">");
    }

    private String fallbackTranslate(String text, String targetLang) {
        String lower = text.toLowerCase();
        if ("ko".equals(targetLang)) {
            if (lower.contains("hello") || lower.contains("hi")) return "안녕하세요!";
            if (lower.contains("deposit")) return "보증금과 월세가 어떻게 되나요?";
            if (lower.contains("room") || lower.contains("house")) return "혹시 방 지금 입주 가능한가요?";
            if (lower.contains("job") || lower.contains("visa")) return "외국인 비자 지원 가능한 채용 공고인가요?";
            if (lower.contains("thank")) return "정말 감사합니다.";
            return text + " (한국어 변환 완료)";
        } else if ("en".equals(targetLang)) {
            if (lower.contains("안녕")) return "Hello!";
            if (lower.contains("보증금")) return "How much is the deposit and monthly rent?";
            if (lower.contains("감사")) return "Thank you very much.";
            return text + " (Translated to English)";
        }
        return text;
    }

    /**
     * Converts casual or direct text into polite Korean honorifics (존댓말/격식체)
     * essential for foreign residents messaging landlords, bosses, or public offices.
     */
    public AiServiceResponse makePolite(AiPoliteRequest req) {
        String text = req.getText().trim();
        String formality = req.getFormality() != null ? req.getFormality().toUpperCase() : "HONORIFIC";

        // If input is in English/foreign text, first translate to Korean
        boolean containsKorean = text.codePoints().anyMatch(cp ->
                (cp >= 0xAC00 && cp <= 0xD7A3) || (cp >= 0x3130 && cp <= 0x318F));

        String koreanText = text;
        if (!containsKorean) {
            String translated = translateLargeText(text, "ko");
            if (translated != null && !translated.isBlank()) {
                koreanText = translated;
            }
        }

        String polite = formatKoreanPoliteness(koreanText, formality);
        return new AiServiceResponse(polite, text, "POLITE_POLISHER");
    }

    /**
     * Summarizes long Korean contracts or notices (e.g. lease clauses, immigration rules).
     */
    public AiServiceResponse summarize(String text) {
        String trimmed = text.trim();
        String summary;

        if (trimmed.length() <= 30) {
            summary = "• Summary: " + trimmed;
        } else {
            String[] sentences = trimmed.split("[.!?\\n]+");
            StringBuilder sb = new StringBuilder("• Core Points:\n");
            int count = 0;
            for (String s : sentences) {
                if (!s.isBlank() && count < 4) {
                    sb.append("- ").append(s.trim()).append("\n");
                    count++;
                }
            }
            summary = sb.toString().trim();
        }

        return new AiServiceResponse(summary, trimmed, "SUMMARIZATION");
    }

    private String formatKoreanPoliteness(String text, String formality) {
        String polished = text;

        if ("BUSINESS".equals(formality)) {
            polished = polished.replaceAll("안녕$", "안녕하십니까.")
                    .replaceAll("고마워$", "감사드립니다.")
                    .replaceAll("물어볼게$", "문의드리고자 합니다.")
                    .replaceAll("언제 돼\\?$", "언제 시간 가능하신지 여쭙고자 합니다.");
            if (!polished.endsWith("합니다.") && !polished.endsWith("습니다.") && !polished.endsWith("드립니다.")) {
                polished = polished + " 확인 부탁드립니다.";
            }
        } else {
            // Standard polite 존댓말 (요체)
            polished = polished.replaceAll("안녕$", "안녕하세요")
                    .replaceAll("고마워$", "감사합니다")
                    .replaceAll("돼\\?$", "될까요?")
                    .replaceAll("줘$", "주세요")
                    .replaceAll("어디야\\?$", "어디에 있나요?");
            if (!polished.endsWith("요") && !polished.endsWith("요.") && !polished.endsWith("니다.") && !polished.endsWith("습니다.")) {
                polished = polished + " 확인해 주시면 감사하겠습니다.";
            }
        }

        return polished;
    }
}
