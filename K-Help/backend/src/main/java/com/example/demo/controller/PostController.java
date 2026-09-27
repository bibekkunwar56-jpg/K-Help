package com.example.demo.controller;

import com.example.demo.dto.CreatePostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.dto.UpdatePostRequest;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /** Public. Filters: ?category=slug, ?q=keyword, ?authorId=uuid. */
    @GetMapping
    public List<PostResponse> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) UUID authorId,
            Authentication authentication) {
        UUID currentUserId = CurrentUser.id(authentication);
        if (authorId != null) {
            return postService.listByAuthor(authorId, currentUserId);
        }
        return postService.list(category, q, currentUserId);
    }

    /** Public. */
    @GetMapping("/{id}")
    public PostResponse get(@PathVariable UUID id, Authentication authentication) {
        return postService.get(id, CurrentUser.id(authentication));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody CreatePostRequest request, Authentication authentication) {
        return postService.create(CurrentUser.required(authentication), request);
    }

    @PutMapping("/{id}")
    public PostResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequest request,
            Authentication authentication) {
        return postService.update(id, CurrentUser.required(authentication), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, Authentication authentication) {
        postService.delete(id, CurrentUser.required(authentication));
    }

    @PostMapping("/{id}/like")
    public PostResponse like(@PathVariable UUID id, Authentication authentication) {
        return postService.like(id, CurrentUser.required(authentication));
    }

    @DeleteMapping("/{id}/like")
    public PostResponse unlike(@PathVariable UUID id, Authentication authentication) {
        return postService.unlike(id, CurrentUser.required(authentication));
    }
}
