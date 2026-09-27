package com.example.demo.repository;

import com.example.demo.entity.ChatMessage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Query("""
            SELECT m FROM ChatMessage m
            JOIN FETCH m.sender
            JOIN FETCH m.recipient
            WHERE (m.sender.id = :u1 AND m.recipient.id = :u2)
               OR (m.sender.id = :u2 AND m.recipient.id = :u1)
            ORDER BY m.createdAt ASC
            """)
    List<ChatMessage> findConversation(@Param("u1") UUID u1, @Param("u2") UUID u2);

    @Query("""
            SELECT DISTINCT CASE WHEN m.sender.id = :userId THEN m.recipient.id ELSE m.sender.id END
            FROM ChatMessage m
            WHERE m.sender.id = :userId OR m.recipient.id = :userId
            """)
    List<UUID> findConversationPartnerIds(@Param("userId") UUID userId);

    @Modifying
    @Query("""
            UPDATE ChatMessage m
            SET m.read = true
            WHERE m.sender.id = :partnerId AND m.recipient.id = :userId AND m.read = false
            """)
    void markAsRead(@Param("userId") UUID userId, @Param("partnerId") UUID partnerId);

    @Query("""
            SELECT COUNT(m) FROM ChatMessage m
            WHERE m.recipient.id = :userId AND m.read = false
            """)
    long countUnread(@Param("userId") UUID userId);
}
