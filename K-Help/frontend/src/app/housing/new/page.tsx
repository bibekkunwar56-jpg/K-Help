"use client";

import Link from "next/link";
import { FormEvent, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Navbar } from "@/components/Navbar";
import { createHouse, getToken } from "@/lib/api";

export default function NewHousePage() {
  const router = useRouter();
  const [title, setTitle] = useState("");
  const [housingType, setHousingType] = useState("ONE_ROOM");
  const [location, setLocation] = useState("");
  const [depositKrw, setDepositKrw] = useState("");
  const [monthlyRentKrw, setMonthlyRentKrw] = useState("");
  const [maintenanceFeeKrw, setMaintenanceFeeKrw] = useState("");
  const [floorLevel, setFloorLevel] = useState("");
  const [description, setDescription] = useState("");
  const [contactPhone, setContactPhone] = useState("");
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
      await createHouse({
        title,
        housingType,
        location,
        depositKrw: parseInt(depositKrw, 10),
        monthlyRentKrw: parseInt(monthlyRentKrw, 10),
        maintenanceFeeKrw: maintenanceFeeKrw ? parseInt(maintenanceFeeKrw, 10) : 0,
        floorLevel: floorLevel || undefined,
        description,
        contactPhone: contactPhone || undefined,
      });
      router.push("/housing");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to publish housing listing");
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-2xl px-6 py-14">
        <Link href="/housing" className="text-sm font-medium text-ink/60 transition hover:text-sea-deep">
          ← Back to housing
        </Link>

        <h1 className="mt-4 font-[family-name:var(--font-display)] text-4xl font-semibold text-ink">
          List a Room or Apartment
        </h1>
        <p className="mt-2 text-ink/70">
          Direct rent, sublets, or foreigner-friendly studio postings.
        </p>

        <form onSubmit={onSubmit} className="mt-8 space-y-4">
          <label className="block text-sm font-medium text-ink">
            Listing Title
            <input
              required
              minLength={3}
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="e.g. Sunny One-room near Sinchon Station exit 4"
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>

          <div className="grid gap-4 sm:grid-cols-2">
            <label className="block text-sm font-medium text-ink">
              Housing Type
              <select
                value={housingType}
                onChange={(e) => setHousingType(e.target.value)}
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              >
                <option value="ONE_ROOM">One-room (Studio)</option>
                <option value="TWO_ROOM">Two-room</option>
                <option value="OFFICETEL">Officetel</option>
                <option value="APARTMENT">Apartment</option>
                <option value="SHAREHOUSE">Sharehouse / Gosiwon</option>
              </select>
            </label>

            <label className="block text-sm font-medium text-ink">
              Location / Neighborhood
              <input
                required
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                placeholder="Seoul, Mapo-gu, Yeonnam-dong"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>
          </div>

          <div className="grid gap-4 sm:grid-cols-3">
            <label className="block text-sm font-medium text-ink">
              Deposit (KRW)
              <input
                required
                type="number"
                min="0"
                value={depositKrw}
                onChange={(e) => setDepositKrw(e.target.value)}
                placeholder="5000000"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>

            <label className="block text-sm font-medium text-ink">
              Monthly Rent (KRW)
              <input
                required
                type="number"
                min="0"
                value={monthlyRentKrw}
                onChange={(e) => setMonthlyRentKrw(e.target.value)}
                placeholder="550000"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>

            <label className="block text-sm font-medium text-ink">
              Maintenance Fee (KRW)
              <input
                type="number"
                min="0"
                value={maintenanceFeeKrw}
                onChange={(e) => setMaintenanceFeeKrw(e.target.value)}
                placeholder="70000"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>
          </div>

          <div className="grid gap-4 sm:grid-cols-2">
            <label className="block text-sm font-medium text-ink">
              Floor Level (optional)
              <input
                value={floorLevel}
                onChange={(e) => setFloorLevel(e.target.value)}
                placeholder="3rd floor / Semi-basement"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>

            <label className="block text-sm font-medium text-ink">
              Contact Phone / KakaoTalk ID
              <input
                value={contactPhone}
                onChange={(e) => setContactPhone(e.target.value)}
                placeholder="010-1234-5678"
                className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
              />
            </label>
          </div>

          <label className="block text-sm font-medium text-ink">
            Description & Terms
            <textarea
              required
              minLength={10}
              rows={8}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Furnishing details, utility inclusions, lease duration..."
              className="mt-1 w-full rounded-md border border-sand-deep bg-white px-3 py-2 outline-none focus:border-sea"
            />
          </label>

          {error ? <p className="text-sm text-accent">{error}</p> : null}

          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-md bg-sea-deep px-4 py-2.5 text-sm font-semibold text-sand transition hover:bg-ink disabled:opacity-60"
          >
            {loading ? "Listing..." : "Post Housing Listing"}
          </button>
        </form>
      </section>
    </main>
  );
}
