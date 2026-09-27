package com.example.demo.controller;

import com.example.demo.dto.CommentResponse;
import com.example.demo.dto.CreateCommentRequest;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /** Public. */
    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> list(@PathVariable UUID postId) {
        return commentService.list(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(
            @PathVariable UUID postId,
            @Valid @RequestBody CreateCommentRequest request,
            Authentication authentication) {
        return commentService.create(postId, CurrentUser.required(authentication), request);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID commentId, Authentication authentication) {
        commentService.delete(commentId, CurrentUser.required(authentication));
    }
}
