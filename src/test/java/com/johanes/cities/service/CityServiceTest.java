package com.johanes.cities.service;

import com.johanes.cities.model.City;
import com.johanes.cities.model.Suggestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityServiceTest {

    private CityService cityService;

    @BeforeEach
    void setUp() {
        cityService = new CityService();
        List<City> cities = List.of(
                new City("Toronto", "Toronto", 43.70011, -79.4163, "CA", "08", 2731571),
                new City("Montreal", "Montreal", 45.50884, -73.58781, "CA", "10", 1704694));
        ReflectionTestUtils.setField(cityService, "cities", cities);
    }

    @Test
    void findSuggestions_ReturnsResults() {
        List<Suggestion> results = cityService.findSuggestions("tor", null, null);
        assertFalse(results.isEmpty());
        assertEquals("Toronto, 08, CA", results.get(0).getName());
    }

    @Test
    void findSuggestions_NoMatch_ReturnsEmpty() {
        List<Suggestion> results = cityService.findSuggestions("xyz", null, null);
        assertTrue(results.isEmpty());
    }
}