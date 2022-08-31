package com.github.fariciolfj.redisspring.geo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fariciolfj.redisspring.geo.dto.Restaurant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class RestaurantUtil {

    public static List<Restaurant> getRestaurants() {
        final ObjectMapper mapper = new ObjectMapper();
        final InputStream stream = RestaurantUtil.class.getClassLoader().getResourceAsStream("restaurant.json");

        try {
            return mapper.readValue(stream, new TypeReference<>() {
            });
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
