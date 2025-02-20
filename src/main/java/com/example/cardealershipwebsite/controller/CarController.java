package com.example.cardealershipwebsite.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
class CarController {

    /** nothing. */
    @GetMapping
    public Map<String, String> getCarsByQuery(@RequestParam(required = false) String brand,
                                              @RequestParam(required = false) String color) {
        return Map.of(
                "brand", brand != null ? brand : "any",
                "color", color != null ? color : "any"
        );
    }

    /** nothing. */
    @GetMapping("/{brand}")
    public Map<String, String> getCarByBrand(@PathVariable String brand) {
        return Map.of("brand", brand, "message", "Car details for " + brand);
    }

}