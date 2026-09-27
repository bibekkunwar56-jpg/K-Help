package com.example.demo.repository;

import com.example.demo.entity.Post;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("""
            SELECT p FROM Post p
            JOIN FETCH p.author
            JOIN FETCH p.category
            ORDER BY p.createdAt DESC
            """)
    List<Post> findAllWithDetails();

    @Query("""
            SELECT p FROM Post p
            JOIN FETCH p.author
            JOIN FETCH p.category
            WHERE p.category.slug = :slug
            ORDER BY p.createdAt DESC
            """)
    List<Post> findByCategorySlug(@Param("slug") String slug);

    @Query("""
            SELECT p FROM Post p
            JOIN FETCH p.author
            JOIN FETCH p.category
            WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(p.body) LIKE LOWER(CONCAT('%', :q, '%'))
            ORDER BY p.createdAt DESC
            """)
    List<Post> searchByText(@Param("q") String q);

    @Query("""
            SELECT p FROM Post p
            JOIN FETCH p.author
            JOIN FETCH p.category
            WHERE p.category.slug = :slug
              AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.body) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY p.createdAt DESC
            """)
    List<Post> searchByCategoryAndText(@Param("slug") String slug, @Param("q") String q);

    @Query("""
            SELECT p FROM Post p
            JOIN FETCH p.author
            JOIN FETCH p.category
            WHERE p.id = :id
            """)
    Optional<Post> findByIdWithDetails(@Param("id") UUID id);

    @Query("""
            SELECT p FROM Post p
            JOIN FETCH p.author
            JOIN FETCH p.category
            WHERE p.author.id = :authorId
            ORDER BY p.createdAt DESC
            """)
    List<Post> findByAuthorIdWithDetails(@Param("authorId") UUID authorId);
}
