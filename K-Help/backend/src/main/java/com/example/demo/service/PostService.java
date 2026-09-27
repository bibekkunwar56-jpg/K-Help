package com.example.demo.service;

import com.example.demo.dto.CreatePostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.dto.UpdatePostRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Post;
import com.example.demo.entity.PostLike;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public PostService(
            PostRepository postRepository,
            CommentRepository commentRepository,
            PostLikeRepository postLikeRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.postLikeRepository = postLikeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    /** Feed. Both filters are optional; currentUserId may be null for signed-out readers. */
    @Transactional(readOnly = true)
    public List<PostResponse> list(String categorySlug, String q, UUID currentUserId) {
        String slug = blankToNull(categorySlug);
        String keyword = blankToNull(q);

        List<Post> posts;
        if (slug != null && keyword != null) {
            posts = postRepository.searchByCategoryAndText(slug.trim().toLowerCase(), keyword);
        } else if (slug != null) {
            posts = postRepository.findByCategorySlug(slug.trim().toLowerCase());
        } else if (keyword != null) {
            posts = postRepository.searchByText(keyword);
        } else {
            posts = postRepository.findAllWithDetails();
        }

        return toResponses(posts, currentUserId);
    }

    @Transactional(readOnly = true)
    public PostResponse get(UUID postId, UUID currentUserId) {
        return toResponses(List.of(loadPost(postId)), currentUserId).get(0);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> listByAuthor(UUID authorId, UUID currentUserId) {
        return toResponses(postRepository.findByAuthorIdWithDetails(authorId), currentUserId);
    }

    @Transactional
    public PostResponse create(UUID authorId, CreatePostRequest request) {
        Category category = categoryRepository
                .findBySlug(request.getCategorySlug().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Unknown category: " + request.getCategorySlug()));

        Post post = new Post();
        post.setAuthor(loadUser(authorId));
        post.setCategory(category);
        post.setTitle(request.getTitle().trim());
        post.setBody(request.getBody().trim());

        Post saved = postRepository.save(post);
        return PostResponse.from(saved, 0L, 0L, false);
    }

    @Transactional
    public PostResponse update(UUID postId, UUID userId, UpdatePostRequest request) {
        Post post = loadPost(postId);
        requireOwnerOrAdmin(post.getAuthor(), userId, "post");

        post.setTitle(request.getTitle().trim());
        post.setBody(request.getBody().trim());

        return toResponses(List.of(postRepository.save(post)), userId).get(0);
    }

    @Transactional
    public void delete(UUID postId, UUID userId) {
        Post post = loadPost(postId);
        requireOwnerOrAdmin(post.getAuthor(), userId, "post");
        postRepository.delete(post);
    }

    /** Idempotent: liking twice leaves one like. */
    @Transactional
    public PostResponse like(UUID postId, UUID userId) {
        Post post = loadPost(postId);
        if (!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            PostLike like = new PostLike();
            like.setPost(post);
            like.setUser(loadUser(userId));
            postLikeRepository.save(like);
        }
        return toResponses(List.of(post), userId).get(0);
    }

    /** Idempotent: unliking a post that is not liked is a no-op. */
    @Transactional
    public PostResponse unlike(UUID postId, UUID userId) {
        Post post = loadPost(postId);
        postLikeRepository.deleteByPostIdAndUserId(postId, userId);
        return toResponses(List.of(post), userId).get(0);
    }

    // ---------------------------------------------------------------- helpers

    private List<PostResponse> toResponses(List<Post> posts, UUID currentUserId) {
        if (posts.isEmpty()) {
            return List.of();
        }

        List<UUID> postIds = posts.stream().map(Post::getId).toList();
        Map<UUID, Long> likeCounts = toCountMap(postLikeRepository.countByPostIds(postIds));
        Map<UUID, Long> commentCounts = toCountMap(commentRepository.countByPostIds(postIds));

        Set<UUID> likedByMe = new HashSet<>();
        if (currentUserId != null) {
            likedByMe.addAll(postLikeRepository.findLikedPostIds(currentUserId, postIds));
        }

        return posts.stream()
                .map(post -> PostResponse.from(
                        post,
                        likeCounts.getOrDefault(post.getId(), 0L),
                        commentCounts.getOrDefault(post.getId(), 0L),
                        likedByMe.contains(post.getId())))
                .toList();
    }

    private static Map<UUID, Long> toCountMap(List<Object[]> rows) {
        Map<UUID, Long> counts = new HashMap<>();
        for (Object[] row : rows) {
            counts.put((UUID) row[0], ((Number) row[1]).longValue());
        }
        return counts;
    }

    private Post loadPost(UUID postId) {
        return postRepository
                .findByIdWithDetails(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    private User loadUser(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void requireOwnerOrAdmin(User owner, UUID userId, String what) {
        if (owner.getId().equals(userId)) {
            return;
        }
        User actor = loadUser(userId);
        if (!"ADMIN".equals(actor.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only change your own " + what);
        }
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
