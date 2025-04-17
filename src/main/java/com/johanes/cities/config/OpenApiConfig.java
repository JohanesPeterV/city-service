package com.johanes.cities.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cities API")
                        .description("API for city information and intelligent city name suggestions")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Cities API Support")
                                .url("http://localhost:8080")));
    }
}