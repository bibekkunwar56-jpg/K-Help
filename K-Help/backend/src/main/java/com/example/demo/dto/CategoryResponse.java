package com.example.demo.dto;

import com.example.demo.entity.Category;
import java.util.UUID;

public class CategoryResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;

    public static CategoryResponse from(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.id = category.getId();
        response.name = category.getName();
        response.slug = category.getSlug();
        response.description = category.getDescription();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }
}
