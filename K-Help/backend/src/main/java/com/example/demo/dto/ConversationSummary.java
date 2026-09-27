package com.example.demo.dto;

import java.time.Instant;
import java.util.UUID;

public class ConversationSummary {

    private UUID partnerId;
    private String partnerNickname;
    private String lastMessage;
    private Instant lastMessageTime;
    private long unreadCount;

    public ConversationSummary(UUID partnerId, String partnerNickname, String lastMessage, Instant lastMessageTime, long unreadCount) {
        this.partnerId = partnerId;
        this.partnerNickname = partnerNickname;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
    }

    public UUID getPartnerId() {
        return partnerId;
    }

    public String getPartnerNickname() {
        return partnerNickname;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Instant getLastMessageTime() {
        return lastMessageTime;
    }

    public long getUnreadCount() {
        return unreadCount;
    }
}
