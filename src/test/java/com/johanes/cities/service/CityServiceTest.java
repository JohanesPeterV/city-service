package com.johanes.cities.service;

import com.johanes.cities.model.City;
import com.johanes.cities.model.Suggestion;
import com.johanes.cities.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    void setUp() {
        List<City> cities = List.of(
                new City("Toronto", "Toronto", 43.70011, -79.4163, "CA", "08", 2731571),
                new City("Montreal", "Montreal", 45.50884, -73.58781, "CA", "10", 1704694));
        when(cityRepository.findAll()).thenReturn(cities);
    }

    @Test
    void findSuggestions_ReturnsResults() {
        List<Suggestion> results = cityService.findSuggestions("tor", null, null);
        assertFalse(results.isEmpty());
        assertEquals("Toronto, 08, CA", results.get(0).getName());
        assertTrue(results.get(0).getScore() > 0);
    }

    @Test
    void findSuggestions_NoMatch_ReturnsEmpty() {
        List<Suggestion> results = cityService.findSuggestions("xyz", null, null);
        assertTrue(results.isEmpty());
    }

    @Test
    void findSuggestions_WithLocation_AdjustsScores() {
        List<Suggestion> results = cityService.findSuggestions(
                "tor", 43.70011, -79.4163); // Toronto coordinates
        assertFalse(results.isEmpty());
        assertEquals("Toronto, 08, CA", results.get(0).getName());
        assertTrue(results.get(0).getScore() > 0.5); // Should have high score due to location
    }
}