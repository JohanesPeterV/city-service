package com.johanes.cities.repository;

import com.johanes.cities.model.City;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CityRepository {
    private final String dataDirectory;
    private final String dataFile;
    private List<City> cities;

    public CityRepository(
            @Value("${app.data.directory}") String dataDirectory,
            @Value("${app.data.file}") String dataFile) {
        this.dataDirectory = dataDirectory;
        this.dataFile = dataFile;
        this.cities = new ArrayList<>();
    }

    @PostConstruct
    public void loadCities() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(dataDirectory, dataFile))) {
            reader.readLine(); // Skip header

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
                if (fields.length >= 7) {
                    cities.add(createCityFromFields(fields));
                }
            }
        }
    }

    private City createCityFromFields(String[] fields) {
        String name = fields[1];
        String ascii = fields[2];
        double latitude = Double.parseDouble(fields[4]);
        double longitude = Double.parseDouble(fields[5]);
        String country = fields[8];
        String admin1 = fields[10];
        long population = Long.parseLong(fields[14]);

        return new City(name, ascii, latitude, longitude, country, admin1, population);
    }

    public List<City> findAll() {
        return cities;
    }
}