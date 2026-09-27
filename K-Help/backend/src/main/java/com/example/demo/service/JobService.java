package com.example.demo.service;

import com.example.demo.dto.CreateJobRequest;
import com.example.demo.dto.JobResponse;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<JobResponse> list(String location, String q) {
        String loc = blankToNull(location);
        String keyword = blankToNull(q);

        List<Job> jobs;
        if (loc != null && keyword != null) {
            jobs = jobRepository.searchActiveWithLocation(loc, keyword);
        } else if (loc != null) {
            jobs = jobRepository.findByLocation(loc);
        } else if (keyword != null) {
            jobs = jobRepository.searchActive(keyword);
        } else {
            jobs = jobRepository.findAllActiveWithEmployer();
        }

        return jobs.stream().map(JobResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public JobResponse get(UUID id) {
        Job job = jobRepository
                .findById(id)
                .filter(Job::isActive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job posting not found"));
        return JobResponse.from(job);
    }

    @Transactional
    public JobResponse create(UUID employerId, CreateJobRequest req) {
        User employer = userRepository
                .findById(employerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Job job = new Job();
        job.setEmployer(employer);
        job.setTitle(req.getTitle().trim());
        job.setCompanyName(req.getCompanyName().trim());
        job.setLocation(req.getLocation().trim());
        job.setVisaRequirements(blankToNull(req.getVisaRequirements()));
        job.setEmploymentType(req.getEmploymentType() != null ? req.getEmploymentType().trim() : "FULL_TIME");
        job.setSalaryRange(blankToNull(req.getSalaryRange()));
        job.setDescription(req.getDescription().trim());
        job.setContactEmail(blankToNull(req.getContactEmail()));
        job.setActive(true);

        return JobResponse.from(jobRepository.save(job));
    }

    @Transactional
    public void delete(UUID id, UUID userId) {
        Job job = jobRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job posting not found"));

        if (!job.getEmployer().getId().equals(userId)) {
            User actor = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            if (!"ADMIN".equals(actor.getRole())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the employer or admin can delete this job");
            }
        }

        job.setActive(false);
        jobRepository.save(job);
    }

    private static String blankToNull(String val) {
        return (val == null || val.isBlank()) ? null : val.trim();
    }
}
