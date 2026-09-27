package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Destination prefixes for messages from the server to clients
        registry.enableSimpleBroker("/topic", "/queue");
        // Prefix for messages sent from client to @MessageMapping handlers
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // AGENT.md §2: WebSocket (/ws) for 1:1 real-time messaging
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:3000", "*")
                .withSockJS();

        // Also register pure websocket endpoint without SockJS
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:3000", "*");
    }
}
