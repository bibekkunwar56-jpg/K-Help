package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AiPoliteRequest {

    @NotBlank
    @Size(min = 1, max = 5000)
    private String text;

    private String formality = "HONORIFIC"; // "FORMAL", "HONORIFIC", "BUSINESS"

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormality() {
        return formality;
    }

    public void setFormality(String formality) {
        this.formality = formality;
    }
}
