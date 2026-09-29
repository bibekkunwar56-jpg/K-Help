import Link from "next/link";
import { Navbar } from "@/components/Navbar";
import { fetchHello } from "@/lib/api";
import {
  Globe2,
  Home,
  Briefcase,
  Users,
  BookOpen,
  Bot,
  MessageSquare,
  ShieldCheck,
  Sparkles,
  ArrowRight,
  PhoneCall,
  CheckCircle2,
} from "lucide-react";

export default async function HomePage() {
  let backendMessage = "Backend is not connected yet.";
  let connected = false;

  try {
    backendMessage = await fetchHello();
    connected = true;
  } catch {
    backendMessage = "Backend is not connected yet. Start Docker + Spring Boot.";
  }

  const features = [
    {
      icon: Users,
      title: "Foreign Resident Community",
      desc: "Connect with students, expats & workers across Korea. Ask visa questions, share experiences, and get genuine advice.",
      href: "/community",
      tag: "Active Forum",
      badge: "D-2 / E-7 / F-Visa Friendly",
    },
    {
      icon: BookOpen,
      title: "Korea Living Guides",
      desc: "Step-by-step guides for ARC registration, National Health Insurance (NHIS), banking, and cheap phone plans.",
      href: "/guides",
      tag: "8 Essential Guides",
      badge: "Official 1345 Info",
    },
    {
      icon: Briefcase,
      title: "Foreigner-Friendly Jobs",
      desc: "Job openings with explicit visa sponsorship (E-7, F-series, D-10 internships, part-time student work).",
      href: "/jobs",
      tag: "Verified Employers",
      badge: "Visa Tags Included",
    },
    {
      icon: Home,
      title: "Transparent Housing",
      desc: "One-rooms, officetels, and studios with clear wolse rent & deposit terms without foreign resident discrimination.",
      href: "/housing",
      tag: "Direct & Realtor",
      badge: "Clear Deposit Terms",
    },
    {
      icon: Bot,
      title: "AI Honorifics & Translator",
      desc: "Translate across 12+ languages and automatically polish messages into polite Korean honorifics (존댓말) for landlords & bosses.",
      href: "/ai",
      tag: "12+ Languages",
      badge: "존댓말 Polisher",
    },
    {
      icon: MessageSquare,
      title: "Live 1:1 Direct Chat",
      desc: "Direct real-time messaging with room listers, prospective employers, and peers with instant WebSocket updates.",
      href: "/chat",
      tag: "Real-time /ws",
      badge: "Instant Messaging",
    },
  ];

  const quickHotlines = [
    { name: "Immigration Contact Center", number: "1345", desc: "Multilingual visa & stay inquiries" },
    { name: "Korea Life Helpline (Dasan)", number: "120", desc: "Seoul municipal & civil guidance" },
    { name: "Emergency Police / Fire", number: "112 / 119", desc: "Immediate 24/7 emergency dispatch" },
    { name: "Health Insurance (NHIS)", number: "1577-1000", desc: "Foreigner insurance service desk" },
  ];

  return (
    <main className="min-h-screen bg-[radial-gradient(circle_at_top_left,_#dff0f2,_transparent_45%),radial-gradient(circle_at_bottom_right,_#f6e7d8,_transparent_40%),var(--mist)] text-ink">
      <Navbar />

      {/* HERO SECTION */}
      <section className="mx-auto max-w-6xl px-6 pt-16 pb-20">
        <div className="grid gap-12 lg:grid-cols-[1.3fr_0.7fr] lg:items-center">
          <div>
            {/* Foreigner Callout Badge */}
            <div className="inline-flex items-center gap-2 rounded-full border border-sea/30 bg-sea/10 px-4 py-1.5 text-xs font-semibold uppercase tracking-[0.16em] text-sea-deep">
              <Globe2 className="h-4 w-4 text-sea" />
              <span>Dedicated Platform for All Foreigners Living in Korea</span>
            </div>

            <h1 className="mt-6 font-[family-name:var(--font-display)] text-5xl leading-[1.08] font-bold text-ink sm:text-6xl">
              Live, work, and thrive in South Korea with <span className="text-sea">K-Help</span>.
            </h1>

            <p className="mt-6 max-w-xl text-lg leading-relaxed text-ink/80">
              The all-in-one verified platform designed specifically for international residents,
              students, and workers in Korea. Find rooms without language barriers, browse foreigner-eligible jobs,
              get official visa guidance, and craft respectful Korean messages effortlessly.
            </p>

            {/* Prominent Action Bar */}
            <div className="mt-8 flex flex-col sm:flex-row sm:items-center gap-4">
              <Link
                href="/register"
                className="inline-flex items-center justify-center gap-2 rounded-xl bg-accent px-7 py-3.5 text-base font-bold text-sand shadow-lg transition hover:bg-sea-deep hover:shadow-xl"
              >
                <span>Create Foreigner Account</span>
                <ArrowRight className="h-4 w-4" />
              </Link>
              <Link
                href="/login"
                className="inline-flex items-center justify-center rounded-xl border-2 border-sea-deep/30 bg-white/70 px-6 py-3.5 text-base font-semibold text-ink backdrop-blur-sm transition hover:border-sea hover:bg-white"
              >
                Sign In to K-Help
              </Link>
            </div>

            {/* Quick Benefits / Trust Badges */}
            <div className="mt-8 flex flex-wrap items-center gap-6 text-xs text-ink/75">
              <span className="flex items-center gap-1.5 font-medium">
                <CheckCircle2 className="h-4 w-4 text-sea" /> Free for International Students & Expats
              </span>
              <span className="flex items-center gap-1.5 font-medium">
                <CheckCircle2 className="h-4 w-4 text-sea" /> 100% English & Multilingual Friendly
              </span>
              <span className="flex items-center gap-1.5 font-medium">
                <CheckCircle2 className="h-4 w-4 text-sea" /> Transparent & No Hidden Fees
              </span>
            </div>
          </div>

          {/* System Check & Status Card */}
          <div className="space-y-6">
            <aside className="rounded-2xl border border-sand-deep bg-white/80 p-6 shadow-[0_20px_50px_rgba(11,31,42,0.06)] backdrop-blur-md">
              <div className="flex items-center justify-between">
                <p className="text-xs font-bold uppercase tracking-[0.16em] text-sea">
                  Live System Health
                </p>
                <span
                  className={`inline-flex items-center gap-1.5 rounded-full px-2.5 py-0.5 text-xs font-semibold ${
                    connected
                      ? "bg-sea/15 text-sea-deep"
                      : "bg-accent/15 text-accent"
                  }`}
                >
                  <span
                    className={`h-2 w-2 rounded-full ${
                      connected ? "bg-sea animate-pulse" : "bg-accent"
                    }`}
                  />
                  {connected ? "Online" : "Connecting"}
                </span>
              </div>
              <p className="mt-3 text-sm font-semibold text-ink">
                {connected ? "K-Help API Core Connected" : "Local Services Syncing"}
              </p>
              <p className="mt-1 text-xs text-ink/70 leading-relaxed">{backendMessage}</p>

              <div className="mt-5 border-t border-sand-deep pt-4">
                <p className="text-xs font-semibold uppercase tracking-wider text-ink/60">
                  Supported Visa Categories
                </p>
                <div className="mt-2 flex flex-wrap gap-1.5">
                  {["D-2 Student", "D-10 Job Seeker", "E-7 Professional", "E-9 Worker", "F-2 Resident", "F-4 Overseas", "F-6 Marriage"].map(
                    (visa) => (
                      <span
                        key={visa}
                        className="rounded-md border border-sand-deep/80 bg-sand/60 px-2 py-0.5 text-[11px] font-medium text-ink/80"
                      >
                        {visa}
                      </span>
                    )
                  )}
                </div>
              </div>
            </aside>

            {/* Community Highlight Card */}
            <div className="rounded-2xl border border-sea/20 bg-gradient-to-br from-sea/5 to-accent/5 p-5">
              <div className="flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-sea-deep">
                <Sparkles className="h-4 w-4 text-accent" />
                <span>AI-Powered Communication</span>
              </div>
              <p className="mt-2 text-sm text-ink/85 leading-snug">
                Worried about messaging a landlord or company? Type in English, and K-Help translates it into proper Korean honorifics (존댓말) instantly.
              </p>
              <Link
                href="/ai"
                className="mt-3 inline-flex items-center gap-1 text-xs font-bold text-sea-deep hover:underline"
              >
                Try AI Assistant →
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* CORE PLATFORM FEATURES */}
      <section className="border-t border-sand-deep/80 bg-white/40 py-16 backdrop-blur-sm">
        <div className="mx-auto max-w-6xl px-6">
          <div className="text-center max-w-2xl mx-auto">
            <p className="text-xs font-bold uppercase tracking-[0.2em] text-sea">Built For Your Life In Korea</p>
            <h2 className="mt-2 font-[family-name:var(--font-display)] text-3xl sm:text-4xl font-semibold text-ink">
              Everything foreign residents need in one single place
            </h2>
            <p className="mt-3 text-sm text-ink/70">
              Stop switching between dozens of untranslated websites. K-Help brings together verified solutions for every aspect of Korean life.
            </p>
          </div>

          <div className="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {features.map((item) => {
              const Icon = item.icon;
              return (
                <Link
                  key={item.title}
                  href={item.href}
                  className="group flex flex-col justify-between rounded-2xl border border-sand-deep bg-white/80 p-6 shadow-sm transition hover:-translate-y-1 hover:border-sea hover:shadow-md"
                >
                  <div>
                    <div className="flex items-center justify-between">
                      <div className="rounded-xl bg-sea/10 p-2.5 text-sea-deep group-hover:bg-sea group-hover:text-sand transition">
                        <Icon className="h-5 w-5" />
                      </div>
                      <span className="rounded-full bg-sand px-2.5 py-0.5 text-[11px] font-semibold text-sea-deep">
                        {item.tag}
                      </span>
                    </div>

                    <h3 className="mt-4 font-[family-name:var(--font-display)] text-xl font-semibold text-ink group-hover:text-sea-deep transition">
                      {item.title}
                    </h3>

                    <p className="mt-2 text-sm text-ink/75 leading-relaxed">
                      {item.desc}
                    </p>
                  </div>

                  <div className="mt-6 flex items-center justify-between border-t border-sand-deep pt-4">
                    <span className="text-xs font-medium text-accent">
                      {item.badge}
                    </span>
                    <span className="text-xs font-bold text-sea-deep group-hover:translate-x-1 transition">
                      Explore →
                    </span>
                  </div>
                </Link>
              );
            })}
          </div>
        </div>
      </section>

      {/* QUICK HELPLINES FOR FOREIGNERS */}
      <section className="mx-auto max-w-6xl px-6 py-16">
        <div className="rounded-3xl border border-sand-deep bg-sand/40 p-8 sm:p-10">
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div>
              <div className="inline-flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-sea">
                <PhoneCall className="h-4 w-4" />
                <span>Emergency & Foreign Resident Hotlines</span>
              </div>
              <h3 className="mt-2 font-[family-name:var(--font-display)] text-2xl sm:text-3xl font-semibold text-ink">
                Essential Korean Government Phone Lines
              </h3>
              <p className="mt-2 max-w-xl text-sm text-ink/75">
                Save these official numbers for your stay. Translation and English assistance are provided free of charge by ministries.
              </p>
            </div>
            <Link
              href="/guides/immigration-contact-center-1345"
              className="inline-flex items-center gap-2 rounded-xl bg-ink px-5 py-3 text-xs font-semibold text-sand transition hover:bg-sea-deep whitespace-nowrap self-start md:self-auto"
            >
              <span>View Guide on 1345 Hotline</span>
              <ArrowRight className="h-4 w-4" />
            </Link>
          </div>

          <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {quickHotlines.map((h) => (
              <div key={h.name} className="rounded-2xl border border-sand-deep bg-white/90 p-5">
                <span className="text-2xl font-black tracking-tight text-sea-deep">
                  {h.number}
                </span>
                <p className="mt-1 font-semibold text-sm text-ink">{h.name}</p>
                <p className="mt-1 text-xs text-ink/65">{h.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* BOTTOM CTA BAR */}
      <section className="border-t border-sand-deep bg-white/60 py-16">
        <div className="mx-auto max-w-4xl px-6 text-center">
          <div className="mx-auto w-12 h-12 rounded-2xl bg-sea/10 flex items-center justify-center text-sea-deep mb-4">
            <ShieldCheck className="h-6 w-6" />
          </div>
          <h2 className="font-[family-name:var(--font-display)] text-3xl sm:text-4xl font-bold text-ink">
            Join the K-Help Community Today
          </h2>
          <p className="mt-3 text-base text-ink/75 max-w-xl mx-auto">
            Whether you just landed at Incheon Airport or have lived in Korea for years, K-Help empowers your journey with transparent resources.
          </p>

          <div className="mt-8 flex flex-wrap justify-center gap-4">
            <Link
              href="/register"
              className="rounded-xl bg-sea-deep px-8 py-3.5 text-sm font-bold text-sand shadow-md transition hover:bg-ink"
            >
              Sign Up as a Foreign Resident
            </Link>
            <Link
              href="/community"
              className="rounded-xl border border-ink/20 bg-white px-8 py-3.5 text-sm font-bold text-ink transition hover:border-sea"
            >
              Browse Community Forum
            </Link>
          </div>
        </div>
      </section>
    </main>
  );
}
