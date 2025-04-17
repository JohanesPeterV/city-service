# Cities API

A Spring Boot application that provides city information and intelligent city name suggestions for Canada and USA cities. The application uses data from GeoNames and provides smart matching with location-aware scoring.

## Features

- **City Suggestions**: Get city suggestions based on partial name matches
- **Location-Aware Scoring**: Results are scored based on text match and geographic proximity
- **Case-Insensitive Search**: Search works regardless of text case
- **Smart Matching**: Supports exact, prefix, and partial matches with different scoring weights
- **Swagger UI**: Interactive API documentation and testing interface
- **Fuzzy Search**: Intelligent matching that handles typos and misspellings (e.g., "Londn" → "London", "Tornto" → "Toronto")

## Live Demo

The API is deployed on Heroku and can be accessed at:

```
https://city-service-372784ecbcb6.herokuapp.com
```

### Example API Calls

1. Basic search:

```
https://city-service-372784ecbcb6.herokuapp.com/suggestions?q=toronto
```

2. Search with location:

```
https://city-service-372784ecbcb6.herokuapp.com/suggestions?q=toronto&latitude=43.70011&longitude=-79.4163
```

3. Partial name search:

```
https://city-service-372784ecbcb6.herokuapp.com/suggestions?q=tor
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Local Development

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

## Rate Limiting

The API implements rate limiting to ensure fair usage and prevent abuse. The current limits are:

- 100 requests per minute per IP address
- Requests exceeding the limit will receive a 429 (Too Many Requests) status code

You can test the rate limiter by making multiple requests in quick succession:

```bash
for i in {1..110}; do echo "Request #$i"; curl -s -w "\nStatus: %{http_code}\n" "http://localhost:8080/suggestions?q=toronto"; echo "-------------------"; done
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

## Deployment

The application is deployed on Heroku. The deployment process includes:

1. Automatic build and deployment from the main branch
2. Environment configuration for production
3. Proper scaling and monitoring setup

## Potential Improvements

While the current implementation satisfies the core requirements effectively, there are several potential improvements that could be added as the application evolves:

- Caching for frequently searched terms
- Enhanced logging and monitoring
- Alternative city names support
- Population-based scoring adjustments

These improvements can be considered based on actual usage patterns and specific needs that arise. For now there is no need yet for that.

## Contributing

Feel free to submit issues and enhancement requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
