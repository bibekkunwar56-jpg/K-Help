"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { MapPinned } from "lucide-react";
import { clearToken, getToken } from "@/lib/api";
import { useEffect, useState } from "react";

const links = [
  { href: "/", label: "Home" },
  { href: "/community", label: "Community" },
  { href: "/guides", label: "Guides" },
  { href: "/jobs", label: "Jobs" },
  { href: "/housing", label: "Housing" },
  { href: "/chat", label: "Chat" },
  { href: "/ai", label: "AI Tools" },
  { href: "/login", label: "Sign in" },
  { href: "/register", label: "Join" },
  { href: "/profile", label: "Profile" },
];

export function Navbar() {
  const pathname = usePathname();
  const [signedIn, setSignedIn] = useState(false);

  useEffect(() => {
    setSignedIn(Boolean(getToken()));
  }, [pathname]);

  function handleSignOut() {
    clearToken();
    setSignedIn(false);
    window.location.href = "/";
  }

  return (
    <header className="border-b border-sand-deep/80 bg-sand/80 backdrop-blur">
      <div className="mx-auto flex max-w-5xl items-center justify-between px-6 py-4">
        <Link href="/" className="group flex items-start gap-2">
          <MapPinned className="mt-1 h-5 w-5 text-sea" aria-hidden />
          <span>
            <span className="block font-[family-name:var(--font-display)] text-2xl font-semibold tracking-tight text-ink">
              K-Help
            </span>
            <span className="mt-0.5 block text-xs font-medium uppercase tracking-[0.18em] text-sea">
              Korea Life Platform
            </span>
          </span>
        </Link>

        <nav className="flex items-center gap-4 text-sm font-medium text-ink/70">
          {links
            .filter((link) => {
              if (signedIn && (link.href === "/login" || link.href === "/register")) return false;
              if (!signedIn && link.href === "/profile") return false;
              return true;
            })
            .map((link) => (
              <Link
                key={link.href}
                href={link.href}
                className={
                  pathname === link.href
                    ? "text-sea-deep"
                    : "transition hover:text-sea-deep"
                }
              >
                {link.label}
              </Link>
            ))}
          {signedIn ? (
            <button
              type="button"
              onClick={handleSignOut}
              className="rounded-md bg-ink px-3 py-1.5 text-sand transition hover:bg-sea-deep"
            >
              Sign out
            </button>
          ) : (
            <Link
              href="/register"
              className="rounded-md bg-accent px-3 py-1.5 text-sand transition hover:bg-sea-deep"
            >
              Get started
            </Link>
          )}
        </nav>
      </div>
    </header>
  );
}
