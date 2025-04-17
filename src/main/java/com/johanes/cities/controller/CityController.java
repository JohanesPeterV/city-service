package com.johanes.cities.controller;

import com.johanes.cities.model.SuggestionResponse;
import com.johanes.cities.service.CityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
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