"use client";

import Link from "next/link";
import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { Navbar } from "@/components/Navbar";
import { registerUser, setToken } from "@/lib/api";

export default function RegisterPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [nickname, setNickname] = useState("");
  const [visaType, setVisaType] = useState("");
  const [nationality, setNationality] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const result = await registerUser({
        email,
        password,
        nickname,
        visaType: visaType || undefined,
        nationality: nationality || undefined,
      });
      setToken(result.token);
      router.push("/profile");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Registration failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />
      <section className="mx-auto max-w-md px-6 py-16">
        <h1 className="font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
          Join K-Help
        </h1>
        <p className="mt-2 text-ink/70">
          Create your account to access community, jobs, housing, and guides.
        </p>

        <form onSubmit={onSubmit} className="mt-8 space-y-4">
          <label className="block text-sm font-medium text-ink">
            Nickname
            <input
              required
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>
          <label className="block text-sm font-medium text-ink">
            Email
            <input
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>
          <label className="block text-sm font-medium text-ink">
            Password (min 8 characters)
            <input
              type="password"
              required
              minLength={8}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>
          <label className="block text-sm font-medium text-ink">
            Visa type (optional)
            <input
              value={visaType}
              onChange={(e) => setVisaType(e.target.value)}
              placeholder="D-2, E-7, F-2..."
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>
          <label className="block text-sm font-medium text-ink">
            Nationality (optional)
            <input
              value={nationality}
              onChange={(e) => setNationality(e.target.value)}
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>
          {error ? <p className="text-sm text-accent">{error}</p> : null}
          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-md bg-accent px-4 py-2.5 text-sm font-semibold text-sand disabled:opacity-60"
          >
            {loading ? "Creating account..." : "Create account"}
          </button>
        </form>

        <p className="mt-6 text-sm text-ink/70">
          Already have an account?{" "}
          <Link href="/login" className="font-semibold text-sea-deep">
            Sign in
          </Link>
        </p>
      </section>
    </main>
  );
}
