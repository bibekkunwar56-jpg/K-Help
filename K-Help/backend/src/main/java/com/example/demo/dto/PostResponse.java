package com.example.demo.dto;

import com.example.demo.entity.Post;
import java.time.Instant;
import java.util.UUID;

public class PostResponse {
    private UUID id;
    private String title;
    private String body;
    private String categoryName;
    private String categorySlug;
    private String authorNickname;
    private UUID authorId;
    private long likeCount;
    private long commentCount;
    private boolean likedByMe;
    private Instant createdAt;

    public static PostResponse from(Post post, long likeCount, long commentCount, boolean likedByMe) {
        PostResponse response = new PostResponse();
        response.id = post.getId();
        response.title = post.getTitle();
        response.body = post.getBody();
        response.categoryName = post.getCategory().getName();
        response.categorySlug = post.getCategory().getSlug();
        response.authorNickname = post.getAuthor().getNickname();
        response.authorId = post.getAuthor().getId();
        response.likeCount = likeCount;
        response.commentCount = commentCount;
        response.likedByMe = likedByMe;
        response.createdAt = post.getCreatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public boolean isLikedByMe() {
        return likedByMe;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
