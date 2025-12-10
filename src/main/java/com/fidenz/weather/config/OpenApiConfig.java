package com.fidenz.weather.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()
                .info(new Info()
                        .title("Weather Analytics API")
                        .version("1.0.0")
                        .description("""
                    ## Weather Analytics Application - Fidenz Assignment
                    
                    A secure weather analytics application that retrieves weather data, 
                    processes it using a custom Comfort Index metric, and presents meaningful insights.
                    
                    ### Features:
                    - ✅ Weather data retrieval from OpenWeatherMap API
                    - ✅ Custom Comfort Index calculation with weighted parameters
                    - ✅ Server-side caching (5-minute TTL)
                    - ✅ Authentication with Auth0 JWT
                    - ✅ Multi-factor authentication support
                    - ✅ Restricted signups (whitelisted users only)
                    
                    ### Authentication:
                    This API uses Auth0 for authentication. To access protected endpoints:
                    1. Obtain a JWT token from Auth0
                    2. Include it in the Authorization header: `Bearer {token}`
                    
                    ### Test Credentials:
                    - Email: `careers@fidenz.com`
                    - Password: `Pass#fidenz`
                    """)
                        .contact(new Contact()
                                .name("Pawani Uthpalawanna")
                                .email("pawani02jp@gmail.com")
                                .url("https://github.com/JPPawani22"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from Auth0")));
    }
}