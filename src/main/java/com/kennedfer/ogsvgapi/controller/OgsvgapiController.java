package com.kennedfer.ogsvgapi.controller;

import com.kennedfer.ogsvgapi.service.OgsvgapiService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OgsvgapiController {

    private final OgsvgapiService service;

    @GetMapping(value = "/api/v1/svg", produces = "image/svg+xml")
    public String getSvg(@RequestParam String url) throws IOException {
        return service.getSvgStringFromUrl(url);
    }
}
