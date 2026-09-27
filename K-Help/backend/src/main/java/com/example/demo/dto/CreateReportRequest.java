package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class CreateReportRequest {

    @NotBlank
    @Pattern(regexp = "POST|COMMENT", message = "targetType must be POST or COMMENT")
    private String targetType;

    private UUID targetId;

    @NotBlank
    @Size(min = 5, max = 2000)
    private String reason;

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public void setTargetId(UUID targetId) {
        this.targetId = targetId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
