"use client";

import Link from "next/link";
import { FormEvent, useCallback, useEffect, useState } from "react";
import { Navbar } from "@/components/Navbar";
import { fetchGuides, fetchGuideTopics } from "@/lib/api";
import { topicLabel } from "@/types/guide";
import type { Guide } from "@/types/guide";

export default function GuidesPage() {
  const [guides, setGuides] = useState<Guide[]>([]);
  const [topics, setTopics] = useState<string[]>([]);
  const [topic, setTopic] = useState("");
  const [query, setQuery] = useState("");
  const [appliedQuery, setAppliedQuery] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchGuideTopics()
      .then(setTopics)
      .catch(() => setTopics([]));
  }, []);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError("");

    fetchGuides({ topic: topic || undefined, q: appliedQuery || undefined })
      .then((data) => {
        if (!cancelled) setGuides(data);
      })
      .catch((err) => {
        if (!cancelled) setError(err instanceof Error ? err.message : "Could not load guides");
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, [topic, appliedQuery]);

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
        <p className="text-xs font-semibold uppercase tracking-[0.18em] text-sea">Korea Information</p>
        <h1 className="mt-2 font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
          Guides for living in Korea
        </h1>
        <p className="mt-2 max-w-2xl text-ink/70">
          Short, practical walkthroughs — visas, health insurance, banking, phones, housing, and work rules.
          Written for foreign residents, not for lawyers.
        </p>

        <form onSubmit={onSearch} className="mt-8 flex flex-wrap gap-3">
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search guides, e.g. ARC, deposit, insurance..."
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
            onClick={() => setTopic("")}
            className={`rounded-full px-4 py-1.5 text-xs font-semibold transition ${
              topic === "" ? "bg-ink text-sand" : "border border-sand-deep bg-white/60 text-ink/70 hover:border-sea"
            }`}
          >
            All topics
          </button>
          {topics.map((item) => (
            <button
              key={item}
              type="button"
              onClick={() => setTopic(item)}
              className={`rounded-full px-4 py-1.5 text-xs font-semibold transition ${
                topic === item
                  ? "bg-ink text-sand"
                  : "border border-sand-deep bg-white/60 text-ink/70 hover:border-sea"
              }`}
            >
              {topicLabel(item)}
            </button>
          ))}
        </div>

        {error ? <p className="mt-8 text-sm text-accent">{error}</p> : null}
        {loading ? <p className="mt-8 text-sm text-ink/60">Loading guides...</p> : null}

        {!loading && !error && guides.length === 0 ? (
          <div className="mt-8 rounded-2xl border border-sand-deep bg-white/70 p-8 text-center text-ink/70">
            No guides match that search yet.
          </div>
        ) : null}

        <ul className="mt-8 grid gap-4 sm:grid-cols-2">
          {guides.map((guide) => (
            <li key={guide.id}>
              <Link
                href={`/guides/${guide.slug}`}
                className="flex h-full flex-col rounded-2xl border border-sand-deep bg-white/70 p-6 transition hover:border-sea hover:shadow-[0_18px_40px_rgba(11,31,42,0.08)]"
              >
                <span className="inline-block self-start rounded-full bg-sand px-3 py-1 text-xs font-semibold uppercase tracking-[0.12em] text-sea-deep">
                  {topicLabel(guide.topic)}
                </span>
                <h2 className="mt-3 font-[family-name:var(--font-display)] text-xl font-semibold text-ink">
                  {guide.title}
                </h2>
                <p className="mt-2 text-sm leading-relaxed text-ink/70">{guide.summary}</p>
                <span className="mt-4 text-xs font-semibold text-sea-deep">Read the guide →</span>
              </Link>
            </li>
          ))}
        </ul>
      </section>
    </main>
  );
}
