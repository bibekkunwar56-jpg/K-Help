package com.example.demo.service;

import com.example.demo.dto.CommentResponse;
import com.example.demo.dto.CreateCommentRequest;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> list(UUID postId) {
        requirePost(postId);
        return commentRepository.findByPostIdWithAuthor(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse create(UUID postId, UUID authorId, CreateCommentRequest request) {
        Post post = requirePost(postId);
        User author = userRepository
                .findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setBody(request.getBody().trim());

        if (request.getParentId() != null) {
            Comment parent = commentRepository
                    .findById(request.getParentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment not found"));
            if (!parent.getPost().getId().equals(postId)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Parent comment belongs to a different post");
            }
            comment.setParent(parent);
        }

        return CommentResponse.from(commentRepository.save(comment));
    }

    @Transactional
    public void delete(UUID commentId, UUID userId) {
        Comment comment = commentRepository
                .findByIdWithAuthor(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            User actor = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            if (!"ADMIN".equals(actor.getRole())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own comment");
            }
        }

        commentRepository.delete(comment);
    }

    private Post requirePost(UUID postId) {
        return postRepository
                .findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }
}
