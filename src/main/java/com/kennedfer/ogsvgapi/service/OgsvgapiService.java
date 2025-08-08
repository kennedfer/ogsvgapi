package com.kennedfer.ogsvgapi.service;

import com.kennedfer.ogsvgapi.dto.OgMetaData;

import com.kennedfer.ogsvgapi.theme.Theme;
import com.kennedfer.ogsvgapi.theme.ThemeConfig;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

@Service
@AllArgsConstructor
public class OgsvgapiService {
    private static final String BASE64_PLACEHOLDER=
            "data:image/png;base64iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8Xw8AAnsB9vCdY64AAAAASUVORK5CYII";

    private final ThemeConfig themeConfig;

    private String convertImageUrlToBase64(String imageUrl) throws IOException {
        if(imageUrl == null || imageUrl.isEmpty()) return BASE64_PLACEHOLDER;

        try(InputStream in = new URL(imageUrl).openStream()){
            byte[] imageBytes = in.readAllBytes();
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64;
        }
    }

    private OgMetaData extractOgMetaData(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        String title = doc.select("meta[property=og:title]").attr("content");
        String description = doc.select("meta[property=og:description]").attr("content");
        String pageUrl = doc.select("meta[property=og:url]").attr("content");
        String imageUrl = doc.select("meta[property=og:image]").attr("content");

        //Fallbacks
        if (title.isBlank()) title = "Página sem título Open Graph";
        if (description.isBlank()) description = "Sem descrição disponível";
        if (pageUrl.isBlank()) pageUrl = url; // usa a URL original como fallback

        return new OgMetaData(title, description, pageUrl, imageUrl);
    }

    private String escapeXml(String input) {
        return input == null ? "" : input
                .replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private String generateSvgFromMeta(OgMetaData meta, String requestTheme) throws IOException {
        String base64Image = convertImageUrlToBase64(meta.getImage());

        Theme theme = themeConfig.getTheme(requestTheme);

        return String.format("""
            <svg viewBox="0 0 1200 200" width="100%%" height="100%%" xmlns="http://www.w3.org/2000/svg">
                <rect width="100%%" height="100%%" fill="%s" rx="8" ry="8"/>

                <image preserveAspectRatio="xMidYMid slice" href="%s" x="20" y="20" width="300" height="160" clip-path="url(#clip)"/>

                <foreignObject x="340" y="40" width="820" height="40">
                    <div xmlns="http://www.w3.org/1999/xhtml"
                         style="font-size: 28px; font-weight: bold; font-family: sans-serif; color: %s; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                        %s
                    </div>
                </foreignObject>

                <foreignObject x="340" y="87" width="820" height="40">
                    <div xmlns="http://www.w3.org/1999/xhtml"
                         style="font-size: 25px; font-family: sans-serif; color: %s; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                        %s
                    </div>
                </foreignObject>

                <foreignObject x="340" y="130" width="820" height="40">
                    <div xmlns="http://www.w3.org/1999/xhtml"
                         style="font-size: 23px; font-family: sans-serif; color: %s; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                        %s
                    </div>
                </foreignObject>
            </svg>
            """,theme.getBackground(),
                base64Image,
                theme.getTitle(),
                escapeXml(meta.getTitle()),
                theme.getDescription(),
                escapeXml(meta.getDescription()),
                theme.getUrl(),
                escapeXml(meta.getUrl()));
    }

    public String getSvgStringFromUrl(String url, String theme) throws IOException {
        System.out.println("🔍 URL recebida: " + url);
        OgMetaData ogMetaData = extractOgMetaData(url);
        return generateSvgFromMeta(ogMetaData, theme);
    }
}
