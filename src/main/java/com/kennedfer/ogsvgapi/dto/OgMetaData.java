package com.kennedfer.ogsvgapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class OgMetaData {
    private String title;
    private String description;
    private String url;
    private String image;
}
