package com.example.demo.repository;

import com.example.demo.entity.Comment;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.author
            LEFT JOIN FETCH c.parent
            WHERE c.post.id = :postId
            ORDER BY c.createdAt ASC
            """)
    List<Comment> findByPostIdWithAuthor(@Param("postId") UUID postId);

    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.author
            WHERE c.id = :id
            """)
    Optional<Comment> findByIdWithAuthor(@Param("id") UUID id);

    long countByPostId(UUID postId);

    /**
     * One grouped query for the whole feed instead of one count per post (avoids N+1).
     * Rows are [postId, count].
     */
    @Query("""
            SELECT c.post.id, COUNT(c)
            FROM Comment c
            WHERE c.post.id IN :postIds
            GROUP BY c.post.id
            """)
    List<Object[]> countByPostIds(@Param("postIds") Collection<UUID> postIds);
}
