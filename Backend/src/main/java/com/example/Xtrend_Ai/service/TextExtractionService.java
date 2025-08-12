package com.example.Xtrend_Ai.service;


import com.example.Xtrend_Ai.exceptions.BadRequestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.time.Duration;

@Service
public class TextExtractionService {



    private static final int TIMEOUT_MS = (int) Duration.ofSeconds(8).toMillis();
    private static final int MAX_TITLE_LEN = 100;

    public String extractTitleOrDomain(String rawUrl) {
        String url = normalizeUrl(rawUrl);
        try {
            // Fetch HTML like a real browser
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/124.0 Safari/537.36")
                    .referrer("https://www.google.com/")
                    .timeout(TIMEOUT_MS)
                    .followRedirects(true)
                    .ignoreContentType(false) // fail early on non-HTML
                    .get();


            String ctype = doc.connection().response().contentType();
            if (ctype == null || !ctype.toLowerCase().contains("text/html")) {
                /// this returns the domain name instead if the title can not be accessed
                return fallbackDomain(url);
            }

            String title = extractTitle(doc);
            if (isBlank(title)) {
                return fallbackDomain(url);
            }
            return tidy(title);

        } catch (Exception e) {
            // Any network/HTTP/parse problem -> fallback to domain
            return fallbackDomain(url);
        }
    }

    private String extractTitle(Document doc) {
        // 1) <title>
        String title = doc.title();
        if (!isBlank(title)) return title;

        // 2) <meta property="og:title"> or <meta name="twitter:title">
        Element og = doc.selectFirst("meta[property=og:title], meta[name=og:title]");
        if (og != null) {
            String c = og.attr("content");
            if (!isBlank(c)) return c;
        }
        Element tw = doc.selectFirst("meta[name=twitter:title]");
        if (tw != null) {
            String c = tw.attr("content");
            if (!isBlank(c)) return c;
        }

        // 3) First <h1> as a last resort
        Element h1 = doc.selectFirst("h1");
        if (h1 != null && !isBlank(h1.text())) return h1.text();

        return null;
    }

    private String fallbackDomain(String url) {
        try {
            URI uri = URI.create(url);
            String host = uri.getHost();
            if (host == null) return "Unknown Site";
            // strip common prefixes
            host = host.replaceFirst("^www\\.", "");
            return host;
        } catch (Exception ignored) {
            return "Unknown Site";
        }
    }

    private String tidy(String title) {
        // Collapse whitespace and truncate
        String t = title.replaceAll("\\s+", " ").trim();
        if (t.length() > MAX_TITLE_LEN) {
            t = t.substring(0, MAX_TITLE_LEN - 1).trim() + "…";
        }
        return t;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String normalizeUrl(String url) {
        // You said you already validate https; still ensure a scheme
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new BadRequestException("url must start with http:// or https://");
        }
        return url;
    }


    public String ExtractTextForTitle(String text){
        String extracted = text.substring(0,20);
        return extracted.concat("...");
    }
}
