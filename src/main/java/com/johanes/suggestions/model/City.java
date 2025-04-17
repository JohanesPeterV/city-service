package com.johanes.suggestions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private String name;
    private String ascii;
    private double latitude;
    private double longitude;
    private String country;
    private String admin1;
    private long population;
}