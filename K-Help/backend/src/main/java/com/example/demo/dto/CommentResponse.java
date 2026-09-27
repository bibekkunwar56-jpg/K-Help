package com.example.demo.dto;

import com.example.demo.entity.Comment;
import java.time.Instant;
import java.util.UUID;

public class CommentResponse {
    private UUID id;
    private String body;
    private String authorNickname;
    private UUID authorId;
    private UUID parentId;
    private Instant createdAt;

    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.id = comment.getId();
        response.body = comment.getBody();
        response.authorNickname = comment.getAuthor().getNickname();
        response.authorId = comment.getAuthor().getId();
        response.parentId = comment.getParent() != null ? comment.getParent().getId() : null;
        response.createdAt = comment.getCreatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getParentId() {
        return parentId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
