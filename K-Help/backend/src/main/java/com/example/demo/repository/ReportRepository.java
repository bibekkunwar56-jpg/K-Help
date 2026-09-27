package com.example.demo.repository;

import com.example.demo.entity.Report;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, UUID> {
}
