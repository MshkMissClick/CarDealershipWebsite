package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.repository.CarRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**Car service.*/
@Service
public class CarService {

    private final CarRepository carRepository;

    /**Конструктор.*/
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**Получить всех машин.*/
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**Получить машину по ID.*/
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    /**Создать новую машину.*/
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    /**Обновить данные машины.*/
    public Optional<Car> updateCar(Long id, Car car) {
        if (!carRepository.existsById(id)) {
            return Optional.empty();  // Если машина с таким ID не существует
        }
        car.setId(id);  // Установить ID для обновления существующей машины
        return Optional.of(carRepository.save(car));
    }

    /**Удалить машину по ID.*/
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}
