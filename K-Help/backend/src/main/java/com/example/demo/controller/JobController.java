package com.example.demo.controller;

import com.example.demo.dto.CreateJobRequest;
import com.example.demo.dto.JobResponse;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.JobService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<JobResponse> list(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String q) {
        return jobService.list(location, q);
    }

    @GetMapping("/{id}")
    public JobResponse get(@PathVariable UUID id) {
        return jobService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse create(@Valid @RequestBody CreateJobRequest req, Authentication authentication) {
        return jobService.create(CurrentUser.required(authentication), req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, Authentication authentication) {
        jobService.delete(id, CurrentUser.required(authentication));
    }
}
