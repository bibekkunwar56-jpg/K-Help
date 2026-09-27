package com.example.demo.dto;

import java.util.UUID;

public class ReportResponse {
    private UUID id;
    private String targetType;
    private UUID targetId;
    private String reason;
    private String status;

    public ReportResponse(UUID id, String targetType, UUID targetId, String reason, String status) {
        this.id = id;
        this.targetType = targetType;
        this.targetId = targetId;
        this.reason = reason;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getTargetType() {
        return targetType;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }
}
