package com.example.demo.controller;

import com.example.demo.dto.AiPoliteRequest;
import com.example.demo.dto.AiServiceResponse;
import com.example.demo.dto.AiTranslateRequest;
import com.example.demo.service.AiService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/translate")
    public AiServiceResponse translate(@Valid @RequestBody AiTranslateRequest req) {
        return aiService.translate(req);
    }

    @PostMapping("/polite")
    public AiServiceResponse polite(@Valid @RequestBody AiPoliteRequest req) {
        return aiService.makePolite(req);
    }

    @PostMapping("/summarize")
    public AiServiceResponse summarize(@RequestBody Map<String, String> body) {
        String text = body.getOrDefault("text", "");
        return aiService.summarize(text);
    }
}
