package com.johanes.cities.repository;

import com.johanes.cities.model.City;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityRepositoryTest {

    @Test
    void loadCities_LoadsData() throws IOException {
        String testData = "geonameid\tname\tasciiname\tlatitude\tlongitude\tcountry code\tadmin1 code\tpopulation\n" +
                "1\tToronto\tToronto\t43.70011\t-79.4163\tCA\t08\t2731571";

        Path testFile = Files.createTempFile("test-cities", ".tsv");
        Files.writeString(testFile, testData);

        CityRepository repository = new CityRepository("", testFile.toString());
        repository.loadCities();

        List<City> cities = repository.findAll();
        assertEquals(1, cities.size());

        City city = cities.get(0);
        assertEquals("Toronto", city.getName());
        assertEquals(43.70011, city.getLatitude());
        assertEquals(-79.4163, city.getLongitude());
        assertEquals("CA", city.getCountry());
        assertEquals("08", city.getAdmin1());
        assertEquals(2731571, city.getPopulation());

        Files.delete(testFile);
    }
}