import type { ApiError, AuthResponse, User } from "@/types/user";
import type { Category, Comment, Post, Report } from "@/types/community";
import type { Guide } from "@/types/guide";
import type { House, Job } from "@/types/listings";
import type { AiResponse, ChatMessage, ConversationSummary } from "@/types/chat";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export const TOKEN_KEY = "khelp_token";

export function getToken(): string | null {
  if (typeof window === "undefined") return null;
  return localStorage.getItem(TOKEN_KEY);
}

export function setToken(token: string) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}

async function parseJson<T>(res: Response): Promise<T> {
  const text = await res.text();
  const data = text ? JSON.parse(text) : null;
  if (!res.ok) {
    const error = data as ApiError | null;
    throw new Error(error?.error ?? "Request failed");
  }
  return data as T;
}

/**
 * Single entry point for backend calls: attaches the JWT when one is stored and
 * normalises error bodies ({"error": "..."}) into a thrown Error.
 */
export async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const headers = new Headers(init.headers);
  if (init.body) headers.set("Content-Type", "application/json");

  const token = getToken();
  if (token) headers.set("Authorization", `Bearer ${token}`);

  const res = await fetch(`${API_URL}${path}`, { ...init, headers, cache: "no-store" });
  return parseJson<T>(res);
}

// ------------------------------------------------------------------ auth

export async function fetchHello(): Promise<string> {
  const data = await request<{ message: string }>("/api/hello");
  return data.message;
}

export function registerUser(body: {
  email: string;
  password: string;
  nickname: string;
  visaType?: string;
  nationality?: string;
}): Promise<AuthResponse> {
  return request<AuthResponse>("/api/auth/register", {
    method: "POST",
    body: JSON.stringify(body),
  });
}

export function loginUser(body: { email: string; password: string }): Promise<AuthResponse> {
  return request<AuthResponse>("/api/auth/login", {
    method: "POST",
    body: JSON.stringify(body),
  });
}

export function fetchMe(): Promise<User> {
  return request<User>("/api/users/me");
}

// ------------------------------------------------------------- community

export function fetchCategories(): Promise<Category[]> {
  return request<Category[]>("/api/categories");
}

export function fetchPosts(params: { category?: string; q?: string; authorId?: string } = {}): Promise<Post[]> {
  const search = new URLSearchParams();
  if (params.category) search.set("category", params.category);
  if (params.q) search.set("q", params.q);
  if (params.authorId) search.set("authorId", params.authorId);
  const query = search.toString();
  return request<Post[]>(`/api/posts${query ? `?${query}` : ""}`);
}

export function fetchPost(id: string): Promise<Post> {
  return request<Post>(`/api/posts/${id}`);
}

export function createPost(body: { categorySlug: string; title: string; body: string }): Promise<Post> {
  return request<Post>("/api/posts", { method: "POST", body: JSON.stringify(body) });
}

export function updatePost(id: string, body: { title: string; body: string }): Promise<Post> {
  return request<Post>(`/api/posts/${id}`, { method: "PUT", body: JSON.stringify(body) });
}

export function deletePost(id: string): Promise<void> {
  return request<void>(`/api/posts/${id}`, { method: "DELETE" });
}

/** Pass true to like, false to unlike. Returns the refreshed post. */
export function setPostLike(id: string, liked: boolean): Promise<Post> {
  return request<Post>(`/api/posts/${id}/like`, { method: liked ? "POST" : "DELETE" });
}

export function fetchComments(postId: string): Promise<Comment[]> {
  return request<Comment[]>(`/api/posts/${postId}/comments`);
}

export function createComment(postId: string, body: string, parentId?: string | null): Promise<Comment> {
  return request<Comment>(`/api/posts/${postId}/comments`, {
    method: "POST",
    body: JSON.stringify({ body, parentId: parentId ?? null }),
  });
}

export function deleteComment(id: string): Promise<void> {
  return request<void>(`/api/comments/${id}`, { method: "DELETE" });
}

export function createReport(body: {
  targetType: "POST" | "COMMENT";
  targetId: string;
  reason: string;
}): Promise<Report> {
  return request<Report>("/api/reports", { method: "POST", body: JSON.stringify(body) });
}

// ---------------------------------------------------------------- guides

export function fetchGuides(params: { topic?: string; q?: string } = {}): Promise<Guide[]> {
  const search = new URLSearchParams();
  if (params.topic) search.set("topic", params.topic);
  if (params.q) search.set("q", params.q);
  const query = search.toString();
  return request<Guide[]>(`/api/guides${query ? `?${query}` : ""}`);
}

export function fetchGuideTopics(): Promise<string[]> {
  return request<string[]>("/api/guides/topics");
}

export function fetchGuide(slug: string): Promise<Guide> {
  return request<Guide>(`/api/guides/${slug}`);
}

export function createGuide(body: {
  slug: string;
  title: string;
  summary: string;
  body: string;
  topic: string;
  sourceUrl?: string | null;
  published?: boolean;
}): Promise<Guide> {
  return request<Guide>("/api/guides", { method: "POST", body: JSON.stringify(body) });
}

export function updateGuide(
  slug: string,
  body: {
    slug: string;
    title: string;
    summary: string;
    body: string;
    topic: string;
    sourceUrl?: string | null;
    published?: boolean;
  },
): Promise<Guide> {
  return request<Guide>(`/api/guides/${slug}`, { method: "PUT", body: JSON.stringify(body) });
}

export function deleteGuide(slug: string): Promise<void> {
  return request<void>(`/api/guides/${slug}`, { method: "DELETE" });
}

// ---------------------------------------------------------------- jobs

export function fetchJobs(params: { location?: string; q?: string } = {}): Promise<Job[]> {
  const search = new URLSearchParams();
  if (params.location) search.set("location", params.location);
  if (params.q) search.set("q", params.q);
  const q = search.toString();
  return request<Job[]>(`/api/jobs${q ? `?${q}` : ""}`);
}

export function fetchJob(id: string): Promise<Job> {
  return request<Job>(`/api/jobs/${id}`);
}

export function createJob(body: {
  title: string;
  companyName: string;
  location: string;
  visaRequirements?: string;
  employmentType?: string;
  salaryRange?: string;
  description: string;
  contactEmail?: string;
}): Promise<Job> {
  return request<Job>("/api/jobs", { method: "POST", body: JSON.stringify(body) });
}

export function deleteJob(id: string): Promise<void> {
  return request<void>(`/api/jobs/${id}`, { method: "DELETE" });
}

// ------------------------------------------------------------- housing

export function fetchHouses(params: { location?: string; maxRent?: number; maxDeposit?: number } = {}): Promise<House[]> {
  const search = new URLSearchParams();
  if (params.location) search.set("location", params.location);
  if (params.maxRent) search.set("maxRent", String(params.maxRent));
  if (params.maxDeposit) search.set("maxDeposit", String(params.maxDeposit));
  const q = search.toString();
  return request<House[]>(`/api/housing${q ? `?${q}` : ""}`);
}

export function fetchHouse(id: string): Promise<House> {
  return request<House>(`/api/housing/${id}`);
}

export function createHouse(body: {
  title: string;
  housingType?: string;
  location: string;
  depositKrw: number;
  monthlyRentKrw: number;
  maintenanceFeeKrw?: number;
  floorLevel?: string;
  description: string;
  contactPhone?: string;
}): Promise<House> {
  return request<House>("/api/housing", { method: "POST", body: JSON.stringify(body) });
}

export function deleteHouse(id: string): Promise<void> {
  return request<void>(`/api/housing/${id}`, { method: "DELETE" });
}

// ---------------------------------------------------------------- chat

export function fetchConversations(): Promise<ConversationSummary[]> {
  return request<ConversationSummary[]>("/api/chat/conversations");
}

export function fetchConversation(partnerId: string): Promise<ChatMessage[]> {
  return request<ChatMessage[]>(`/api/chat/conversations/${partnerId}`);
}

export function sendChatMessage(recipientId: string, content: string): Promise<ChatMessage> {
  return request<ChatMessage>("/api/chat/messages", {
    method: "POST",
    body: JSON.stringify({ recipientId, content }),
  });
}

export function markConversationRead(partnerId: string): Promise<void> {
  return request<void>(`/api/chat/conversations/${partnerId}/read`, {
    method: "PUT",
  });
}

export function fetchUnreadChatCount(): Promise<{ unreadCount: number }> {
  return request<{ unreadCount: number }>("/api/chat/unread-count");
}

// ---------------------------------------------------------------- AI services

export function aiTranslate(text: string, targetLanguage: string): Promise<AiResponse> {
  return request<AiResponse>("/api/ai/translate", {
    method: "POST",
    body: JSON.stringify({ text, targetLanguage }),
  });
}

export function aiMakePolite(text: string, formality = "HONORIFIC"): Promise<AiResponse> {
  return request<AiResponse>("/api/ai/polite", {
    method: "POST",
    body: JSON.stringify({ text, formality }),
  });
}

export function aiSummarize(text: string): Promise<AiResponse> {
  return request<AiResponse>("/api/ai/summarize", {
    method: "POST",
    body: JSON.stringify({ text }),
  });
}
