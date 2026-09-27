package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Body for POST /api/guides and PUT /api/guides/{slug}. */
public class SaveGuideRequest {

    @NotBlank
    @Size(min = 3, max = 160)
    @Pattern(
            regexp = "^[a-z0-9]+(-[a-z0-9]+)*$",
            message = "slug must be lowercase words separated by single hyphens")
    private String slug;

    @NotBlank
    @Size(min = 5, max = 200)
    private String title;

    @NotBlank
    @Size(min = 10, max = 2000)
    private String summary;

    @NotBlank
    @Size(min = 20, max = 20000)
    private String body;

    @NotBlank
    @Size(min = 2, max = 60)
    private String topic;

    @Size(max = 500)
    private String sourceUrl;

    private boolean published = true;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
