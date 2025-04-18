package com.johanes.cities.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.stereotype.Service;

import com.johanes.cities.model.City;
import com.johanes.cities.model.Suggestion;
import com.johanes.cities.repository.CityRepository;

@Service
public class CityService {
    private static final int EARTH_RADIUS_KM = 6371;
    private static final double NAME_MATCH_WEIGHT = 0.7;
    private static final double LOCATION_MATCH_WEIGHT = 0.3;
    private static final double MAX_DISTANCE_KM = 1000.0;
    private static final double FUZZY_MATCH_THRESHOLD = 0.8;

    private final CityRepository cityRepository;
    private final JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<Suggestion> findSuggestions(String searchTerm, Double userLatitude, Double userLongitude) {
        return cityRepository.findAll().stream()
                .filter(city -> matchesSearchTerm(city, searchTerm))
                .map(city -> createSuggestion(city, searchTerm, userLatitude, userLongitude))
                .sorted(Comparator.comparing(Suggestion::getScore).reversed())
                .collect(Collectors.toList());
    }

    private boolean matchesSearchTerm(City city, String searchTerm) {
        String normalizedSearchTerm = searchTerm.toLowerCase();
        String cityName = city.getName().toLowerCase();
        String cityAscii = city.getAscii().toLowerCase();

        // Exact or partial match
        if (cityName.contains(normalizedSearchTerm) || cityAscii.contains(normalizedSearchTerm)) {
            return true;
        }

        // Fuzzy match using Jaro-Winkler
        double nameSimilarity = similarity.apply(cityName, normalizedSearchTerm);
        double asciiSimilarity = similarity.apply(cityAscii, normalizedSearchTerm);

        return nameSimilarity >= FUZZY_MATCH_THRESHOLD || asciiSimilarity >= FUZZY_MATCH_THRESHOLD;
    }

    private Suggestion createSuggestion(City city, String searchTerm, Double userLatitude, Double userLongitude) {
        double nameMatchScore = calculateNameMatchScore(city, searchTerm);
        double locationMatchScore = calculateLocationMatchScore(city, userLatitude, userLongitude);
        double totalScore = calculateTotalScore(nameMatchScore, locationMatchScore);

        return new Suggestion(
                formatCityName(city),
                city.getLatitude(),
                city.getLongitude(),
                totalScore);
    }

    private String formatCityName(City city) {
        return String.format("%s, %s, %s", city.getName(), city.getAdmin1(), city.getCountry());
    }

    private double calculateTotalScore(double nameMatchScore, double locationMatchScore) {
        return locationMatchScore > 0
                ? (nameMatchScore * NAME_MATCH_WEIGHT + locationMatchScore * LOCATION_MATCH_WEIGHT)
                : nameMatchScore;
    }

    private double calculateNameMatchScore(City city, String searchTerm) {
        String cityName = city.getName().toLowerCase();
        String normalizedSearchTerm = searchTerm.toLowerCase();

        // Exact match
        if (cityName.equals(normalizedSearchTerm))
            return 1.0;

        // Partial match
        if (cityName.startsWith(normalizedSearchTerm))
            return 0.9;
        if (cityName.contains(normalizedSearchTerm))
            return 0.8;

        // Fuzzy match score
        double nameSimilarity = similarity.apply(cityName, normalizedSearchTerm);
        double asciiSimilarity = similarity.apply(city.getAscii().toLowerCase(), normalizedSearchTerm);
        double fuzzyScore = Math.max(nameSimilarity, asciiSimilarity);

        return fuzzyScore >= FUZZY_MATCH_THRESHOLD ? fuzzyScore : 0.5;
    }

    private double calculateLocationMatchScore(City city, Double userLatitude, Double userLongitude) {
        if (userLatitude == null || userLongitude == null)
            return 0.0;

        double distanceInKm = calculateDistanceInKm(
                city.getLatitude(),
                city.getLongitude(),
                userLatitude,
                userLongitude);
        return Math.max(0, 1 - (distanceInKm / MAX_DISTANCE_KM));
    }

    private double calculateDistanceInKm(double cityLatitude, double cityLongitude, double userLatitude,
            double userLongitude) {
        double latitudeDifference = Math.toRadians(userLatitude - cityLatitude);
        double longitudeDifference = Math.toRadians(userLongitude - cityLongitude);

        double a = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2)
                + Math.cos(Math.toRadians(cityLatitude)) * Math.cos(Math.toRadians(userLatitude))
                        * Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}