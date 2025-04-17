# Cities API

A Spring Boot application that provides city information and intelligent city name suggestions for Canada and USA cities. The application uses data from GeoNames and provides smart matching with location-aware scoring.

## Features

- **City Suggestions**: Get city suggestions based on partial name matches
- **Location-Aware Scoring**: Results are scored based on text match and geographic proximity
- **Case-Insensitive Search**: Search works regardless of text case
- **Smart Matching**: Supports exact, prefix, and partial matches with different scoring weights
- **Swagger UI**: Interactive API documentation and testing interface

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/cities.git
cd cities
```

2. Build the project:

```bash
./mvnw clean install
```

3. Run the application:

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### API Documentation

Access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

The Swagger UI provides:

- Interactive API documentation
- Try-it-out functionality for testing endpoints
- Detailed parameter descriptions
- Example requests and responses

## API Endpoints

### Get City Suggestions

Returns city suggestions based on a search query and optional location coordinates.

```http
GET /suggestions?q={query}&latitude={latitude}&longitude={longitude}
```

#### Parameters

- `q` (required): The search query string (partial or complete city name)
- `latitude` (optional): Latitude of the reference point for location-based scoring
- `longitude` (optional): Longitude of the reference point for location-based scoring

#### Response Format

```json
{
  "suggestions": [
    {
      "name": "City Name, State/Province, Country",
      "latitude": 43.70011,
      "longitude": -79.4163,
      "score": 0.9
    }
  ]
}
```

#### Scoring System

The scoring algorithm considers:

- Exact name match: 1.0
- Prefix match: 0.9
- Contains match: 0.8
- Location proximity (when coordinates provided): Affects final score based on distance

#### Example Requests

1. Basic search:

```bash
curl "http://localhost:8080/suggestions?q=toronto"
```

2. Search with location:

```bash
curl "http://localhost:8080/suggestions?q=toronto&latitude=43.70011&longitude=-79.4163"
```

3. Partial name search:

```bash
curl "http://localhost:8080/suggestions?q=tor"
```

## Data Source

The application uses data from GeoNames, specifically for cities in Canada and the USA. The data is loaded from a TSV file containing the following information for each city:

- Name
- ASCII name
- Latitude/Longitude
- Country
- State/Province
- Population

## Technical Details

Built with:

- Spring Boot 3.3.10
- Java 17
- Maven
- Lombok

Key components:

- `CityController`: Handles HTTP requests
- `CityService`: Core business logic and city matching
- `City`: Data model for city information
- `Suggestion`: Data model for city suggestions with scoring
