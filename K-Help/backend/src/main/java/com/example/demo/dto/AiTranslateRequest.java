package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AiTranslateRequest {

    @NotBlank
    @Size(min = 1, max = 5000)
    private String text;

    @NotBlank
    private String targetLanguage; // "ko", "en", "ne", "vi", "zh", etc.

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }
}
