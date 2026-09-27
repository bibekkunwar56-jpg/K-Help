"use client";

import Link from "next/link";
import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { Navbar } from "@/components/Navbar";
import { loginUser, setToken } from "@/lib/api";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const result = await loginUser({ email, password });
      setToken(result.token);
      router.push("/profile");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Login failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />
      <section className="mx-auto max-w-md px-6 py-16">
        <h1 className="font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
          Sign in
        </h1>
        <p className="mt-2 text-ink/70">
          Welcome back. Access your K-Help profile and services.
        </p>

        <form onSubmit={onSubmit} className="mt-8 space-y-4">
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
            Password
            <input
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>
          {error ? <p className="text-sm text-accent">{error}</p> : null}
          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-md bg-sea-deep px-4 py-2.5 text-sm font-semibold text-sand disabled:opacity-60"
          >
            {loading ? "Signing in..." : "Sign in"}
          </button>
        </form>

        <p className="mt-6 text-sm text-ink/70">
          New here?{" "}
          <Link href="/register" className="font-semibold text-sea-deep">
            Create an account
          </Link>
        </p>
      </section>
    </main>
  );
}
