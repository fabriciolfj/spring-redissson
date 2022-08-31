package com.github.fariciolfj.redisspring.geo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class GeoLocation {

    private double longitude;
    private double latitude;
}
