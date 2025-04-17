package com.johanes.cities.repository;

import com.johanes.cities.model.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    void findAll_ReturnsLoadedCities() {
        List<City> cities = cityRepository.findAll();

        assertNotNull(cities);
        assertFalse(cities.isEmpty());
        assertEquals(3, cities.size());

        City toronto = cities.stream()
                .filter(c -> c.getName().equals("Toronto"))
                .findFirst()
                .orElseThrow();

        assertEquals("Toronto", toronto.getName());
        assertEquals(43.70011, toronto.getLatitude());
        assertEquals(-79.4163, toronto.getLongitude());
        assertEquals("CA", toronto.getCountry());
        assertEquals("08", toronto.getAdmin1());
    }
}