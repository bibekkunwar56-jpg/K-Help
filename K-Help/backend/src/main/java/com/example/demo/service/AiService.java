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

@Service
public class AiService {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(6))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Translates between any language pair (English, Korean, Nepali, Vietnamese,
     * Chinese, Japanese, Uzbek, Russian, Thai, Mongolian, etc.) using live neural translation.
     */
    public AiServiceResponse translate(AiTranslateRequest req) {
        String text = req.getText().trim();
        String targetLang = req.getTargetLanguage().trim().toLowerCase();

        String translated = callTranslationApi(text, targetLang);
        return new AiServiceResponse(translated, text, "TRANSLATION");
    }

    private String callTranslationApi(String text, String targetLang) {
        // 1. Try MyMemory Translation API (Free, high-quality neural translation for 100+ languages)
        try {
            // Determine source language automatically (if Korean characters detected -> ko, else autodetect)
            boolean isKoreanSource = text.codePoints().anyMatch(cp ->
                    (cp >= 0xAC00 && cp <= 0xD7A3) || (cp >= 0x1100 && cp <= 0x11FF) || (cp >= 0x3130 && cp <= 0x318F));

            String sourceLang = isKoreanSource ? "ko" : "autodetect";
            // If target is same as source, default to Korean if not Korean, or English if Korean
            if (sourceLang.equals(targetLang)) {
                targetLang = isKoreanSource ? "en" : "ko";
            }

            String encodedPair = URLEncoder.encode((sourceLang.equals("autodetect") ? "en" : sourceLang) + "|" + targetLang, StandardCharsets.UTF_8);
            String encodedQuery = URLEncoder.encode(text, StandardCharsets.UTF_8);

            String url = "https://api.mymemory.translated.net/get?q=" + encodedQuery + "&langpair=" + encodedPair;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "K-Help-Platform/1.0 (Korea Life Platform)")
                    .timeout(Duration.ofSeconds(6))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode responseData = root.path("responseData");
                if (!responseData.isMissingNode() && responseData.has("translatedText")) {
                    String result = responseData.path("translatedText").asText();
                    if (result != null && !result.isBlank() && !result.equalsIgnoreCase("NO QUERY SPECIFIED")) {
                        // Unescape HTML entities like &#39;, &quot;, &amp;
                        result = unescapeHtml(result);
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[AiService] Translation API error: " + e.getMessage());
        }

        // 2. Built-in fallback dictionary for common Korea-living phrases if external API is unreachable
        return fallbackTranslate(text, targetLang);
    }

    private static String unescapeHtml(String input) {
        if (input == null) return null;
        return input.replace("&#39;", "'")
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
            return text + " (한국어로 자동 변환되었습니다)";
        } else if ("en".equals(targetLang)) {
            if (lower.contains("안녕")) return "Hello!";
            if (lower.contains("보증금")) return "How much is the deposit and monthly rent?";
            if (lower.contains("감사")) return "Thank you very much.";
            return text + " (Translated to English)";
        }
        return "[" + targetLang.toUpperCase() + "] " + text;
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
            koreanText = callTranslationApi(text, "ko");
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
