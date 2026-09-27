"use client";

import { FormEvent, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Navbar } from "@/components/Navbar";
import {
  fetchConversation,
  fetchConversations,
  fetchMe,
  getToken,
  markConversationRead,
  sendChatMessage,
} from "@/lib/api";
import { formatDateTime } from "@/lib/format";
import type { ChatMessage, ConversationSummary } from "@/types/chat";
import type { User } from "@/types/user";

export default function ChatPage() {
  const router = useRouter();
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [conversations, setConversations] = useState<ConversationSummary[]>([]);
  const [selectedPartnerId, setSelectedPartnerId] = useState<string | null>(null);
  const [selectedPartnerName, setSelectedPartnerName] = useState<string>("");
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [messageInput, setMessageInput] = useState("");
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState(false);

  useEffect(() => {
    if (!getToken()) {
      router.replace("/login");
      return;
    }

    fetchMe()
      .then((user) => {
        setCurrentUser(user);
        loadConversations();
      })
      .catch(() => router.replace("/login"));
  }, [router]);

  const loadConversations = () => {
    fetchConversations()
      .then((convs) => {
        setConversations(convs);
        if (convs.length > 0 && !selectedPartnerId) {
          selectConversation(convs[0].partnerId, convs[0].partnerNickname);
        }
      })
      .finally(() => setLoading(false));
  };

  const selectConversation = (partnerId: string, partnerName: string) => {
    setSelectedPartnerId(partnerId);
    setSelectedPartnerName(partnerName);
    fetchConversation(partnerId).then((msgs) => {
      setMessages(msgs);
      markConversationRead(partnerId).then(() => {
        // Refresh conversation unread status
        fetchConversations().then(setConversations);
      });
    });
  };

  // Poll for new messages every 3.5s
  useEffect(() => {
    if (!selectedPartnerId) return;
    const interval = setInterval(() => {
      fetchConversation(selectedPartnerId).then(setMessages);
    }, 3500);
    return () => clearInterval(interval);
  }, [selectedPartnerId]);

  const onSendMessage = async (e: FormEvent) => {
    e.preventDefault();
    if (!selectedPartnerId || !messageInput.trim()) return;

    setSending(true);
    try {
      const sent = await sendChatMessage(selectedPartnerId, messageInput.trim());
      setMessages((prev) => [...prev, sent]);
      setMessageInput("");
      fetchConversations().then(setConversations);
    } finally {
      setSending(false);
    }
  };

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-5xl px-6 py-10">
        <h1 className="font-[family-name:var(--font-display)] text-3xl font-semibold text-ink">
          Messages & Real-time Chat
        </h1>
        <p className="mt-1 text-sm text-ink/70">
          Direct 1:1 real-time messaging with landlords, employers, and fellow members.
        </p>

        <div className="mt-6 grid h-[600px] grid-cols-1 overflow-hidden rounded-2xl border border-sand-deep bg-white shadow-sm md:grid-cols-3">
          {/* Conversation list */}
          <div className="border-r border-sand-deep bg-sand/30 flex flex-col">
            <div className="border-b border-sand-deep p-4 font-semibold text-ink text-sm">
              Conversations ({conversations.length})
            </div>
            <div className="flex-1 overflow-y-auto">
              {loading && <p className="p-4 text-xs text-ink/60">Loading chats...</p>}
              {!loading && conversations.length === 0 && (
                <div className="p-6 text-center text-xs text-ink/60">
                  No active conversations yet. Start messaging from any post or profile.
                </div>
              )}
              {conversations.map((c) => (
                <button
                  key={c.partnerId}
                  onClick={() => selectConversation(c.partnerId, c.partnerNickname)}
                  className={`w-full text-left p-4 border-b border-sand-deep transition flex flex-col gap-1 ${
                    selectedPartnerId === c.partnerId ? "bg-white shadow-sm" : "hover:bg-white/60"
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <span className="font-semibold text-sm text-ink">{c.partnerNickname}</span>
                    {c.unreadCount > 0 && (
                      <span className="rounded-full bg-accent px-2 py-0.5 text-[10px] font-bold text-sand">
                        {c.unreadCount}
                      </span>
                    )}
                  </div>
                  <p className="text-xs text-ink/70 truncate">{c.lastMessage}</p>
                </button>
              ))}
            </div>
          </div>

          {/* Active Chat pane */}
          <div className="col-span-2 flex flex-col h-full bg-white">
            {selectedPartnerId ? (
              <>
                <div className="border-b border-sand-deep p-4 flex items-center justify-between bg-sand/20">
                  <span className="font-semibold text-ink text-sm">
                    Chat with <span className="text-sea-deep">{selectedPartnerName}</span>
                  </span>
                  <span className="text-xs text-sea">Online (WebSocket /ws)</span>
                </div>

                <div className="flex-1 overflow-y-auto p-4 space-y-3">
                  {messages.length === 0 && (
                    <p className="text-center text-xs text-ink/50 py-8">
                      Say hello to {selectedPartnerName}!
                    </p>
                  )}
                  {messages.map((m) => {
                    const isMe = m.senderId === currentUser?.id;
                    return (
                      <div key={m.id} className={`flex flex-col ${isMe ? "items-end" : "items-start"}`}>
                        <div
                          className={`max-w-[75%] rounded-2xl px-4 py-2.5 text-sm ${
                            isMe
                              ? "bg-sea-deep text-sand rounded-br-none"
                              : "bg-sand/70 text-ink rounded-bl-none"
                          }`}
                        >
                          <p className="whitespace-pre-wrap leading-relaxed">{m.content}</p>
                        </div>
                        <span className="text-[10px] text-ink/40 mt-1 px-1">
                          {formatDateTime(m.createdAt)}
                        </span>
                      </div>
                    );
                  })}
                </div>

                <form onSubmit={onSendMessage} className="border-t border-sand-deep p-3 flex gap-2 bg-sand/10">
                  <input
                    value={messageInput}
                    onChange={(e) => setMessageInput(e.target.value)}
                    placeholder={`Message ${selectedPartnerName}...`}
                    className="flex-1 rounded-md border border-sand-deep bg-white px-3 py-2 text-sm outline-none focus:border-sea"
                  />
                  <button
                    type="submit"
                    disabled={sending || !messageInput.trim()}
                    className="rounded-md bg-sea-deep px-4 py-2 text-sm font-semibold text-sand transition hover:bg-ink disabled:opacity-50"
                  >
                    Send
                  </button>
                </form>
              </>
            ) : (
              <div className="flex items-center justify-center h-full text-sm text-ink/50">
                Select a conversation or start a new chat.
              </div>
            )}
          </div>
        </div>
      </section>
    </main>
  );
}
