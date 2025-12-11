package com.fidenz.weather.controller;

import com.fidenz.weather.dto.ComfortIndexResponse;
import com.fidenz.weather.dto.ErrorResponseDTO;
import com.fidenz.weather.dto.WeatherResponseDTO;
import com.fidenz.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
@Tag(name = "Weather API", description = "Weather data and comfort index operations")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/cities")
    @Operation(
            summary = "Get weather data for all cities",
            description = "Returns weather data for all supported cities with comfort index ranking"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved weather data"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<List<WeatherResponseDTO>> getAllCitiesWeather(
            @Parameter(description = "Force refresh data from API (bypass cache)")
            @RequestParam(defaultValue = "false") boolean forceRefresh) {

        List<WeatherResponseDTO> weatherList = weatherService.getAllCitiesWeather(forceRefresh);
        return ResponseEntity.ok(weatherList);
    }

    @GetMapping("/city/{cityId}")
    @Operation(
            summary = "Get weather data for a specific city",
            description = "Returns weather data and comfort index for a single city"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved city weather"),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<WeatherResponseDTO> getCityWeather(
            @Parameter(description = "City ID from OpenWeatherMap", example = "1248991")
            @PathVariable String cityId,

            @Parameter(description = "Force refresh data from API (bypass cache)")
            @RequestParam(defaultValue = "false") boolean forceRefresh) {

        WeatherResponseDTO weather = weatherService.getCityWeather(cityId, forceRefresh);

        if (weather == null) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    LocalDateTime.now(),
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    "City not found: " + cityId,
                    "/api/v1/weather/city/" + cityId,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(weather);
    }

    @GetMapping("/comfort-index/{cityId}")
    @Operation(
            summary = "Calculate comfort index for a city",
            description = "Calculates and returns the comfort index score with breakdown"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully calculated comfort index"),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<ComfortIndexResponse> getComfortIndex(
            @Parameter(description = "City ID", example = "1248991")
            @PathVariable String cityId) {

        ComfortIndexResponse comfortIndex = weatherService.calculateComfortIndexForCity(cityId);
        return ResponseEntity.ok(comfortIndex);
    }

    @GetMapping("/supported-cities")
    @Operation(
            summary = "Get list of supported cities",
            description = "Returns the list of cities supported by the application"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved city list")
    public ResponseEntity<List<Map<String, Object>>> getSupportedCities() {
        List<Map<String, Object>> cities = weatherService.getSupportedCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/health")
    @Operation(
            summary = "Health check endpoint",
            description = "Returns system health and status information"
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = weatherService.getSystemInfo();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }
}