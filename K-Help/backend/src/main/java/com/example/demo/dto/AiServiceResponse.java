package com.example.demo.dto;

public class AiServiceResponse {

    private String result;
    private String originalText;
    private String mode;

    public AiServiceResponse(String result, String originalText, String mode) {
        this.result = result;
        this.originalText = originalText;
        this.mode = mode;
    }

    public String getResult() {
        return result;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getMode() {
        return mode;
    }
}
