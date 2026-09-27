"use client";

import Link from "next/link";
import { FormEvent, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Navbar } from "@/components/Navbar";
import { createPost, fetchCategories, getToken } from "@/lib/api";
import type { Category } from "@/types/community";

export default function NewPostPage() {
  const router = useRouter();
  const [categories, setCategories] = useState<Category[]>([]);
  const [categorySlug, setCategorySlug] = useState("");
  const [title, setTitle] = useState("");
  const [body, setBody] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!getToken()) {
      router.replace("/login");
      return;
    }

    fetchCategories()
      .then((data) => {
        setCategories(data);
        if (data.length > 0) setCategorySlug(data[0].slug);
      })
      .catch((err) => setError(err instanceof Error ? err.message : "Could not load categories"));
  }, [router]);

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const post = await createPost({ categorySlug, title, body });
      router.push(`/community/${post.id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not create the post");
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-2xl px-6 py-14">
        <Link href="/community" className="text-sm font-medium text-ink/60 transition hover:text-sea-deep">
          ← Back to community
        </Link>

        <h1 className="mt-4 font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
          Write a post
        </h1>
        <p className="mt-2 text-ink/70">Share a question or an experience with the community.</p>

        <form onSubmit={onSubmit} className="mt-8 space-y-5">
          <label className="block text-sm font-medium text-ink">
            Topic
            <select
              required
              value={categorySlug}
              onChange={(e) => setCategorySlug(e.target.value)}
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            >
              {categories.length === 0 ? <option value="">Loading topics...</option> : null}
              {categories.map((category) => (
                <option key={category.id} value={category.slug}>
                  {category.name}
                </option>
              ))}
            </select>
          </label>

          <label className="block text-sm font-medium text-ink">
            Title
            <input
              required
              minLength={3}
              maxLength={200}
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="What do you need help with?"
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>

          <label className="block text-sm font-medium text-ink">
            Post
            <textarea
              required
              rows={10}
              value={body}
              onChange={(e) => setBody(e.target.value)}
              placeholder="Give as much detail as you can — city, dates, documents you already have..."
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>

          {error ? <p className="text-sm text-accent">{error}</p> : null}

          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-md bg-sea-deep px-4 py-2.5 text-sm font-semibold text-sand transition hover:bg-ink disabled:opacity-60"
          >
            {loading ? "Publishing..." : "Publish post"}
          </button>
        </form>
      </section>
    </main>
  );
}
