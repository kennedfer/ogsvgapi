package com.kennedfer.ogsvgapi.services;

import com.kennedfer.ogsvgapi.dto.OgMetaData;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class OpenGraphService {
        private SvgService svgService;

    public OgMetaData extractOgMetaData(String url) throws IOException {
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


    public String getSvgStringFromUrl(String url, String theme) throws IOException {
        System.out.println("🔍 URL recebida: " + url);
        OgMetaData ogMetaData = extractOgMetaData(url);
        return svgService.generateSvg(ogMetaData, theme);
    }
}
