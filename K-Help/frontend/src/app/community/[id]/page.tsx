"use client";

import Link from "next/link";
import { FormEvent, useCallback, useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { Navbar } from "@/components/Navbar";
import {
  createComment,
  createReport,
  deleteComment,
  deletePost,
  fetchComments,
  fetchMe,
  fetchPost,
  setPostLike,
} from "@/lib/api";
import { formatDateTime } from "@/lib/format";
import type { Comment, Post } from "@/types/community";

export default function PostDetailPage() {
  const params = useParams<{ id: string }>();
  const postId = String(params?.id ?? "");
  const router = useRouter();

  const [post, setPost] = useState<Post | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [meId, setMeId] = useState<string | null>(null);
  const [commentBody, setCommentBody] = useState("");
  const [replyTo, setReplyTo] = useState<string | null>(null);
  const [reportOpen, setReportOpen] = useState(false);
  const [reportReason, setReportReason] = useState("");
  const [notice, setNotice] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);

  const load = useCallback(async () => {
    if (!postId) return;
    try {
      const [loadedPost, loadedComments] = await Promise.all([fetchPost(postId), fetchComments(postId)]);
      setPost(loadedPost);
      setComments(loadedComments);
      setError("");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not load this post");
    } finally {
      setLoading(false);
    }
  }, [postId]);

  useEffect(() => {
    void load();
  }, [load]);

  useEffect(() => {
    fetchMe()
      .then((user) => setMeId(user.id))
      .catch(() => setMeId(null));
  }, []);

  async function toggleLike() {
    if (!post) return;
    if (!meId) {
      router.push("/login");
      return;
    }
    setBusy(true);
    try {
      setPost(await setPostLike(post.id, !post.likedByMe));
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not update your like");
    } finally {
      setBusy(false);
    }
  }

  async function onComment(e: FormEvent) {
    e.preventDefault();
    if (!post) return;
    if (!meId) {
      router.push("/login");
      return;
    }
    setBusy(true);
    try {
      await createComment(post.id, commentBody, replyTo);
      setCommentBody("");
      setReplyTo(null);
      await load();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not post your comment");
    } finally {
      setBusy(false);
    }
  }

  async function onDeleteComment(commentId: string) {
    setBusy(true);
    try {
      await deleteComment(commentId);
      await load();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not delete the comment");
    } finally {
      setBusy(false);
    }
  }

  async function onDeletePost() {
    if (!post) return;
    setBusy(true);
    try {
      await deletePost(post.id);
      router.push("/community");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not delete the post");
      setBusy(false);
    }
  }

  async function onReport(e: FormEvent) {
    e.preventDefault();
    if (!post) return;
    if (!meId) {
      router.push("/login");
      return;
    }
    setBusy(true);
    try {
      await createReport({ targetType: "POST", targetId: post.id, reason: reportReason });
      setReportOpen(false);
      setReportReason("");
      setNotice("Thanks — a moderator will review this post.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not send the report");
    } finally {
      setBusy(false);
    }
  }

  const topLevel = comments.filter((comment) => comment.parentId === null);

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-3xl px-6 py-14">
        <Link href="/community" className="text-sm font-medium text-ink/60 transition hover:text-sea-deep">
          ← Back to community
        </Link>

        {loading ? <p className="mt-6 text-sm text-ink/60">Loading post...</p> : null}
        {error ? <p className="mt-6 text-sm text-accent">{error}</p> : null}

        {post ? (
          <>
            <article className="mt-6 rounded-2xl border border-sand-deep bg-white/80 p-8">
              <span className="inline-block rounded-full bg-sand px-3 py-1 text-xs font-semibold uppercase tracking-[0.12em] text-sea-deep">
                {post.categoryName}
              </span>
              <h1 className="mt-4 font-[family-name:var(--font-display)] text-4xl leading-tight font-semibold text-ink">
                {post.title}
              </h1>
              <p className="mt-3 text-xs text-ink/50">
                {post.authorNickname} · {formatDateTime(post.createdAt)}
              </p>
              <p className="mt-6 whitespace-pre-wrap leading-relaxed text-ink/85">{post.body}</p>

              <div className="mt-8 flex flex-wrap items-center gap-3">
                <button
                  type="button"
                  onClick={toggleLike}
                  disabled={busy}
                  className={`rounded-md px-4 py-2 text-sm font-semibold transition disabled:opacity-60 ${
                    post.likedByMe
                      ? "bg-accent text-sand hover:bg-ink"
                      : "border border-sand-deep bg-white text-ink hover:border-sea"
                  }`}
                >
                  {post.likedByMe ? "Liked" : "Like"} · {post.likeCount}
                </button>
                <span className="text-sm text-ink/60">
                  {post.commentCount} {post.commentCount === 1 ? "comment" : "comments"}
                </span>
                {meId === post.authorId ? (
                  <button
                    type="button"
                    onClick={onDeletePost}
                    disabled={busy}
                    className="ml-auto text-sm font-semibold text-accent transition hover:text-ink disabled:opacity-60"
                  >
                    Delete post
                  </button>
                ) : (
                  <button
                    type="button"
                    onClick={() => setReportOpen((open) => !open)}
                    className="ml-auto text-sm font-medium text-ink/50 transition hover:text-accent"
                  >
                    Report
                  </button>
                )}
              </div>

              {notice ? <p className="mt-4 text-sm text-sea-deep">{notice}</p> : null}

              {reportOpen ? (
                <form onSubmit={onReport} className="mt-4 space-y-3 rounded-xl border border-sand-deep bg-sand/60 p-4">
                  <label className="block text-sm font-medium text-ink">
                    Why are you reporting this post?
                    <textarea
                      required
                      minLength={5}
                      rows={3}
                      value={reportReason}
                      onChange={(e) => setReportReason(e.target.value)}
                      className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
                    />
                  </label>
                  <button
                    type="submit"
                    disabled={busy}
                    className="rounded-md bg-ink px-4 py-2 text-sm font-semibold text-sand transition hover:bg-sea-deep disabled:opacity-60"
                  >
                    Send report
                  </button>
                </form>
              ) : null}
            </article>

            <section className="mt-10">
              <h2 className="font-[family-name:var(--font-display)] text-2xl font-semibold text-ink">Comments</h2>

              <form onSubmit={onComment} className="mt-4 space-y-3">
                {replyTo ? (
                  <p className="text-xs font-medium text-sea">
                    Replying to a comment ·{" "}
                    <button
                      type="button"
                      onClick={() => setReplyTo(null)}
                      className="underline transition hover:text-accent"
                    >
                      cancel
                    </button>
                  </p>
                ) : null}
                <textarea
                  required
                  rows={3}
                  value={commentBody}
                  onChange={(e) => setCommentBody(e.target.value)}
                  placeholder={meId ? "Write a comment..." : "Sign in to comment"}
                  className="w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
                />
                <button
                  type="submit"
                  disabled={busy}
                  className="rounded-md bg-sea-deep px-4 py-2 text-sm font-semibold text-sand transition hover:bg-ink disabled:opacity-60"
                >
                  {busy ? "Sending..." : "Post comment"}
                </button>
              </form>

              {comments.length === 0 ? (
                <p className="mt-6 text-sm text-ink/60">No comments yet.</p>
              ) : null}

              <ul className="mt-6 space-y-4">
                {topLevel.map((comment) => (
                  <li key={comment.id} className="rounded-xl border border-sand-deep bg-white/70 p-4">
                    <CommentBubble
                      comment={comment}
                      mine={meId === comment.authorId}
                      signedIn={Boolean(meId)}
                      busy={busy}
                      onReply={() => {
                        setReplyTo(comment.id);
                        setCommentBody("");
                      }}
                      onDelete={() => onDeleteComment(comment.id)}
                    />
                    <ul className="mt-3 space-y-3 border-l border-sand-deep pl-4">
                      {comments
                        .filter((reply) => reply.parentId === comment.id)
                        .map((reply) => (
                          <li key={reply.id}>
                            <CommentBubble
                              comment={reply}
                              mine={meId === reply.authorId}
                              signedIn={Boolean(meId)}
                              busy={busy}
                              onDelete={() => onDeleteComment(reply.id)}
                            />
                          </li>
                        ))}
                    </ul>
                  </li>
                ))}
              </ul>
            </section>
          </>
        ) : null}
      </section>
    </main>
  );
}

function CommentBubble({
  comment,
  mine,
  signedIn,
  busy,
  onReply,
  onDelete,
}: {
  comment: Comment;
  mine: boolean;
  signedIn: boolean;
  busy: boolean;
  onReply?: () => void;
  onDelete: () => void;
}) {
  return (
    <div>
      <p className="text-xs text-ink/50">
        {comment.authorNickname} · {formatDateTime(comment.createdAt)}
      </p>
      <p className="mt-1 whitespace-pre-wrap text-sm leading-relaxed text-ink/85">{comment.body}</p>
      <div className="mt-2 flex items-center gap-3 text-xs font-medium">
        {onReply && signedIn ? (
          <button type="button" onClick={onReply} className="text-sea transition hover:text-sea-deep">
            Reply
          </button>
        ) : null}
        {mine ? (
          <button
            type="button"
            onClick={onDelete}
            disabled={busy}
            className="text-accent transition hover:text-ink disabled:opacity-60"
          >
            Delete
          </button>
        ) : null}
      </div>
    </div>
  );
}
