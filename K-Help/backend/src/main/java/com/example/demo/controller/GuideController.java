package com.example.demo.controller;

import com.example.demo.dto.GuideResponse;
import com.example.demo.dto.SaveGuideRequest;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.GuideService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guides")
public class GuideController {

    private final GuideService guideService;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    /** Public. Filters: ?topic=<topic>, ?q=<keyword>. */
    @GetMapping
    public List<GuideResponse> list(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String q) {
        return guideService.list(topic, q);
    }

    /** Public. Topics that currently have at least one published guide. */
    @GetMapping("/topics")
    public List<String> topics() {
        return guideService.topics();
    }

    /** Admin only — every guide including unpublished drafts. */
    @GetMapping("/all")
    public List<GuideResponse> listAll(Authentication authentication) {
        return guideService.listAll(CurrentUser.required(authentication));
    }

    /** Public. */
    @GetMapping("/{slug}")
    public GuideResponse getBySlug(@PathVariable String slug) {
        return guideService.getBySlug(slug);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GuideResponse create(@Valid @RequestBody SaveGuideRequest request, Authentication authentication) {
        return guideService.create(CurrentUser.required(authentication), request);
    }

    @PutMapping("/{slug}")
    public GuideResponse update(
            @PathVariable String slug,
            @Valid @RequestBody SaveGuideRequest request,
            Authentication authentication) {
        return guideService.update(slug, CurrentUser.required(authentication), request);
    }

    @DeleteMapping("/{slug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String slug, Authentication authentication) {
        guideService.delete(slug, CurrentUser.required(authentication));
    }
}
