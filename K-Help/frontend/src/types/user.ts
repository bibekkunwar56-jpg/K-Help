export type User = {
  id: string;
  email: string;
  nickname: string;
  visaType?: string | null;
  nationality?: string | null;
  role: string;
};

export type AuthResponse = {
  token: string;
  user: User;
};

export type ApiError = {
  error: string;
  fields?: Record<string, string>;
};
