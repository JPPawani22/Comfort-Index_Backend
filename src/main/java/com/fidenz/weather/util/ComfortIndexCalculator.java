package com.fidenz.weather.util;

import com.fidenz.weather.dto.ComfortIndexResponse;
import com.fidenz.weather.dto.WeatherResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ComfortIndexCalculator {

    public ComfortIndexResponse calculateComfortIndex(WeatherResponseDTO weather) {
        // Temperature scoring: Optimal 20-25°C = 100, range 15-30°C
        double tempScore = calculateTemperatureScore(weather.getTemperature());

        // Humidity scoring: Optimal 40-60% = 100, range 30-70%
        double humidityScore = calculateHumidityScore(weather.getHumidity());

        // Wind speed scoring: Optimal 0-5 km/h = 100, range 0-20 km/h
        double windScore = calculateWindScore(weather.getWindSpeed());

        // Weighted average: Temperature 40%, Humidity 30%, Wind 30%
        double comfortScore = (tempScore * 0.4) + (humidityScore * 0.3) + (windScore * 0.3);

        // Ensure score is between 0-100
        comfortScore = Math.max(0, Math.min(100, comfortScore));

        String comfortLevel = getComfortLevel(comfortScore);

        return new ComfortIndexResponse(
                Math.round(comfortScore * 10.0) / 10.0,
                comfortLevel,
                tempScore,
                humidityScore,
                windScore
        );
    }

    private double calculateTemperatureScore(double tempCelsius) {
        if (tempCelsius >= 20 && tempCelsius <= 25) {
            return 100;
        } else if (tempCelsius >= 15 && tempCelsius <= 30) {
            if (tempCelsius < 20) {
                return 50 + ((tempCelsius - 15) / 5) * 50;
            } else {
                return 100 - ((tempCelsius - 25) / 5) * 50;
            }
        } else {
            double diff = Math.min(Math.abs(tempCelsius - 20), Math.abs(tempCelsius - 25));
            return Math.max(0, 50 - diff * 10);
        }
    }

    private double calculateHumidityScore(double humidity) {
        if (humidity >= 40 && humidity <= 60) {
            return 100;
        } else if (humidity >= 30 && humidity <= 70) {
            if (humidity < 40) {
                return 50 + ((humidity - 30) / 10) * 50;
            } else {
                return 100 - ((humidity - 60) / 10) * 50;
            }
        } else {
            double diff = Math.min(Math.abs(humidity - 40), Math.abs(humidity - 60));
            return Math.max(0, 50 - diff * 5);
        }
    }

    private double calculateWindScore(double windSpeed) {
        // Convert to km/h for scoring
        double windKmh = windSpeed * 3.6;

        if (windKmh <= 5) {
            return 100;
        } else if (windKmh <= 20) {
            return 100 - ((windKmh - 5) / 15) * 50;
        } else {
            return Math.max(0, 50 - (windKmh - 20) * 2);
        }
    }

    private String getComfortLevel(double score) {
        if (score >= 80) return "Very Comfortable";
        if (score >= 60) return "Comfortable";
        if (score >= 40) return "Moderate";
        if (score >= 20) return "Uncomfortable";
        return "Very Uncomfortable";
    }
}