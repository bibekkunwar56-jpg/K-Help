export type ChatMessage = {
  id: string;
  senderId: string;
  senderNickname: string;
  recipientId: string;
  recipientNickname: string;
  content: string;
  read: boolean;
  createdAt: string;
};

export type ConversationSummary = {
  partnerId: string;
  partnerNickname: string;
  lastMessage: string;
  lastMessageTime: string;
  unreadCount: number;
};

export type AiResponse = {
  result: string;
  originalText: string;
  mode: string;
};
