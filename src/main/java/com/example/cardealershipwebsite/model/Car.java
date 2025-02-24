package com.example.cardealershipwebsite.model;

import lombok.Getter;
import lombok.Setter;

/** Model. */
@Getter
@Setter
public class Car {
    private Long id;
    private String name;
    private String color;
    private double price;

    /** Constructor. */
    public Car(Long id, String name, String color, double price) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.price = price;
    }
}
