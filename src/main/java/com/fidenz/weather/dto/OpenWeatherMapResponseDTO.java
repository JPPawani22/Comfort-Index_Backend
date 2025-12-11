package com.fidenz.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OpenWeatherMapResponseDTO {
    private Coord coord;
    private List<Weather> weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Integer visibility;
    private Long dt;
    private Sys sys;
    private Integer timezone;
    private Long id;
    private String name;
    private Integer cod;

    @Data
    public static class Coord {
        private Double lon;
        private Double lat;
    }

    @Data
    public static class Weather {
        private Integer id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    public static class Main {
        private Double temp;
        @JsonProperty("feels_like")
        private Double feelsLike;
        private Double pressure;
        private Double humidity;
        @JsonProperty("temp_min")
        private Double tempMin;
        @JsonProperty("temp_max")
        private Double tempMax;
    }

    @Data
    public static class Wind {
        private Double speed;
        private Double deg;
        @JsonProperty("gust")
        private Double gust;
    }

    @Data
    public static class Clouds {
        private Integer all;
    }

    @Data
    public static class Sys {
        private Integer type;
        private Long id;
        private String country;
        private Long sunrise;
        private Long sunset;
    }
}