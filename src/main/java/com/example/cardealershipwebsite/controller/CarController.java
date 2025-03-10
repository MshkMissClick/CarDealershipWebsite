package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.service.CarService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long id, @RequestBody CarDto carDto) {
        return ResponseEntity.of(carService.updateCar(id, carDto));
    }

    /** Удалить машину. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    /** Получить список ID пользователей, у которых эта машина в заказах. */
    @GetMapping("/{id}/users")
    public ResponseEntity<List<Long>> getUserIdsWithCarInOrders(@PathVariable Long id) {
        List<Long> userIds = carService.getUserIdsWithCarInOrders(id);
        return ResponseEntity.ok(userIds);
    }


}
