package com.fidenz.weather.service;

import com.fidenz.weather.dto.CacheStatusDTO;
import com.fidenz.weather.dto.ComfortIndexResponse;
import com.fidenz.weather.dto.WeatherResponseDTO;
import java.util.List;
import java.util.Map;

public interface WeatherService {

    // Weather data operations
    List<WeatherResponseDTO> getAllCitiesWeather(boolean forceRefresh);
    WeatherResponseDTO getCityWeather(String cityId, boolean forceRefresh);
    ComfortIndexResponse calculateComfortIndexForCity(String cityId);

    // Cache operations
    CacheStatusDTO getCacheStatus(String cityId);
    Map<String, Object> getCacheStatistics();
    void clearCacheForCity(String cityId);
    void clearAllCache();

    // Utility operations
    List<Map<String, Object>> getSupportedCities();
    Map<String, Object> getSystemInfo();
}