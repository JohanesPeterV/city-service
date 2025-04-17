package com.johanes.cities.controller;

import com.johanes.cities.model.SuggestionResponse;
import com.johanes.cities.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Cities", description = "City information and suggestions API")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Operation(summary = "Get city suggestions", description = "Returns city suggestions based on a search query and optional location coordinates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved suggestions"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/suggestions")
    public SuggestionResponse getSuggestions(
            @Parameter(description = "Search query (partial or complete city name)", required = true) @RequestParam String q,

            @Parameter(description = "Latitude for location-based scoring (optional)") @RequestParam(required = false) Double latitude,

            @Parameter(description = "Longitude for location-based scoring (optional)") @RequestParam(required = false) Double longitude) {
        return new SuggestionResponse(cityService.findSuggestions(q, latitude, longitude));
    }
}