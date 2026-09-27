package com.example.demo.repository;

import com.example.demo.entity.Job;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, UUID> {

    @Query("""
            SELECT j FROM Job j
            JOIN FETCH j.employer
            WHERE j.active = true
            ORDER BY j.createdAt DESC
            """)
    List<Job> findAllActiveWithEmployer();

    @Query("""
            SELECT j FROM Job j
            JOIN FETCH j.employer
            WHERE j.active = true
              AND LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))
            ORDER BY j.createdAt DESC
            """)
    List<Job> findByLocation(@Param("location") String location);

    @Query("""
            SELECT j FROM Job j
            JOIN FETCH j.employer
            WHERE j.active = true
              AND (LOWER(j.title) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(j.description) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY j.createdAt DESC
            """)
    List<Job> searchActive(@Param("q") String q);

    @Query("""
            SELECT j FROM Job j
            JOIN FETCH j.employer
            WHERE j.active = true
              AND LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))
              AND (LOWER(j.title) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(j.description) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY j.createdAt DESC
            """)
    List<Job> searchActiveWithLocation(@Param("location") String location, @Param("q") String q);
}
