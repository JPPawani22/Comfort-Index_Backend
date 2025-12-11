package com.fidenz.weather.controller;

import com.fidenz.weather.dto.CacheStatusDTO;
import com.fidenz.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
@Tag(name = "Cache Management", description = "Cache operations and monitoring")
public class CacheController {

    private final WeatherService weatherService;

    @GetMapping("/status/{cityId}")
    @Operation(
            summary = "Get cache status for a city",
            description = "Returns cache hit/miss status and TTL information"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cache status"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    public ResponseEntity<CacheStatusDTO> getCacheStatus(
            @Parameter(description = "City ID", example = "1248991")
            @PathVariable String cityId) {

        CacheStatusDTO cacheStatus = weatherService.getCacheStatus(cityId);
        return ResponseEntity.ok(cacheStatus);
    }

    @GetMapping("/statistics")
    @Operation(
            summary = "Get cache statistics",
            description = "Returns detailed cache statistics including hit rates and eviction counts"
    )
    @PreAuthorize("hasAuthority('SCOPE_read:cache')")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved cache statistics")
    public ResponseEntity<Map<String, Object>> getCacheStatistics() {
        Map<String, Object> stats = weatherService.getCacheStatistics();
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/clear/{cityId}")
    @Operation(
            summary = "Clear cache for a specific city",
            description = "Evicts cached weather data for a single city"
    )
    @PreAuthorize("hasAuthority('SCOPE_write:cache')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cache cleared successfully"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    public ResponseEntity<Map<String, String>> clearCityCache(
            @Parameter(description = "City ID", example = "1248991")
            @PathVariable String cityId) {

        weatherService.clearCacheForCity(cityId);
        return ResponseEntity.ok(Map.of(
                "message", "Cache cleared for city: " + cityId,
                "status", "SUCCESS"
        ));
    }

    @DeleteMapping("/clear-all")
    @Operation(
            summary = "Clear all cache",
            description = "Evicts all cached weather data"
    )
    @PreAuthorize("hasAuthority('SCOPE_admin:cache')")
    @ApiResponse(responseCode = "200", description = "All cache cleared successfully")
    public ResponseEntity<Map<String, String>> clearAllCache() {
        weatherService.clearAllCache();
        return ResponseEntity.ok(Map.of(
                "message", "All cache cleared successfully",
                "status", "SUCCESS"
        ));
    }
}