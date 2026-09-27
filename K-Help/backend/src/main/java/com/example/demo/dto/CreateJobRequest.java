package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateJobRequest {

    @NotBlank
    @Size(min = 3, max = 200)
    private String title;

    @NotBlank
    @Size(min = 2, max = 150)
    private String companyName;

    @NotBlank
    @Size(min = 2, max = 120)
    private String location;

    @Size(max = 150)
    private String visaRequirements;

    private String employmentType = "FULL_TIME";

    @Size(max = 100)
    private String salaryRange;

    @NotBlank
    @Size(min = 10, max = 10000)
    private String description;

    @Size(max = 255)
    private String contactEmail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVisaRequirements() {
        return visaRequirements;
    }

    public void setVisaRequirements(String visaRequirements) {
        this.visaRequirements = visaRequirements;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
