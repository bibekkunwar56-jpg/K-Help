package com.example.demo.repository;

import com.example.demo.entity.PostLike;
import com.example.demo.entity.PostLike.PostLikeId;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {

    long countByPostId(UUID postId);

    boolean existsByPostIdAndUserId(UUID postId, UUID userId);

    void deleteByPostIdAndUserId(UUID postId, UUID userId);

    /**
     * One grouped query for the whole feed instead of one count per post (avoids N+1).
     * Rows are [postId, count].
     */
    @Query("""
            SELECT l.post.id, COUNT(l)
            FROM PostLike l
            WHERE l.post.id IN :postIds
            GROUP BY l.post.id
            """)
    List<Object[]> countByPostIds(@Param("postIds") Collection<UUID> postIds);

    /** Ids of the posts from :postIds that :userId has liked (drives "likedByMe" on the feed). */
    @Query("""
            SELECT l.post.id
            FROM PostLike l
            WHERE l.user.id = :userId
              AND l.post.id IN :postIds
            """)
    List<UUID> findLikedPostIds(@Param("userId") UUID userId, @Param("postIds") Collection<UUID> postIds);
}
