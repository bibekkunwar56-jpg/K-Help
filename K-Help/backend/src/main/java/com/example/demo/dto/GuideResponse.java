package com.example.demo.dto;

import com.example.demo.entity.Guide;
import java.time.Instant;
import java.util.UUID;

public class GuideResponse {

    private UUID id;
    private String slug;
    private String title;
    private String summary;
    private String body;
    private String topic;
    private String sourceUrl;
    private boolean published;
    private Instant createdAt;
    private Instant updatedAt;

    public static GuideResponse from(Guide guide) {
        GuideResponse response = new GuideResponse();
        response.id = guide.getId();
        response.slug = guide.getSlug();
        response.title = guide.getTitle();
        response.summary = guide.getSummary();
        response.body = guide.getBody();
        response.topic = guide.getTopic();
        response.sourceUrl = guide.getSourceUrl();
        response.published = guide.isPublished();
        response.createdAt = guide.getCreatedAt();
        response.updatedAt = guide.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getBody() {
        return body;
    }

    public String getTopic() {
        return topic;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public boolean isPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
