package com.example.demo.controller;

import com.example.demo.dto.ChatMessageResponse;
import com.example.demo.dto.ConversationSummary;
import com.example.demo.dto.SendMessageRequest;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.ChatService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessageResponse sendMessage(
            @Valid @RequestBody SendMessageRequest req,
            Authentication authentication) {
        return chatService.sendMessage(CurrentUser.required(authentication), req);
    }

    @GetMapping("/conversations")
    public List<ConversationSummary> getConversations(Authentication authentication) {
        return chatService.getConversations(CurrentUser.required(authentication));
    }

    @GetMapping("/conversations/{partnerId}")
    public List<ChatMessageResponse> getConversation(
            @PathVariable UUID partnerId,
            Authentication authentication) {
        return chatService.getConversation(CurrentUser.required(authentication), partnerId);
    }

    @PutMapping("/conversations/{partnerId}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(
            @PathVariable UUID partnerId,
            Authentication authentication) {
        chatService.markConversationAsRead(CurrentUser.required(authentication), partnerId);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount(Authentication authentication) {
        long count = chatService.getUnreadCount(CurrentUser.required(authentication));
        return Map.of("unreadCount", count);
    }
}
