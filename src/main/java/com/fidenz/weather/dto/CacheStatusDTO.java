package com.fidenz.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Cache status information")
public class CacheStatusDTO {
    @Schema(description = "City ID", example = "1248991")
    @JsonProperty("city_id")
    private String cityId;

    @Schema(description = "Cache status (HIT/MISS/EXPIRED)", example = "HIT")
    @JsonProperty("cache_status")
    private String cacheStatus;

    @Schema(description = "Time to live in seconds", example = "300")
    @JsonProperty("ttl_seconds")
    private Long ttlSeconds;

    @Schema(description = "Cache key")
    @JsonProperty("cache_key")
    private String cacheKey;

    @Schema(description = "Is cache expired")
    @JsonProperty("is_expired")
    private Boolean isExpired;
}