"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Navbar } from "@/components/Navbar";
import { clearToken, fetchMe, getToken } from "@/lib/api";
import type { User } from "@/types/user";

export default function ProfilePage() {
  const router = useRouter();
  const [user, setUser] = useState<User | null>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!getToken()) {
      router.replace("/login");
      return;
    }

    fetchMe()
      .then(setUser)
      .catch((err) => {
        clearToken();
        setError(err instanceof Error ? err.message : "Could not load profile");
        router.replace("/login");
      });
  }, [router]);

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />
      <section className="mx-auto max-w-2xl px-6 py-16">
        <h1 className="font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
          Your profile
        </h1>
        <p className="mt-2 text-ink/70">Signed-in account details from `/api/users/me`.</p>

        {error ? <p className="mt-6 text-sm text-accent">{error}</p> : null}

        {!user && !error ? (
          <p className="mt-8 text-sm text-ink/60">Loading profile...</p>
        ) : null}

        {user ? (
          <dl className="mt-8 space-y-4 rounded-2xl border border-sand-deep bg-white/80 p-6">
            <div>
              <dt className="text-xs uppercase tracking-[0.14em] text-sea">Nickname</dt>
              <dd className="mt-1 text-lg font-semibold text-ink">{user.nickname}</dd>
            </div>
            <div>
              <dt className="text-xs uppercase tracking-[0.14em] text-sea">Email</dt>
              <dd className="mt-1 text-ink">{user.email}</dd>
            </div>
            <div>
              <dt className="text-xs uppercase tracking-[0.14em] text-sea">Visa</dt>
              <dd className="mt-1 text-ink">{user.visaType || "Not set"}</dd>
            </div>
            <div>
              <dt className="text-xs uppercase tracking-[0.14em] text-sea">Nationality</dt>
              <dd className="mt-1 text-ink">{user.nationality || "Not set"}</dd>
            </div>
            <div>
              <dt className="text-xs uppercase tracking-[0.14em] text-sea">Role</dt>
              <dd className="mt-1 text-ink">{user.role}</dd>
            </div>
          </dl>
        ) : null}
      </section>
    </main>
  );
}
