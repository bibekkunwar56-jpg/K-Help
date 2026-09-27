import Link from "next/link";
import { Navbar } from "@/components/Navbar";
import { fetchHello } from "@/lib/api";

export default async function HomePage() {
  let backendMessage = "Backend is not connected yet.";
  let connected = false;

  try {
    backendMessage = await fetchHello();
    connected = true;
  } catch {
    backendMessage = "Backend is not connected yet. Start Docker + Spring Boot.";
  }

  return (
    <main className="min-h-screen bg-[radial-gradient(circle_at_top_left,_#dff0f2,_transparent_40%),radial-gradient(circle_at_bottom_right,_#f6e7d8,_transparent_35%),var(--mist)]">
      <Navbar />

      <section className="mx-auto grid max-w-5xl gap-10 px-6 py-20 lg:grid-cols-[1.2fr_0.8fr] lg:items-end">
        <div>
          <p className="mb-4 font-[family-name:var(--font-display)] text-sm uppercase tracking-[0.22em] text-sea">
            For foreign residents in Korea
          </p>
          <h1 className="font-[family-name:var(--font-display)] text-5xl leading-[1.05] font-semibold text-ink sm:text-6xl">
            K-Help
          </h1>
          <p className="mt-6 max-w-xl text-lg leading-relaxed text-ink/75">
            One place for community discussions, foreigner-friendly jobs, housing
            searches, living guides, and AI-assisted translation.
          </p>
          <div className="mt-8 flex flex-wrap gap-3">
            <Link
              href="/register"
              className="rounded-md bg-sea-deep px-5 py-3 text-sm font-semibold text-sand transition hover:bg-ink"
            >
              Create account
            </Link>
            <Link
              href="/login"
              className="rounded-md border border-ink/15 bg-white/50 px-5 py-3 text-sm font-semibold text-ink transition hover:border-sea"
            >
              Sign in
            </Link>
          </div>
        </div>

        <aside className="rounded-2xl border border-sand-deep bg-white/70 p-6 shadow-[0_20px_60px_rgba(11,31,42,0.08)]">
          <p className="text-xs font-semibold uppercase tracking-[0.16em] text-sea">
            System check
          </p>
          <p
            className={`mt-3 text-sm font-medium ${
              connected ? "text-sea-deep" : "text-accent"
            }`}
          >
            {connected ? "Backend connected" : "Backend offline"}
          </p>
          <p className="mt-2 text-sm leading-relaxed text-ink/70">{backendMessage}</p>
        </aside>
      </section>
    </main>
  );
}
