package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.service.CarService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Car Controller. */
@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    /** Конструктор. */
    public CarController(CarService carService) {
        this.carService = carService;
    }

    /** Получить все машины. */
    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    /** Получить машину по ID. */
    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long id) {
        return ResponseEntity.of(carService.getCarById(id));
    }

    /** Создать машину. */
    @PostMapping
    public ResponseEntity<CarDto> createCar(@RequestBody CarDto carDto) {
        return ResponseEntity.ok(carService.createCar(carDto));
    }

    /** Обновить машину. */
    @PatchMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long id, @RequestBody CarDto carDto) {
        return ResponseEntity.of(carService.updateCar(id, carDto));
    }

    /** Удалить машину. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    /** Получить ID пользователя, у которого эта машина в заказах. */
    @GetMapping("/{id}/user")
    public ResponseEntity<Long> getUserIdsWithCarInOrders(@PathVariable Long id) {
        Long userId = carService.getUserIdWhoOrderedCar(id).orElse(null);
        if (userId == null) {
            return ResponseEntity.notFound().build(); // Если пользователь не найден
        }
        return ResponseEntity.ok(userId);
    }

    @GetMapping("/filter")
    public List<CarDto> filterCars(
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) String bodyType
    ) {
        return carService.filterCars(brandName, bodyType);
    }

}
