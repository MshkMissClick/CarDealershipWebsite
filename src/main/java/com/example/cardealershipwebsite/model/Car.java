package com.example.cardealershipwebsite.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Car Model. */
@Entity
@Table(name = "cars")
@Getter
@Setter
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String bodyType;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String fuelType;

    @Column(nullable = false)
    private int power;

    @Column(nullable = false)
    private double engineVolume;

    @Column(nullable = false)
    private double fuelConsumption;

    @Column(nullable = false)
    private int cylinders;

    @Column(nullable = false)
    private int maxSpeed;

    @Column(nullable = false)
    private double acceleration;

    @Column(nullable = false)
    private int trunkVolume;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantityInStock;

    @ManyToMany(mappedBy = "orders")
    private List<User> usersWhoOrdered;
}