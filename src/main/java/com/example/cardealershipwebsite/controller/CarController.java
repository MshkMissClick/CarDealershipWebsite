package com.example.cardealershipwebsite.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер. */
@RestController
@RequestMapping("/cars")
class CarController {

    /** Обработка запроса с параметрами brand и color. */
    @GetMapping
    public Map<String, String> getCarsByQuery(
            @RequestParam(required = false, defaultValue = "any") String brand,
            @RequestParam(required = false, defaultValue = "any") String color
    ) {
        return Map.of(
                "brand", brand,
                "color", color
        );
    }

    /** Обработка запроса с переменной пути brand. */
    @GetMapping("/{brand}")
    public Map<String, String> getCarByBrand(@PathVariable String brand) {
        return Map.of("brand", brand, "message", "Car details for " + brand);
    }
}