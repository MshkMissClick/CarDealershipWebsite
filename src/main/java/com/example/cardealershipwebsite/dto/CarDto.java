package com.example.cardealershipwebsite.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Car DTO. */
@Getter
@Setter
public class CarDto {
    private Long id;
    private String name;
    private String bodyType;
    private String color;
    private String fuelType;
    private int power;
    private double engineVolume;
    private double fuelConsumption;
    private int cylinders;
    private int maxSpeed;
    private double acceleration;
    private int trunkVolume;
    private double price;
    private int quantityInStock;

    private List<Long> usersWhoOrderedIds; // id пользователей, которые заказали эту машину
}
