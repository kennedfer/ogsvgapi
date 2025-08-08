package com.kennedfer.ogsvgapi.theme;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ThemeConfig {
    private final Map<String, Theme> themes = Map.of(
            "light", new Theme("#f6f8fa", "#1f2328", "#8a9199", "#747a80"),
            "dark", new Theme("#0d1117","#f0f6fc", "#6e747c", "#8f959e")
    );

    public Theme getTheme(String theme){
        return themes.getOrDefault(theme, themes.get("dark"));
    }
}
