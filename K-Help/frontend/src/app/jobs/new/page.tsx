"use client";

import Link from "next/link";
import { FormEvent, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Navbar } from "@/components/Navbar";
import { createJob, getToken } from "@/lib/api";

export default function NewJobPage() {
  const router = useRouter();
  const [title, setTitle] = useState("");
  const [companyName, setCompanyName] = useState("");
  const [location, setLocation] = useState("");
  const [visaRequirements, setVisaRequirements] = useState("");
  const [employmentType, setEmploymentType] = useState("FULL_TIME");
  const [salaryRange, setSalaryRange] = useState("");
  const [description, setDescription] = useState("");
  const [contactEmail, setContactEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!getToken()) {
      router.replace("/login");
    }
  }, [router]);

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await createJob({
        title,
        companyName,
        location,
        visaRequirements: visaRequirements || undefined,
        employmentType,
        salaryRange: salaryRange || undefined,
        description,
        contactEmail: contactEmail || undefined,
      });
      router.push("/jobs");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to publish job");
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-2xl px-6 py-14">
        <Link href="/jobs" className="text-sm font-medium text-ink/60 transition hover:text-sea-deep">
          ← Back to jobs
        </Link>

        <h1 className="mt-4 font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
          Post a Job Opening
        </h1>
        <p className="mt-2 text-ink/70">
          Reach skilled international talent residing in South Korea.
        </p>

        <form onSubmit={onSubmit} className="mt-8 space-y-4">
          <label className="block text-sm font-medium text-ink">
            Job Title
            <input
              required
              minLength={3}
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="e.g. Bilingual Customer Success Specialist"
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>

          <div className="grid gap-4 sm:grid-cols-2">
            <label className="block text-sm font-medium text-ink">
              Company Name
              <input
                required
                value={companyName}
                onChange={(e) => setCompanyName(e.target.value)}
                placeholder="Acme Corp Korea"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>

            <label className="block text-sm font-medium text-ink">
              Location
              <input
                required
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                placeholder="Seoul, Gangnam-gu"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>
          </div>

          <div className="grid gap-4 sm:grid-cols-2">
            <label className="block text-sm font-medium text-ink">
              Visa Eligibility / Requirements
              <input
                value={visaRequirements}
                onChange={(e) => setVisaRequirements(e.target.value)}
                placeholder="F-2, F-4, F-5, F-6 or E-7 sponsorship available"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>

            <label className="block text-sm font-medium text-ink">
              Employment Type
              <select
                value={employmentType}
                onChange={(e) => setEmploymentType(e.target.value)}
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              >
                <option value="FULL_TIME">Full-time</option>
                <option value="PART_TIME">Part-time</option>
                <option value="CONTRACT">Contract</option>
                <option value="INTERNSHIP">Internship</option>
              </select>
            </label>
          </div>

          <div className="grid gap-4 sm:grid-cols-2">
            <label className="block text-sm font-medium text-ink">
              Salary / Compensation
              <input
                value={salaryRange}
                onChange={(e) => setSalaryRange(e.target.value)}
                placeholder="e.g. 35M - 45M KRW / year"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>

            <label className="block text-sm font-medium text-ink">
              Contact Email
              <input
                type="email"
                value={contactEmail}
                onChange={(e) => setContactEmail(e.target.value)}
                placeholder="hiring@company.kr"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>
          </div>

          <label className="block text-sm font-medium text-ink">
            Description & Qualifications
            <textarea
              required
              minLength={10}
              rows={8}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Roles, requirements, language proficiency needed..."
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>

          {error ? <p className="text-sm text-accent">{error}</p> : null}

          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-md bg-sea-deep px-4 py-2.5 text-sm font-semibold text-sand transition hover:bg-ink disabled:opacity-60"
          >
            {loading ? "Submitting..." : "Post Job Opening"}
          </button>
        </form>
      </section>
    </main>
  );
}
