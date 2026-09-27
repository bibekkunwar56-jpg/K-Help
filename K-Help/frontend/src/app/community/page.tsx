"use client";

import Link from "next/link";
import { FormEvent, useCallback, useEffect, useState } from "react";
import { Navbar } from "@/components/Navbar";
import { fetchCategories, fetchPosts } from "@/lib/api";
import { formatDate } from "@/lib/format";
import type { Category, Post } from "@/types/community";

export default function CommunityPage() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [posts, setPosts] = useState<Post[]>([]);
  const [category, setCategory] = useState("");
  const [query, setQuery] = useState("");
  const [appliedQuery, setAppliedQuery] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchCategories()
      .then(setCategories)
      .catch(() => setCategories([]));
  }, []);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError("");

    fetchPosts({ category: category || undefined, q: appliedQuery || undefined })
      .then((data) => {
        if (!cancelled) setPosts(data);
      })
      .catch((err) => {
        if (!cancelled) setError(err instanceof Error ? err.message : "Could not load posts");
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, [category, appliedQuery]);

  const onSearch = useCallback(
    (e: FormEvent) => {
      e.preventDefault();
      setAppliedQuery(query.trim());
    },
    [query],
  );

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-5xl px-6 py-14">
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.18em] text-sea">Community</p>
            <h1 className="mt-2 font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
              Ask, share, and help each other
            </h1>
            <p className="mt-2 max-w-xl text-ink/70">
              Experiences from foreign residents in Korea — visas, jobs, housing, language, daily life.
            </p>
          </div>
          <Link
            href="/community/new"
            className="rounded-md bg-sea-deep px-5 py-2.5 text-sm font-semibold text-sand transition hover:bg-ink"
          >
            Write a post
          </Link>
        </div>

        <form onSubmit={onSearch} className="mt-8 flex flex-wrap gap-3">
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search titles and posts..."
            className="min-w-[240px] flex-1 rounded-md border border-sand-deep bg-white px-3 py-2 text-sm outline-none focus:border-sea"
          />
          <button
            type="submit"
            className="rounded-md border border-sand-deep bg-white/70 px-5 py-2 text-sm font-semibold text-ink transition hover:border-sea"
          >
            Search
          </button>
          {appliedQuery ? (
            <button
              type="button"
              onClick={() => {
                setQuery("");
                setAppliedQuery("");
              }}
              className="rounded-md px-3 py-2 text-sm font-medium text-ink/60 transition hover:text-accent"
            >
              Clear
            </button>
          ) : null}
        </form>

        <div className="mt-4 flex flex-wrap gap-2">
          <button
            type="button"
            onClick={() => setCategory("")}
            className={`rounded-full px-4 py-1.5 text-xs font-semibold transition ${
              category === "" ? "bg-ink text-sand" : "border border-sand-deep bg-white/60 text-ink/70 hover:border-sea"
            }`}
          >
            All topics
          </button>
          {categories.map((item) => (
            <button
              key={item.id}
              type="button"
              onClick={() => setCategory(item.slug)}
              className={`rounded-full px-4 py-1.5 text-xs font-semibold transition ${
                category === item.slug
                  ? "bg-ink text-sand"
                  : "border border-sand-deep bg-white/60 text-ink/70 hover:border-sea"
              }`}
            >
              {item.name}
            </button>
          ))}
        </div>

        {error ? <p className="mt-8 text-sm text-accent">{error}</p> : null}
        {loading ? <p className="mt-8 text-sm text-ink/60">Loading posts...</p> : null}

        {!loading && !error && posts.length === 0 ? (
          <div className="mt-8 rounded-2xl border border-sand-deep bg-white/70 p-8 text-center">
            <p className="text-ink/70">No posts here yet.</p>
            <Link href="/community/new" className="mt-3 inline-block text-sm font-semibold text-sea-deep">
              Be the first to write one
            </Link>
          </div>
        ) : null}

        <ul className="mt-8 space-y-4">
          {posts.map((post) => (
            <li key={post.id}>
              <Link
                href={`/community/${post.id}`}
                className="block rounded-2xl border border-sand-deep bg-white/70 p-6 transition hover:border-sea hover:shadow-[0_18px_40px_rgba(11,31,42,0.08)]"
              >
                <span className="inline-block rounded-full bg-sand px-3 py-1 text-xs font-semibold uppercase tracking-[0.12em] text-sea-deep">
                  {post.categoryName}
                </span>
                <h2 className="mt-3 font-[family-name:var(--font-display)] text-2xl font-semibold text-ink">
                  {post.title}
                </h2>
                <p className="mt-2 line-clamp-2 text-sm leading-relaxed text-ink/70">{post.body}</p>
                <p className="mt-4 text-xs text-ink/50">
                  {post.authorNickname} · {formatDate(post.createdAt)} · {post.likeCount}{" "}
                  {post.likeCount === 1 ? "like" : "likes"} · {post.commentCount}{" "}
                  {post.commentCount === 1 ? "comment" : "comments"}
                </p>
              </Link>
            </li>
          ))}
        </ul>
      </section>
    </main>
  );
}
