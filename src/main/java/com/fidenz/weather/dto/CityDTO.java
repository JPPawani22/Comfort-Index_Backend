package com.fidenz.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {
    @JsonProperty("CityCode")
    private String cityCode;

    @JsonProperty("CityName")
    private String cityName;

    @JsonProperty("Temp")
    private String temp;

    @JsonProperty("Status")
    private String status;
}