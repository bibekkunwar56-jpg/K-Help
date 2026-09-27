package com.example.demo.service;

import com.example.demo.dto.ChatMessageResponse;
import com.example.demo.dto.ConversationSummary;
import com.example.demo.dto.SendMessageRequest;
import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.User;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(
            ChatMessageRepository chatMessageRepository,
            UserRepository userRepository,
            SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public ChatMessageResponse sendMessage(UUID senderId, SendMessageRequest req) {
        if (senderId.equals(req.getRecipientId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot send message to yourself");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));
        User recipient = userRepository.findById(req.getRecipientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient not found"));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(req.getContent().trim());
        message.setRead(false);

        ChatMessage saved = chatMessageRepository.save(message);
        ChatMessageResponse response = ChatMessageResponse.from(saved);

        // Push real-time to the recipient's personal topic: /topic/user/{recipientId}/messages
        try {
            messagingTemplate.convertAndSend("/topic/user/" + recipient.getId() + "/messages", response);
        } catch (Exception ignored) {
            // Log/ignore if WebSocket client isn't connected
        }

        return response;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getConversation(UUID userId, UUID partnerId) {
        return chatMessageRepository.findConversation(userId, partnerId)
                .stream()
                .map(ChatMessageResponse::from)
                .toList();
    }

    @Transactional
    public void markConversationAsRead(UUID userId, UUID partnerId) {
        chatMessageRepository.markAsRead(userId, partnerId);
    }

    @Transactional(readOnly = true)
    public List<ConversationSummary> getConversations(UUID userId) {
        List<UUID> partnerIds = chatMessageRepository.findConversationPartnerIds(userId);
        List<ConversationSummary> summaries = new ArrayList<>();

        for (UUID partnerId : partnerIds) {
            User partner = userRepository.findById(partnerId).orElse(null);
            if (partner == null) continue;

            List<ChatMessage> conversation = chatMessageRepository.findConversation(userId, partnerId);
            if (conversation.isEmpty()) continue;

            ChatMessage lastMsg = conversation.get(conversation.size() - 1);
            long unread = conversation.stream()
                    .filter(m -> m.getRecipient().getId().equals(userId) && !m.isRead())
                    .count();

            summaries.add(new ConversationSummary(
                    partner.getId(),
                    partner.getNickname(),
                    lastMsg.getContent(),
                    lastMsg.getCreatedAt(),
                    unread
            ));
        }

        // Sort latest first
        summaries.sort((a, b) -> b.getLastMessageTime().compareTo(a.getLastMessageTime()));
        return summaries;
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(UUID userId) {
        return chatMessageRepository.countUnread(userId);
    }
}
