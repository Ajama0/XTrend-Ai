export interface PodcastRequest {
    email: string;
    newsId?: number;
    contentForm: string; // "SHORT" | "LONG"
    podcastType?: string; // "INPUT" | "FILE" | "NEWS"
    url?: string; // For website URLs
    text?: string; // For AI summarizer text input
}