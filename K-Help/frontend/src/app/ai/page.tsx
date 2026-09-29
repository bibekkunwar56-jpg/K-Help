"use client";

import { useState } from "react";
import { Navbar } from "@/components/Navbar";
import { aiMakePolite, aiSummarize, aiTranslate } from "@/lib/api";

export default function AiToolsPage() {
  const [activeTab, setActiveTab] = useState<"translate" | "polite" | "summarize">("polite");

  // State for Polite Generator
  const [politeInput, setPoliteInput] = useState("");
  const [formality, setFormality] = useState("HONORIFIC");
  const [politeResult, setPoliteResult] = useState("");

  // State for Translator
  const [translateInput, setTranslateInput] = useState("");
  const [targetLang, setTargetLang] = useState("ko");
  const [translateResult, setTranslateResult] = useState("");

  // State for Summarizer
  const [summarizeInput, setSummarizeInput] = useState("");
  const [summarizeResult, setSummarizeResult] = useState("");

  const [loading, setLoading] = useState(false);

  const handlePolite = async () => {
    if (!politeInput.trim()) return;
    setLoading(true);
    try {
      const res = await aiMakePolite(politeInput, formality);
      setPoliteResult(res.result);
    } finally {
      setLoading(false);
    }
  };

  const handleTranslate = async () => {
    if (!translateInput.trim()) return;
    setLoading(true);
    try {
      const res = await aiTranslate(translateInput, targetLang);
      setTranslateResult(res.result);
    } finally {
      setLoading(false);
    }
  };

  const handleSummarize = async () => {
    if (!summarizeInput.trim()) return;
    setLoading(true);
    try {
      const res = await aiSummarize(summarizeInput);
      setSummarizeResult(res.result);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen bg-mist">
      <Navbar />

      <section className="mx-auto max-w-4xl px-6 py-12">
        <p className="text-xs font-semibold uppercase tracking-[0.18em] text-sea">AI Services</p>
        <h1 className="mt-2 font-[family-name:var(--font-display)] text-3xl font-semibold text-ink">
          Korea Life AI Assistant
        </h1>
        <p className="mt-1 text-sm text-ink/70">
          Smart tools for foreign residents: polite Korean messaging, real-time translations, and contract summarization.
        </p>

        {/* Tabs */}
        <div className="mt-8 flex border-b border-sand-deep gap-2">
          <button
            onClick={() => setActiveTab("polite")}
            className={`px-4 py-2 text-sm font-semibold border-b-2 transition ${
              activeTab === "polite"
                ? "border-sea-deep text-sea-deep"
                : "border-transparent text-ink/60 hover:text-ink"
            }`}
          >
            Polite Message Polisher (존댓말)
          </button>
          <button
            onClick={() => setActiveTab("translate")}
            className={`px-4 py-2 text-sm font-semibold border-b-2 transition ${
              activeTab === "translate"
                ? "border-sea-deep text-sea-deep"
                : "border-transparent text-ink/60 hover:text-ink"
            }`}
          >
            Living Translator
          </button>
          <button
            onClick={() => setActiveTab("summarize")}
            className={`px-4 py-2 text-sm font-semibold border-b-2 transition ${
              activeTab === "summarize"
                ? "border-sea-deep text-sea-deep"
                : "border-transparent text-ink/60 hover:text-ink"
            }`}
          >
            Contract & Notice Summarizer
          </button>
        </div>

        {/* Tab 1: Polite Polisher */}
        {activeTab === "polite" && (
          <div className="mt-6 rounded-2xl border border-sand-deep bg-white p-6 shadow-sm space-y-4">
            <div>
              <label className="block text-sm font-semibold text-ink mb-1">
                Your message to Landlord, Boss, or Official
              </label>
              <textarea
                rows={4}
                value={politeInput}
                onChange={(e) => setPoliteInput(e.target.value)}
                placeholder="e.g. 방 계약 연장하고 싶은데 언제 만날 수 있어? / 혹시 보증금 영수증 줄 수 있어?"
                className="w-full rounded-md border border-sand-deep bg-mist/30 p-3 text-sm outline-none focus:border-sea"
              />
            </div>

            <div className="flex items-center gap-4">
              <label className="text-xs font-semibold text-ink">Level:</label>
              <select
                value={formality}
                onChange={(e) => setFormality(e.target.value)}
                className="rounded-md border border-sand-deep bg-white px-3 py-1.5 text-xs outline-none focus:border-sea"
              >
                <option value="HONORIFIC">Polite Conversational (존댓말 - 요체)</option>
                <option value="BUSINESS">Formal Business (격식체 - 습니다)</option>
              </select>
              <button
                onClick={handlePolite}
                disabled={loading || !politeInput.trim()}
                className="ml-auto rounded-md bg-sea-deep px-5 py-2 text-sm font-semibold text-sand transition hover:bg-ink disabled:opacity-50"
              >
                {loading ? "Polishing..." : "Convert to Polite Korean"}
              </button>
            </div>

            {politeResult && (
              <div className="mt-6 rounded-xl border border-sea/30 bg-sand/30 p-4">
                <span className="text-xs font-semibold text-sea-deep uppercase tracking-wider">Polished Korean Output</span>
                <p className="mt-2 text-base font-medium text-ink whitespace-pre-wrap">{politeResult}</p>
                <button
                  onClick={() => navigator.clipboard.writeText(politeResult)}
                  className="mt-3 text-xs text-sea underline hover:text-ink"
                >
                  Copy to clipboard
                </button>
              </div>
            )}
          </div>
        )}

        {/* Tab 2: Living Translator */}
        {activeTab === "translate" && (
          <div className="mt-6 rounded-2xl border border-sand-deep bg-white p-6 shadow-sm space-y-4">
            <div>
              <label className="block text-sm font-semibold text-ink mb-1">Text to Translate</label>
              <textarea
                rows={4}
                value={translateInput}
                onChange={(e) => setTranslateInput(e.target.value)}
                placeholder="Enter text in English or Korean (e.g. How much is the deposit? / 혹시 비자 연장 서류 확인해 주실 수 있나요?)"
                className="w-full rounded-md border border-sand-deep bg-mist/30 p-3 text-sm outline-none focus:border-sea"
              />
            </div>

            <div className="flex items-center gap-4">
              <label className="text-xs font-semibold text-ink">Target Language:</label>
              <select
                value={targetLang}
                onChange={(e) => setTargetLang(e.target.value)}
                className="rounded-md border border-sand-deep bg-white px-3 py-1.5 text-xs outline-none focus:border-sea"
              >
                <option value="ko">Korean (한국어)</option>
                <option value="en">English</option>
                <option value="ne">Nepali (नेपाली)</option>
                <option value="vi">Vietnamese (Tiếng Việt)</option>
                <option value="zh">Chinese (中文)</option>
                <option value="ja">Japanese (日本語)</option>
                <option value="uz">Uzbek (Oʻzbek)</option>
                <option value="ru">Russian (Русский)</option>
                <option value="th">Thai (ไทย)</option>
                <option value="mn">Mongolian (Монгол)</option>
                <option value="id">Indonesian (Bahasa Indonesia)</option>
                <option value="tl">Tagalog / Filipino</option>
              </select>
              <button
                onClick={handleTranslate}
                disabled={loading || !translateInput.trim()}
                className="ml-auto rounded-md bg-sea-deep px-5 py-2 text-sm font-semibold text-sand transition hover:bg-ink disabled:opacity-50"
              >
                {loading ? "Translating..." : "Translate"}
              </button>
            </div>

            {translateResult && (
              <div className="mt-6 rounded-xl border border-sea/30 bg-sand/30 p-4">
                <span className="text-xs font-semibold text-sea-deep uppercase tracking-wider">Translation Result</span>
                <p className="mt-2 text-base font-medium text-ink whitespace-pre-wrap">{translateResult}</p>
              </div>
            )}
          </div>
        )}

        {/* Tab 3: Summarize */}
        {activeTab === "summarize" && (
          <div className="mt-6 rounded-2xl border border-sand-deep bg-white p-6 shadow-sm space-y-4">
            <div>
              <label className="block text-sm font-semibold text-ink mb-1">
                Paste Korean Lease Contract Clause, Job Notice, or Civil Document
              </label>
              <textarea
                rows={6}
                value={summarizeInput}
                onChange={(e) => setSummarizeInput(e.target.value)}
                placeholder="임대차 계약서 조항이나 출입국 공지사항 등을 여기에 붙여넣으세요..."
                className="w-full rounded-md border border-sand-deep bg-mist/30 p-3 text-sm outline-none focus:border-sea"
              />
            </div>

            <div className="flex justify-end">
              <button
                onClick={handleSummarize}
                disabled={loading || !summarizeInput.trim()}
                className="rounded-md bg-sea-deep px-5 py-2 text-sm font-semibold text-sand transition hover:bg-ink disabled:opacity-50"
              >
                {loading ? "Summarizing..." : "Extract Key Takeaways"}
              </button>
            </div>

            {summarizeResult && (
              <div className="mt-6 rounded-xl border border-sea/30 bg-sand/30 p-4">
                <span className="text-xs font-semibold text-sea-deep uppercase tracking-wider">Key Takeaways</span>
                <p className="mt-2 text-sm font-medium text-ink whitespace-pre-wrap leading-relaxed">
                  {summarizeResult}
                </p>
              </div>
            )}
          </div>
        )}
      </section>
    </main>
  );
}
