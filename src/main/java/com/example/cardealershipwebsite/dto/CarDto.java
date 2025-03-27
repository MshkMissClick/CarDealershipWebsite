package com.example.cardealershipwebsite.dto;

import com.example.cardealershipwebsite.model.FuelType;
import com.example.cardealershipwebsite.model.Transmission;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Car Dto. */
@Data
public class CarDto {
    private Long id;
    @NotBlank(message = "Название бренда не может быть пустым")
    private String brand;
    @NotBlank(message = "Название модели не может быть пустым")
    private String model;
    @NotBlank(message = "Год выпуска не может быть пустым")
    private Integer year;
    @NotBlank(message = "Тип кузова не может быть пустым")
    private String bodyType;
    @NotBlank(message = "Цвет не может быть пустым")
    private String color;
    @NotBlank(message = "Тип трансмиссии не может быть пустым")
    private Transmission transmission;
    @NotBlank(message = "Тип топлива не может быть пустым")
    private FuelType fuelType;
    @NotBlank(message = "Мощность не может быть пустой")
    private int power;
    @NotBlank(message = "Объём двигателя не может быть пустым")
    private double engineVolume;
    @NotBlank(message = "Расход топлива не может быть пустым не может быть пустым")
    private double fuelConsumption;
    @NotBlank(message = "Объём багажника не может быть пустым")
    private int trunkVolume;
    @NotBlank(message = "Цена не может быть пустой")
    private double price;
    private DisplayUserDto userWhoOrdered; // Может быть null
}
