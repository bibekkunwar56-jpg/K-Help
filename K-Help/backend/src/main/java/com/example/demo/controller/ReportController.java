package com.example.demo.controller;

import com.example.demo.dto.CreateReportRequest;
import com.example.demo.dto.ReportResponse;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportResponse create(@Valid @RequestBody CreateReportRequest request, Authentication authentication) {
        return reportService.create(CurrentUser.required(authentication), request);
    }
}
