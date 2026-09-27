export type Category = {
  id: string;
  name: string;
  slug: string;
  description?: string | null;
};

export type Post = {
  id: string;
  title: string;
  body: string;
  categoryName: string;
  categorySlug: string;
  authorNickname: string;
  authorId: string;
  likeCount: number;
  commentCount: number;
  likedByMe: boolean;
  createdAt: string;
};

export type Comment = {
  id: string;
  body: string;
  authorNickname: string;
  authorId: string;
  parentId: string | null;
  createdAt: string;
};

export type Report = {
  id: string;
  targetType: "POST" | "COMMENT";
  targetId: string;
  reason: string;
  status: string;
};
