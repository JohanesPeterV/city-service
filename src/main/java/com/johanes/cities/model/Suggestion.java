package com.johanes.cities.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {
    private String name;
    private double latitude;
    private double longitude;
    private double score;
}