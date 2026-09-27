export type Guide = {
  id: string;
  slug: string;
  title: string;
  summary: string;
  body: string;
  topic: string;
  sourceUrl?: string | null;
  published: boolean;
  createdAt: string;
  updatedAt: string;
};

/** Human labels for the topic slugs stored in the database. */
export const TOPIC_LABELS: Record<string, string> = {
  "visa-immigration": "Visa & Immigration",
  healthcare: "Healthcare",
  "money-banking": "Money & Banking",
  "phone-internet": "Phone & Internet",
  housing: "Housing",
  jobs: "Jobs",
  language: "Language",
  "daily-life": "Daily Life",
};

export function topicLabel(topic: string): string {
  return TOPIC_LABELS[topic] ?? topic.replace(/-/g, " ").replace(/\b\w/g, (c) => c.toUpperCase());
}
