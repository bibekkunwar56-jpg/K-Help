-- V5: Chat & Messages Schema
-- Darlami Magar Risipna (Community & Chat Lead) ownership according to AGENT.md

CREATE TABLE chat_messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipient_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_chat_messages_sender ON chat_messages(sender_id);
CREATE INDEX idx_chat_messages_recipient ON chat_messages(recipient_id);
CREATE INDEX idx_chat_messages_conversation ON chat_messages(sender_id, recipient_id, created_at);
