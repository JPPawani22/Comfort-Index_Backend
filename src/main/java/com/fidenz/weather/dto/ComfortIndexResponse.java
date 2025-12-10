package com.fidenz.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Comfort index calculation response")
public class ComfortIndexResponse {

    @Schema(description = "Comfort score (0-100)", example = "72.5")
    @JsonProperty("comfort_score")
    private Double comfortScore;

    @Schema(description = "Comfort level description", example = "Comfortable")
    @JsonProperty("comfort_level")
    private String comfortLevel;

    @Schema(description = "Temperature contribution to score", example = "80.0")
    @JsonProperty("temperature_impact")
    private Double temperatureImpact;

    @Schema(description = "Humidity contribution to score", example = "65.0")
    @JsonProperty("humidity_impact")
    private Double humidityImpact;

    @Schema(description = "Wind contribution to score", example = "75.0")
    @JsonProperty("wind_impact")
    private Double windImpact;

    // Constructor with only 5 parameters for convenience
    public ComfortIndexResponse(Double comfortScore, String comfortLevel,
                                Double temperatureImpact, Double humidityImpact,
                                Double windImpact) {
        this.comfortScore = comfortScore;
        this.comfortLevel = comfortLevel;
        this.temperatureImpact = temperatureImpact;
        this.humidityImpact = humidityImpact;
        this.windImpact = windImpact;
    }
}