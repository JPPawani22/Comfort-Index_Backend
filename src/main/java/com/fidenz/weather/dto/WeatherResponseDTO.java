package com.fidenz.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Weather data response with comfort index")
public class WeatherResponseDTO {
    @Schema(description = "City ID from OpenWeatherMap", example = "1248991")
    @JsonProperty("city_id")
    private String cityId;

    @Schema(description = "City name", example = "Colombo")
    @JsonProperty("city_name")
    private String cityName;

    @Schema(description = "Country code", example = "LK")
    @JsonProperty("country")
    private String country;

    @Schema(description = "Temperature in Celsius", example = "33.0")
    @JsonProperty("temperature")
    private Double temperature;

    @Schema(description = "Feels like temperature", example = "36.5")
    @JsonProperty("feels_like")
    private Double feelsLike;

    @Schema(description = "Humidity percentage", example = "75.0")
    @JsonProperty("humidity")
    private Double humidity;

    @Schema(description = "Atmospheric pressure in hPa", example = "1012.0")
    @JsonProperty("pressure")
    private Double pressure;

    @Schema(description = "Wind speed in m/s", example = "3.1")
    @JsonProperty("wind_speed")
    private Double windSpeed;

    @Schema(description = "Cloudiness percentage", example = "40")
    @JsonProperty("cloudiness")
    private Integer cloudiness;

    @Schema(description = "Visibility in meters", example = "10000")
    @JsonProperty("visibility")
    private Integer visibility;

    @Schema(description = "Weather description", example = "few clouds")
    @JsonProperty("weather_description")
    private String weatherDescription;

    @Schema(description = "Weather icon URL")
    @JsonProperty("weather_icon")
    private String weatherIcon;

    @Schema(description = "Comfort index score (0-100)", example = "72.5")
    @JsonProperty("comfort_score")
    private Double comfortScore;

    @Schema(description = "Comfort level", example = "Comfortable")
    @JsonProperty("comfort_level")
    private String comfortLevel;

    @Schema(description = "Rank based on comfort score", example = "1")
    @JsonProperty("rank")
    private Integer rank;

    @Schema(description = "Cache status", example = "HIT")
    @JsonProperty("cache_status")
    private String cacheStatus;

    @Schema(description = "Timestamp of data fetch")
    @JsonProperty("timestamp")
    private String timestamp;
}