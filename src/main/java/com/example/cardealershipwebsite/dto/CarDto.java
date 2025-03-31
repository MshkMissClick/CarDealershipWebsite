package com.example.cardealershipwebsite.dto;

import com.example.cardealershipwebsite.model.FuelType;
import com.example.cardealershipwebsite.model.Transmission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Car Dto. */
@Data
public class CarDto {
    private Long id;
    @NotBlank(message = "Название бренда не может быть пустым")
    private String brand;
    @NotBlank(message = "Название модели не может быть пустым")
    private String model;
    @NotNull(message = "Год выпуска не может быть пустым")
    private Integer year;
    @NotBlank(message = "Тип кузова не может быть пустым")
    private String bodyType;
    @NotBlank(message = "Цвет не может быть пустым")
    private String color;
    @NotNull(message = "Тип трансмиссии не может быть пустым")
    private Transmission transmission;
    @NotNull(message = "Тип топлива не может быть пустым")
    private FuelType fuelType;
    @NotNull(message = "Мощность не может быть пустой")
    private int power;
    @NotNull(message = "Объём двигателя не может быть пустым")
    private double engineVolume;
    @NotNull(message = "Расход топлива не может быть пустым не может быть пустым")
    private double fuelConsumption;
    @NotNull(message = "Объём багажника не может быть пустым")
    private int trunkVolume;
    @NotNull(message = "Цена не может быть пустой")
    private double price;
    private DisplayUserDto userWhoOrdered; // Может быть null
}
