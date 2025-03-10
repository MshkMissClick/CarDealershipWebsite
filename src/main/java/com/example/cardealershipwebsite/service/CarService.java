package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.mapper.CarMapper;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.CarRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/** Car Service. */
@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final UserRepository userRepository;

    /** Конструктор. */
    public CarService(CarRepository carRepository, CarMapper carMapper, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.userRepository = userRepository;
    }

    /** Получить все машины в виде DTO. */
    public List<CarDto> getAllCars() {
        return carRepository.findAll()
                .stream()
                .map(carMapper::toDto)
                .toList();
    }

    /** Получить машину по ID. */
    public Optional<CarDto> getCarById(Long id) {
        return carRepository.findById(id).map(carMapper::toDto);
    }

    /** Создать машину. */
    public CarDto createCar(CarDto carDto) {
        Car car = carMapper.toEntity(carDto);
        return carMapper.toDto(carRepository.save(car));
    }

    /** Обновить данные машины. */
    public Optional<CarDto> updateCar(Long id, CarDto carDto) {
        if (!carRepository.existsById(id)) {
            return Optional.empty(); // Если машина с таким ID не существует
        }
        Car car = carMapper.toEntity(carDto);
        car.setId(id);
        return Optional.of(carMapper.toDto(carRepository.save(car)));
    }

    /** Удалить машину по ID. */
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    /** Получение айди юзера. */
    public List<Long> getUserIdsWithCarInOrders(Long carId) {
        return userRepository.findAll().stream()  // Ищем всех пользователей
                .filter(user -> user.getOrders().stream()  // Фильтруем пользователей, у которых есть эта машина в заказах
                        .anyMatch(car -> car.getId().equals(carId)))
                .map(User::getId)  // Возвращаем только ID пользователей
                .toList();
    }
}
