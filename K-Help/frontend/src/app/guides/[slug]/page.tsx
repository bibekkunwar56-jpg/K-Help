"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { Navbar } from "@/components/Navbar";
import { fetchGuide } from "@/lib/api";
import { topicLabel } from "@/types/guide";
import type { Guide } from "@/types/guide";

export default function GuideDetailPage() {
  const params = useParams<{ slug: string }>();
  const slug = String(params?.slug ?? "");

  const [guide, setGuide] = useState<Guide | null>(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!slug) return;
    fetchGuide(slug)
      .then(setGuide)
      .catch((err) => setError(err instanceof Error ? err.message : "Could not load this guide"))
      .finally(() => setLoading(false));
  }, [slug]);

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-3xl px-6 py-14">
        <Link href="/guides" className="text-sm font-medium text-ink/60 transition hover:text-sea-deep">
          ← All guides
        </Link>

        {loading ? <p className="mt-6 text-sm text-ink/60">Loading guide...</p> : null}
        {error ? <p className="mt-6 text-sm text-accent">{error}</p> : null}

        {guide ? (
          <article className="mt-6 rounded-2xl border border-sand-deep bg-white/80 p-8">
            <span className="inline-block rounded-full bg-sand px-3 py-1 text-xs font-semibold uppercase tracking-[0.12em] text-sea-deep">
              {topicLabel(guide.topic)}
            </span>
            <h1 className="mt-4 font-[family-name:var(--font-display)] text-4xl leading-tight font-semibold text-ink">
              {guide.title}
            </h1>
            <p className="mt-4 text-lg leading-relaxed text-ink/75">{guide.summary}</p>

            <div className="mt-8 whitespace-pre-wrap border-t border-sand-deep pt-8 leading-relaxed text-ink/85">
              {guide.body}
            </div>

            {guide.sourceUrl ? (
              <p className="mt-8 border-t border-sand-deep pt-6 text-sm text-ink/70">
                Official source:{" "}
                <a
                  href={guide.sourceUrl}
                  target="_blank"
                  rel="noreferrer"
                  className="font-semibold text-sea-deep underline"
                >
                  {guide.sourceUrl}
                </a>
              </p>
            ) : null}

            <p className="mt-6 text-xs text-ink/45">
              Last updated {new Date(guide.updatedAt).toLocaleDateString()}
            </p>
          </article>
        ) : null}
      </section>
    </main>
  );
}
