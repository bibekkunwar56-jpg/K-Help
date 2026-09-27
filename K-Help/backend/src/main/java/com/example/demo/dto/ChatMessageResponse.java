package com.example.demo.dto;

import com.example.demo.entity.ChatMessage;
import java.time.Instant;
import java.util.UUID;

public class ChatMessageResponse {

    private UUID id;
    private UUID senderId;
    private String senderNickname;
    private UUID recipientId;
    private String recipientNickname;
    private String content;
    private boolean read;
    private Instant createdAt;

    public static ChatMessageResponse from(ChatMessage msg) {
        ChatMessageResponse r = new ChatMessageResponse();
        r.id = msg.getId();
        r.senderId = msg.getSender().getId();
        r.senderNickname = msg.getSender().getNickname();
        r.recipientId = msg.getRecipient().getId();
        r.recipientNickname = msg.getRecipient().getNickname();
        r.content = msg.getContent();
        r.read = msg.isRead();
        r.createdAt = msg.getCreatedAt();
        return r;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public String getRecipientNickname() {
        return recipientNickname;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
