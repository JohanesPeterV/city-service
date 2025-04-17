package com.johanes.suggestions.controller;

import com.johanes.suggestions.model.SuggestionResponse;
import com.johanes.suggestions.service.CityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CitySuggestionController {

    private final CityService cityService;

    public CitySuggestionController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/suggestions")
    public SuggestionResponse getSuggestions(
            @RequestParam String q,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        return new SuggestionResponse(cityService.findSuggestions(q, latitude, longitude));
    }
}