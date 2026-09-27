"use client";

import Link from "next/link";
import { FormEvent, useEffect, useState } from "react";
import { Navbar } from "@/components/Navbar";
import { fetchJobs } from "@/lib/api";
import { formatDate } from "@/lib/format";
import type { Job } from "@/types/listings";

export default function JobsPage() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [location, setLocation] = useState("");
  const [query, setQuery] = useState("");
  const [appliedQuery, setAppliedQuery] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    fetchJobs({ location: location || undefined, q: appliedQuery || undefined })
      .then((data) => {
        if (!cancelled) setJobs(data);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [location, appliedQuery]);

  const onSearch = (e: FormEvent) => {
    e.preventDefault();
    setAppliedQuery(query.trim());
  };

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-5xl px-6 py-14">
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.18em] text-sea">Career</p>
            <h1 className="mt-2 font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
              Foreigner-friendly Jobs in Korea
            </h1>
            <p className="mt-2 max-w-xl text-ink/70">
              Listings clearly mentioning visa sponsorships, English-friendly environments, and flexible roles.
            </p>
          </div>
          <Link
            href="/jobs/new"
            className="rounded-md bg-sea-deep px-5 py-2.5 text-sm font-semibold text-sand transition hover:bg-ink"
          >
            Post a Job
          </Link>
        </div>

        <form onSubmit={onSearch} className="mt-8 flex flex-wrap gap-3">
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search keywords (e.g. Developer, Teacher, Marketing)..."
            className="min-w-[240px] flex-1 rounded-md border border-sand-deep bg-white px-3 py-2 text-sm outline-none focus:border-sea"
          />
          <input
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            placeholder="Location (Seoul, Busan, Suwon)..."
            className="w-48 rounded-md border border-sand-deep bg-white px-3 py-2 text-sm outline-none focus:border-sea"
          />
          <button
            type="submit"
            className="rounded-md border border-sand-deep bg-white/70 px-5 py-2 text-sm font-semibold text-ink transition hover:border-sea"
          >
            Filter
          </button>
        </form>

        {loading ? <p className="mt-8 text-sm text-ink/60">Loading job postings...</p> : null}

        {!loading && jobs.length === 0 ? (
          <div className="mt-8 rounded-2xl border border-sand-deep bg-white/70 p-8 text-center">
            <p className="text-ink/70">No job openings found with these criteria.</p>
            <Link href="/jobs/new" className="mt-3 inline-block text-sm font-semibold text-sea-deep">
              Post the first vacancy
            </Link>
          </div>
        ) : null}

        <ul className="mt-8 space-y-4">
          {jobs.map((job) => (
            <li
              key={job.id}
              className="rounded-2xl border border-sand-deep bg-white/70 p-6 transition hover:border-sea hover:shadow-[0_18px_40px_rgba(11,31,42,0.08)]"
            >
              <div className="flex flex-wrap items-baseline justify-between gap-2">
                <span className="text-sm font-bold text-sea-deep">{job.companyName}</span>
                <span className="text-xs text-ink/50">{formatDate(job.createdAt)}</span>
              </div>
              <h2 className="mt-2 font-[family-name:var(--font-display)] text-2xl font-semibold text-ink">
                {job.title}
              </h2>
              <div className="mt-3 flex flex-wrap gap-2 text-xs">
                <span className="rounded-md bg-sand px-2.5 py-1 text-ink/80">{job.location}</span>
                <span className="rounded-md bg-sand px-2.5 py-1 text-ink/80">{job.employmentType}</span>
                {job.salaryRange && (
                  <span className="rounded-md bg-sand px-2.5 py-1 font-medium text-sea-deep">
                    {job.salaryRange}
                  </span>
                )}
                {job.visaRequirements && (
                  <span className="rounded-md bg-accent/15 px-2.5 py-1 font-medium text-accent">
                    Visa: {job.visaRequirements}
                  </span>
                )}
              </div>
              <p className="mt-4 whitespace-pre-wrap text-sm leading-relaxed text-ink/75">{job.description}</p>
              {job.contactEmail && (
                <div className="mt-4 border-t border-sand-deep pt-3 text-xs text-ink/60">
                  Contact:{" "}
                  <a href={`mailto:${job.contactEmail}`} className="font-semibold text-sea-deep underline">
                    {job.contactEmail}
                  </a>
                </div>
              )}
            </li>
          ))}
        </ul>
      </section>
    </main>
  );
}
