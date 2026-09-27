package com.example.demo.service;

import com.example.demo.dto.CreateReportRequest;
import com.example.demo.dto.ReportResponse;
import com.example.demo.entity.Report;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.UserRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public ReportService(
            ReportRepository reportRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ReportResponse create(UUID reporterId, CreateReportRequest request) {
        String targetType = request.getTargetType().trim().toUpperCase();
        UUID targetId = request.getTargetId();

        if (targetId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "targetId is required");
        }

        boolean targetExists = "POST".equals(targetType)
                ? postRepository.existsById(targetId)
                : commentRepository.existsById(targetId);

        if (!targetExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reported " + targetType.toLowerCase() + " not found");
        }

        User reporter = userRepository
                .findById(reporterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Report report = new Report();
        report.setReporter(reporter);
        report.setTargetType(targetType);
        report.setTargetId(targetId);
        report.setReason(request.getReason().trim());
        report.setStatus("OPEN");

        Report saved = reportRepository.save(report);
        return new ReportResponse(
                saved.getId(), saved.getTargetType(), saved.getTargetId(), saved.getReason(), saved.getStatus());
    }
}
