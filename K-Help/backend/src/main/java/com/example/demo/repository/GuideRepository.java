package com.example.demo.repository;

import com.example.demo.entity.Guide;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuideRepository extends JpaRepository<Guide, UUID> {

    Optional<Guide> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Guide> findByPublishedTrueOrderByTitleAsc();

    List<Guide> findByPublishedTrueAndTopicOrderByTitleAsc(String topic);

    @Query("""
            SELECT g FROM Guide g
            WHERE g.published = true
              AND (LOWER(g.title) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(g.summary) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(g.body) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY g.title ASC
            """)
    List<Guide> searchPublished(@Param("q") String q);

    @Query("""
            SELECT g FROM Guide g
            WHERE g.published = true
              AND g.topic = :topic
              AND (LOWER(g.title) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(g.summary) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(g.body) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY g.title ASC
            """)
    List<Guide> searchPublishedByTopic(@Param("topic") String topic, @Param("q") String q);

    @Query("""
            SELECT DISTINCT g.topic FROM Guide g
            WHERE g.published = true
            ORDER BY g.topic ASC
            """)
    List<String> findPublishedTopics();
}
