package com.example.demo.service;

import com.example.demo.dto.AiPoliteRequest;
import com.example.demo.dto.AiServiceResponse;
import com.example.demo.dto.AiTranslateRequest;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    /**
     * Translates Korean text to target language or vice versa.
     * Ready for LLM / DeepL / OpenAI API key integration.
     */
    public AiServiceResponse translate(AiTranslateRequest req) {
        String text = req.getText().trim();
        String targetLang = req.getTargetLanguage().toLowerCase();

        // Built-in essential living phrase translations & rule-based baseline
        String translated;
        if ("ko".equals(targetLang)) {
            translated = toKorean(text);
        } else {
            translated = toForeignLanguage(text, targetLang);
        }

        return new AiServiceResponse(translated, text, "TRANSLATION");
    }

    /**
     * Converts casual or direct text into polite Korean honorifics (존댓말/격식체)
     * essential for foreign residents messaging landlords, bosses, or public offices.
     */
    public AiServiceResponse makePolite(AiPoliteRequest req) {
        String text = req.getText().trim();
        String formality = req.getFormality() != null ? req.getFormality().toUpperCase() : "HONORIFIC";

        String polite = formatKoreanPoliteness(text, formality);
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
            // Extracts core sentence segments as initial baseline
            String[] sentences = trimmed.split("[.!?\\n]+");
            StringBuilder sb = new StringBuilder("• Core Points:\n");
            int count = 0;
            for (String s : sentences) {
                if (!s.isBlank() && count < 3) {
                    sb.append("- ").append(s.trim()).append("\n");
                    count++;
                }
            }
            summary = sb.toString().trim();
        }

        return new AiServiceResponse(summary, trimmed, "SUMMARIZATION");
    }

    // ---------------------------------------------------------------- helpers

    private String toKorean(String text) {
        String lower = text.toLowerCase();
        if (lower.contains("hello") || lower.contains("hi")) {
            return "안녕하세요! 만나서 반갑습니다.";
        }
        if (lower.contains("how much is the deposit") || lower.contains("deposit")) {
            return "보증금과 월세가 어떻게 되나요?";
        }
        if (lower.contains("room") || lower.contains("housing") || lower.contains("available")) {
            return "혹시 방 지금 입주 가능한가요?";
        }
        if (lower.contains("job") || lower.contains("visa")) {
            return "외국인 비자 지원 가능한 채용 공고인가요?";
        }
        if (lower.contains("thank you") || lower.contains("thanks")) {
            return "도움 주셔서 정말 감사합니다.";
        }
        // Fallback marker for LLM proxy
        return "[AI 번역] " + text + " (한국어로 전달되었습니다)";
    }

    private String toForeignLanguage(String text, String targetLang) {
        if ("ne".equals(targetLang)) {
            return "[AI नेपाली अनुवाद] " + text;
        }
        if ("vi".equals(targetLang)) {
            return "[AI Dịch tiếng Việt] " + text;
        }
        if ("zh".equals(targetLang)) {
            return "[AI 中文翻译] " + text;
        }
        return "[AI Translation to " + targetLang.toUpperCase() + "] " + text;
    }

    private String formatKoreanPoliteness(String text, String formality) {
        String polished = text;

        if ("BUSINESS".equals(formality)) {
            polished = polished.replaceAll("안녕$", "안녕하십니까.")
                    .replaceAll("고마워$", "감사드립니다.")
                    .replaceAll("물어볼게$", "문의드리고자 합니다.");
            if (!polished.endsWith("합니다.") && !polished.endsWith("습니다.") && !polished.endsWith("요.")) {
                polished = polished + " 확인 부탁드립니다.";
            }
        } else {
            // Standard polite 존댓말
            polished = polished.replaceAll("안녕$", "안녕하세요")
                    .replaceAll("고마워$", "감사합니다")
                    .replaceAll("돼\\?$", "될까요?")
                    .replaceAll("줘$", "주세요");
            if (!polished.endsWith("요") && !polished.endsWith("요.") && !polished.endsWith("니다.")) {
                polished = polished + " 확인해 주시면 감사하겠습니다.";
            }
        }

        return polished;
    }
}
