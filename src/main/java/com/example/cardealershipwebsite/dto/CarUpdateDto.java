package com.example.cardealershipwebsite.dto;

import com.example.cardealershipwebsite.model.FuelType;
import com.example.cardealershipwebsite.model.Transmission;
import lombok.Data;

/** Car Dto. */
@Data
public class CarUpdateDto {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String bodyType;
    private String color;
    private Transmission transmission;
    private FuelType fuelType;
    private int power;
    private double engineVolume;
    private double fuelConsumption;
    private int trunkVolume;
    private double price;
}
