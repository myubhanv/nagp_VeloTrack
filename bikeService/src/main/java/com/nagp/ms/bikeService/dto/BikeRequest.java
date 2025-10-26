package com.nagp.ms.bikeService.dto;

import lombok.Data;

@Data
public class BikeRequest {

    private String model;
    private Double latitude;
    private Double longitude;
}
