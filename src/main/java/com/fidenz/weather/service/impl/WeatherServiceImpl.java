package com.fidenz.weather.service.impl;

import com.fidenz.weather.dto.*;
import com.fidenz.weather.service.WeatherService;
import com.fidenz.weather.util.ComfortIndexCalculator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ClassPathResource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;
    private final ComfortIndexCalculator comfortCalculator;
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Value("${weather.api.cache-duration:300}")
    private long cacheDuration;

    @Value("${weather.mock.enabled:false}")
    private boolean useMockData;

    private final Map<String, CacheStatusDTO> cacheStatusMap = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastFetchTime = new ConcurrentHashMap<>();
    private List<CityDTO> cachedCities = null;

    @Override
    @Cacheable(value = "weatherData", key = "'all_cities_' + #forceRefresh")
    public List<WeatherResponseDTO> getAllCitiesWeather(boolean forceRefresh) {
        log.info("Fetching weather data for all cities (forceRefresh: {})", forceRefresh);

        List<CityDTO> cities = loadCities();
        List<WeatherResponseDTO> weatherList = new ArrayList<>();

        for (CityDTO city : cities) {
            try {
                WeatherResponseDTO weather = getCityWeather(city.getCityCode(), forceRefresh);
                if (weather != null) {
                    weatherList.add(weather);
                }
            } catch (Exception e) {
                log.error("Error fetching weather for city {}: {}", city.getCityCode(), e.getMessage());
            }
        }

        // Sort by comfort score descending (most comfortable first)
        weatherList.sort((w1, w2) -> Double.compare(w2.getComfortScore(), w1.getComfortScore()));

        // Assign ranks
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setRank(i + 1);
        }

        log.info("Successfully fetched weather for {} cities", weatherList.size());
        return weatherList;
    }

    @Override
    @Cacheable(value = "cityWeather", key = "#cityId", unless = "#result == null")
    public WeatherResponseDTO getCityWeather(String cityId, boolean forceRefresh) {
        if (forceRefresh) {
            evictCityCache(cityId);
        }

        updateCacheStatus(cityId, "MISS");
        lastFetchTime.put(cityId, LocalDateTime.now());

        try {
            OpenWeatherMapResponseDTO weatherData = fetchWeatherData(cityId);

            if (weatherData == null) {
                log.warn("No weather data received for city: {}", cityId);
                return createFallbackResponse(cityId);
            }

            WeatherResponseDTO response = convertToResponseDTO(weatherData);
            ComfortIndexResponse comfortIndex = comfortCalculator.calculateComfortIndex(response);

            response.setComfortScore(comfortIndex.getComfortScore());
            response.setComfortLevel(comfortIndex.getComfortLevel());
            response.setCacheStatus(cacheStatusMap.get(cityId).getCacheStatus());
            response.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            updateCacheStatus(cityId, "HIT");

            log.debug("Successfully fetched weather for city: {}", cityId);
            return response;

        } catch (Exception e) {
            log.error("Error fetching weather for city {}: {}", cityId, e.getMessage());
            updateCacheStatus(cityId, "ERROR");
            return createFallbackResponse(cityId);
        }
    }

    @Override
    public ComfortIndexResponse calculateComfortIndexForCity(String cityId) {
        WeatherResponseDTO weather = getCityWeather(cityId, false);
        if (weather == null) {
            throw new RuntimeException("Weather data not available for city: " + cityId);
        }

        return comfortCalculator.calculateComfortIndex(weather);
    }

    @Override
    public CacheStatusDTO getCacheStatus(String cityId) {
        CacheStatusDTO status = cacheStatusMap.get(cityId);
        if (status == null) {
            status = new CacheStatusDTO(cityId, "NOT_CACHED", cacheDuration,
                    "cityWeather::" + cityId, true);
        } else {
            LocalDateTime lastFetch = lastFetchTime.get(cityId);
            boolean isExpired = lastFetch != null &&
                    lastFetch.plusSeconds(cacheDuration).isBefore(LocalDateTime.now());
            status.setIsExpired(isExpired);
        }
        return status;
    }

    @Override
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> stats = new HashMap<>();

        Cache cache = cacheManager.getCache("cityWeather");
        if (cache instanceof CaffeineCache caffeineCache) {
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                    caffeineCache.getNativeCache();
            CacheStats cacheStats = nativeCache.stats();

            stats.put("hitCount", cacheStats.hitCount());
            stats.put("missCount", cacheStats.missCount());
            stats.put("loadSuccessCount", cacheStats.loadSuccessCount());
            stats.put("loadFailureCount", cacheStats.loadFailureCount());
            stats.put("totalLoadTime", cacheStats.totalLoadTime());
            stats.put("evictionCount", cacheStats.evictionCount());
            stats.put("evictionWeight", cacheStats.evictionWeight());
            stats.put("hitRate", cacheStats.hitRate());
            stats.put("averageLoadPenalty", cacheStats.averageLoadPenalty());
        }

        stats.put("cacheStatusMapSize", cacheStatusMap.size());
        stats.put("cacheDurationSeconds", cacheDuration);
        stats.put("cacheKeys", cacheStatusMap.keySet());

        return stats;
    }

    @Override
    @CacheEvict(value = "cityWeather", key = "#cityId")
    public void clearCacheForCity(String cityId) {
        log.info("Clearing cache for city: {}", cityId);
        cacheStatusMap.remove(cityId);
        lastFetchTime.remove(cityId);
    }

    @Override
    public void clearAllCache() {
        log.info("Clearing all caches");
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });
        cacheStatusMap.clear();
        lastFetchTime.clear();
        cachedCities = null;
    }

    @Override
    public List<Map<String, Object>> getSupportedCities() {
        List<CityDTO> cities = loadCities();
        List<Map<String, Object>> result = new ArrayList<>();

        for (CityDTO city : cities) {
            Map<String, Object> cityInfo = new HashMap<>();
            cityInfo.put("cityCode", city.getCityCode());
            cityInfo.put("cityName", city.getCityName());
            cityInfo.put("status", city.getStatus());
            cityInfo.put("temp", city.getTemp());
            result.add(cityInfo);
        }

        return result;
    }

    @Override
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "Weather Analytics API");
        info.put("version", "1.0.0");
        info.put("usingMockData", useMockData);
        info.put("cacheEnabled", true);
        info.put("cacheDurationSeconds", cacheDuration);
        info.put("totalSupportedCities", loadCities().size());
        info.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return info;
    }

    // Private helper methods
    private void updateCacheStatus(String cityId, String status) {
        CacheStatusDTO cacheStatus = new CacheStatusDTO(
                cityId, status, cacheDuration,
                "cityWeather::" + cityId, false
        );
        cacheStatusMap.put(cityId, cacheStatus);
    }

    @CacheEvict(value = "cityWeather", key = "#cityId")
    private void evictCityCache(String cityId) {
        updateCacheStatus(cityId, "EVICTED");
        lastFetchTime.remove(cityId);
        log.debug("Cache evicted for city: {}", cityId);
    }

    private OpenWeatherMapResponseDTO fetchWeatherData(String cityId) {
        if (useMockData) {
            return getMockWeatherData(cityId);
        }

        try {
            String url = String.format("%s/weather?id=%s&appid=%s&units=metric",
                    baseUrl, cityId, apiKey);

            log.debug("Calling OpenWeatherMap API: {}", url.replace(apiKey, "***"));
            return restTemplate.getForObject(url, OpenWeatherMapResponseDTO.class);

        } catch (Exception e) {
            log.warn("Failed to fetch from OpenWeatherMap, using mock data: {}", e.getMessage());
            return getMockWeatherData(cityId);
        }
    }

    private List<CityDTO> loadCities() {
        if (cachedCities != null) {
            return cachedCities;
        }

        try {
            ClassPathResource resource = new ClassPathResource("cities.json");
            cachedCities = objectMapper.readValue(resource.getInputStream(),
                    new TypeReference<List<CityDTO>>() {});
            log.info("Loaded {} cities from cities.json", cachedCities.size());
            return cachedCities;
        } catch (Exception e) {
            log.error("Error loading cities.json: {}", e.getMessage());
            return getDefaultCities();
        }
    }

    private List<CityDTO> getDefaultCities() {
        return Arrays.asList(
                new CityDTO("1248991", "Colombo", "33.0", "Clouds"),
                new CityDTO("1850147", "Tokyo", "8.6", "Clear"),
                new CityDTO("2644210", "Liverpool", "16.5", "Rain"),
                new CityDTO("2988507", "Paris", "22.4", "Clear"),
                new CityDTO("2147714", "Sydney", "27.3", "Rain"),
                new CityDTO("4930956", "Boston", "4.2", "Mist"),
                new CityDTO("1796236", "Shanghai", "10.1", "Clouds"),
                new CityDTO("3143244", "Oslo", "-3.9", "Clear")
        );
    }

    private WeatherResponseDTO convertToResponseDTO(OpenWeatherMapResponseDTO apiResponse) {
        WeatherResponseDTO dto = new WeatherResponseDTO();
        dto.setCityId(String.valueOf(apiResponse.getId()));
        dto.setCityName(apiResponse.getName());

        if (apiResponse.getSys() != null) {
            dto.setCountry(apiResponse.getSys().getCountry());
        } else {
            dto.setCountry("N/A");
        }

        if (apiResponse.getMain() != null) {
            dto.setTemperature(apiResponse.getMain().getTemp());
            dto.setFeelsLike(apiResponse.getMain().getFeelsLike());
            dto.setHumidity(apiResponse.getMain().getHumidity());
            dto.setPressure(apiResponse.getMain().getPressure());
        }

        if (apiResponse.getWind() != null) {
            dto.setWindSpeed(apiResponse.getWind().getSpeed());
        }

        if (apiResponse.getClouds() != null) {
            dto.setCloudiness(apiResponse.getClouds().getAll());
        }

        dto.setVisibility(apiResponse.getVisibility());

        if (apiResponse.getWeather() != null && !apiResponse.getWeather().isEmpty()) {
            dto.setWeatherDescription(apiResponse.getWeather().get(0).getDescription());
            dto.setWeatherIcon(String.format("https://openweathermap.org/img/wn/%s@2x.png",
                    apiResponse.getWeather().get(0).getIcon()));
        }

        return dto;
    }

    private OpenWeatherMapResponseDTO getMockWeatherData(String cityId) {
        try {
            ClassPathResource resource = new ClassPathResource("static/mock-weather-data.json");
            Map<String, OpenWeatherMapResponseDTO> mockData = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<Map<String, OpenWeatherMapResponseDTO>>() {}
            );

            OpenWeatherMapResponseDTO data = mockData.get(cityId);
            if (data != null) {
                return data;
            }
        } catch (Exception e) {
            log.debug("Mock data file not found or error: {}", e.getMessage());
        }

        return createDefaultMockData(cityId);
    }

    private OpenWeatherMapResponseDTO createDefaultMockData(String cityId) {
        OpenWeatherMapResponseDTO data = new OpenWeatherMapResponseDTO();
        data.setId(Long.parseLong(cityId));

        // Find city from cities.json
        CityDTO city = loadCities().stream()
                .filter(c -> c.getCityCode().equals(cityId))
                .findFirst()
                .orElse(new CityDTO(cityId, "Unknown City", "25.0", "Clear"));

        data.setName(city.getCityName());

        // Create main data
        OpenWeatherMapResponseDTO.Main main = new OpenWeatherMapResponseDTO.Main();
        try {
            main.setTemp(Double.parseDouble(city.getTemp()));
        } catch (NumberFormatException e) {
            main.setTemp(25.0);
        }
        main.setFeelsLike(main.getTemp() + 2);
        main.setHumidity(50.0 + (Math.random() * 40 - 20)); // 30-70%
        main.setPressure(1013.0);
        data.setMain(main);

        // Create wind data
        OpenWeatherMapResponseDTO.Wind wind = new OpenWeatherMapResponseDTO.Wind();
        wind.setSpeed(2.5 + (Math.random() * 5)); // 2.5-7.5 m/s
        data.setWind(wind);

        // Create clouds data
        OpenWeatherMapResponseDTO.Clouds clouds = new OpenWeatherMapResponseDTO.Clouds();
        clouds.setAll(city.getStatus().equals("Clear") ? 0 :
                city.getStatus().equals("Clouds") ? 40 : 60);
        data.setClouds(clouds);

        // Create weather data
        OpenWeatherMapResponseDTO.Weather weather = new OpenWeatherMapResponseDTO.Weather();
        weather.setMain(city.getStatus());
        weather.setDescription(city.getStatus().toLowerCase());
        weather.setIcon(city.getStatus().equals("Clear") ? "01d" : "03d");
        data.setWeather(Arrays.asList(weather));

        // Create sys data
        OpenWeatherMapResponseDTO.Sys sys = new OpenWeatherMapResponseDTO.Sys();
        sys.setCountry("XX");
        data.setSys(sys);

        data.setVisibility(10000);
        data.setCod(200);

        return data;
    }

    private WeatherResponseDTO createFallbackResponse(String cityId) {
        CityDTO city = loadCities().stream()
                .filter(c -> c.getCityCode().equals(cityId))
                .findFirst()
                .orElse(new CityDTO(cityId, "Unknown", "25.0", "Clear"));

        WeatherResponseDTO dto = new WeatherResponseDTO();
        dto.setCityId(cityId);
        dto.setCityName(city.getCityName());
        dto.setCountry("N/A");

        try {
            dto.setTemperature(Double.parseDouble(city.getTemp()));
        } catch (NumberFormatException e) {
            dto.setTemperature(25.0);
        }

        dto.setFeelsLike(dto.getTemperature() + 2);
        dto.setHumidity(50.0);
        dto.setPressure(1013.0);
        dto.setWindSpeed(3.0);
        dto.setCloudiness(city.getStatus().equals("Clear") ? 0 : 40);
        dto.setVisibility(10000);
        dto.setWeatherDescription(city.getStatus());
        dto.setWeatherIcon("https://openweathermap.org/img/wn/03d@2x.png");
        dto.setComfortScore(50.0);
        dto.setComfortLevel("Moderate");
        dto.setCacheStatus("FALLBACK");
        dto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return dto;
    }
}