package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.dto.CarUpdateDto;
import com.example.cardealershipwebsite.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Car Controller. */
@Validated
@RestController
@RequestMapping("/cars")
@Tag(name = "Cars", description = "Управление автомобилями")
public class CarController {

    private final CarService carService;

    /** Конструктор. */
    public CarController(CarService carService) {
        this.carService = carService;
    }

    /** Получить все машины. */
    @Operation(summary = "Получить список всех автомобилей", description = "Возвращает список всех автомобилей в системе.")
    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    /** Получить машину по ID. */
    @Operation(summary = "Получить автомобиль по ID", description = "Возвращает информацию об автомобиле по его идентификатору.")
    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCarById(
            @Parameter(description = "ID автомобиля", required = true)
            @PathVariable @Min(1) Long id
    ) {
        return ResponseEntity.of(carService.getCarById(id));
    }

    /** Создать машину. */
    @Operation(summary = "Создать новый автомобиль", description = "Добавляет новый автомобиль в систему.")
    @PostMapping
    public ResponseEntity<CarDto> createCar(
            @Parameter(description = "Данные нового автомобиля", required = true)
            @Valid @RequestBody CarDto carDto
    ) {
        return ResponseEntity.ok(carService.createCar(carDto));
    }

    /** Обновить машину. */
    @Operation(summary = "Обновить данные автомобиля", description = "Обновляет информацию об автомобиле по его ID.")
    @PatchMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(
            @Parameter(description = "ID автомобиля", required = true) @PathVariable Long id,
            @Parameter(description = "Обновленные данные автомобиля", required = true) @RequestBody CarUpdateDto carDto
    ) {
        CarDto updatedCar = carService.updateCar(id, carDto);
        return ResponseEntity.ok(updatedCar);
    }


    /** Удалить машину. */
    @Operation(summary = "Удалить автомобиль", description = "Удаляет автомобиль из системы по его ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(
            @Parameter(description = "ID автомобиля", required = true)
            @PathVariable @Min(1) Long id
    ) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    /** Получить ID пользователя, у которого эта машина в заказах. */
    @Operation(summary = "Получить ID пользователя, заказавшего автомобиль",
            description = "Возвращает ID пользователя, который заказал данный автомобиль.")
    @GetMapping("/{id}/user")
    public ResponseEntity<Long> getUserIdsWithCarInOrders(
            @Parameter(description = "ID автомобиля", required = true)
            @PathVariable @Min(1) Long id
    ) {
        Long userId = carService.getUserIdWhoOrderedCar(id).orElse(null);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userId);
    }

    /** Filter by brand. */
    @Operation(summary = "Фильтрация автомобилей по бренду", description = "Возвращает список автомобилей указанного бренда.")
    @GetMapping("/filter-by-brand")
    public List<CarDto> filterCarsByBrand(
            @Parameter(description = "Название бренда")
            @RequestParam(required = false) @NotBlank(message = "не должно быть пустым") String brandName
    ) {
        return carService.filterCarsByBrand(brandName);
    }

    /** Filter by body type. */
    @Operation(summary = "Фильтрация автомобилей по типу кузова", description = "Возвращает список автомобилей указанного типа кузова.")
    @GetMapping("/filter-by-bodytype")
    public List<CarDto> filterCarsByBodyType(
            @Parameter(description = "Тип кузова")
            @RequestParam(required = false) @NotBlank(message = "не должно быть пустым") String bodyType
    ) {
        return carService.filterCarsByBodyType(bodyType);
    }
}
