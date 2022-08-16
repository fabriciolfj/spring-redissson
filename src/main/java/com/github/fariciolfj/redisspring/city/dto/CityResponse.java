package com.github.fariciolfj.redisspring.city.dto;

import lombok.Data;

@Data
public class CityResponse {

    private String zip;
    private String city;
    private String stateName;
    private int temperature;
}
