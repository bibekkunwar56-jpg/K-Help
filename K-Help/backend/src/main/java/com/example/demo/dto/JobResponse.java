package com.example.demo.dto;

import com.example.demo.entity.Job;
import java.time.Instant;
import java.util.UUID;

public class JobResponse {

    private UUID id;
    private String title;
    private String companyName;
    private String location;
    private String visaRequirements;
    private String employmentType;
    private String salaryRange;
    private String description;
    private String contactEmail;
    private String employerNickname;
    private UUID employerId;
    private Instant createdAt;

    public static JobResponse from(Job job) {
        JobResponse r = new JobResponse();
        r.id = job.getId();
        r.title = job.getTitle();
        r.companyName = job.getCompanyName();
        r.location = job.getLocation();
        r.visaRequirements = job.getVisaRequirements();
        r.employmentType = job.getEmploymentType();
        r.salaryRange = job.getSalaryRange();
        r.description = job.getDescription();
        r.contactEmail = job.getContactEmail();
        r.employerNickname = job.getEmployer().getNickname();
        r.employerId = job.getEmployer().getId();
        r.createdAt = job.getCreatedAt();
        return r;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public String getVisaRequirements() {
        return visaRequirements;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getDescription() {
        return description;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getEmployerNickname() {
        return employerNickname;
    }

    public UUID getEmployerId() {
        return employerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
