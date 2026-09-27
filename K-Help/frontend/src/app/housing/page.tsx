"use client";

import Link from "next/link";
import { FormEvent, useEffect, useState } from "react";
import { Navbar } from "@/components/Navbar";
import { fetchHouses } from "@/lib/api";
import { formatDate } from "@/lib/format";
import type { House } from "@/types/listings";

export default function HousingPage() {
  const [houses, setHouses] = useState<House[]>([]);
  const [location, setLocation] = useState("");
  const [maxRent, setMaxRent] = useState<string>("");
  const [maxDeposit, setMaxDeposit] = useState<string>("");
  const [loading, setLoading] = useState(true);

  const loadHouses = () => {
    setLoading(true);
    fetchHouses({
      location: location || undefined,
      maxRent: maxRent ? parseInt(maxRent, 10) : undefined,
      maxDeposit: maxDeposit ? parseInt(maxDeposit, 10) : undefined,
    })
      .then(setHouses)
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadHouses();
  }, []);

  const onFilter = (e: FormEvent) => {
    e.preventDefault();
    loadHouses();
  };

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-5xl px-6 py-14">
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.18em] text-sea">Living</p>
            <h1 className="mt-2 font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
              Foreigner-friendly Housing in Korea
            </h1>
            <p className="mt-2 max-w-xl text-ink/70">
              Direct and realtor listings without discriminatory barriers, clear deposit & monthly rent terms.
            </p>
          </div>
          <Link
            href="/housing/new"
            className="rounded-md bg-sea-deep px-5 py-2.5 text-sm font-semibold text-sand transition hover:bg-ink"
          >
            List a Place
          </Link>
        </div>

        <form onSubmit={onFilter} className="mt-8 flex flex-wrap gap-3">
          <input
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            placeholder="Location (e.g. Mapo-gu, Sinchon, Itaewon)..."
            className="min-w-[200px] flex-1 rounded-md border border-sand-deep bg-white px-3 py-2 text-sm outline-none focus:border-sea"
          />
          <input
            type="number"
            value={maxDeposit}
            onChange={(e) => setMaxDeposit(e.target.value)}
            placeholder="Max Deposit (KRW)"
            className="w-40 rounded-md border border-sand-deep bg-white px-3 py-2 text-sm outline-none focus:border-sea"
          />
          <input
            type="number"
            value={maxRent}
            onChange={(e) => setMaxRent(e.target.value)}
            placeholder="Max Rent (KRW)"
            className="w-40 rounded-md border border-sand-deep bg-white px-3 py-2 text-sm outline-none focus:border-sea"
          />
          <button
            type="submit"
            className="rounded-md border border-sand-deep bg-white/70 px-5 py-2 text-sm font-semibold text-ink transition hover:border-sea"
          >
            Filter
          </button>
        </form>

        {loading ? <p className="mt-8 text-sm text-ink/60">Loading housing listings...</p> : null}

        {!loading && houses.length === 0 ? (
          <div className="mt-8 rounded-2xl border border-sand-deep bg-white/70 p-8 text-center">
            <p className="text-ink/70">No housing listings found matching criteria.</p>
            <Link href="/housing/new" className="mt-3 inline-block text-sm font-semibold text-sea-deep">
              Post the first listing
            </Link>
          </div>
        ) : null}

        <ul className="mt-8 grid gap-6 sm:grid-cols-2">
          {houses.map((house) => (
            <li
              key={house.id}
              className="flex flex-col justify-between rounded-2xl border border-sand-deep bg-white/70 p-6 transition hover:border-sea hover:shadow-[0_18px_40px_rgba(11,31,42,0.08)]"
            >
              <div>
                <div className="flex items-center justify-between">
                  <span className="rounded-full bg-sand px-3 py-1 text-xs font-semibold uppercase tracking-[0.12em] text-sea-deep">
                    {house.housingType.replace(/_/g, " ")}
                  </span>
                  <span className="text-xs text-ink/50">{formatDate(house.createdAt)}</span>
                </div>
                <h2 className="mt-3 font-[family-name:var(--font-display)] text-2xl font-semibold text-ink">
                  {house.title}
                </h2>
                <p className="mt-1 text-xs font-medium text-ink/60">{house.location}</p>

                <div className="mt-4 flex flex-wrap gap-2 text-sm font-semibold text-sea-deep">
                  <span>Deposit: ₩{Number(house.depositKrw).toLocaleString()}</span>
                  <span>·</span>
                  <span>Rent: ₩{Number(house.monthlyRentKrw).toLocaleString()}/mo</span>
                </div>

                {house.maintenanceFeeKrw ? (
                  <p className="mt-1 text-xs text-ink/60">
                    Maintenance fee: ₩{Number(house.maintenanceFeeKrw).toLocaleString()}/mo
                  </p>
                ) : null}

                <p className="mt-4 whitespace-pre-wrap text-sm leading-relaxed text-ink/75 line-clamp-3">
                  {house.description}
                </p>
              </div>

              {house.contactPhone && (
                <div className="mt-6 border-t border-sand-deep pt-3 text-xs text-ink/70">
                  Landlord / Agent:{" "}
                  <span className="font-semibold text-ink">{house.contactPhone}</span>
                </div>
              )}
            </li>
          ))}
        </ul>
      </section>
    </main>
  );
}
