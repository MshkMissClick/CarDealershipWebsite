package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.mapper.CarMapper;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.CarRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public Optional<CarDto> updateCar(Long id, CarDto carDto) {
        return carRepository.findById(id).map(existingCar -> {
            updateCarAttributes(existingCar, carDto);
            carRepository.save(existingCar);
            return carMapper.toDto(existingCar);
        });
    }

    /** Удалить машину по ID. */
    @Transactional
    public void deleteCar(Long id) {
        carRepository.findById(id).ifPresent(car -> {
            // Удаляем машину из заказов всех пользователей
            car.getUsersWhoOrdered().forEach(user -> user.getOrders().remove(car));
            carRepository.delete(car); // Теперь удаляем саму машину
        });
    }

    /** Получение айди юзера. */
    public List<Long> getUserIdsWithCarInOrders(Long carId) {
        return userRepository.findAll().stream()  // Ищем всех пользователей
                .filter(user -> user.getOrders().stream()  // Фильтруем пользователей, у которых есть эта машина в заказах
                        .anyMatch(car -> car.getId().equals(carId)))
                .map(User::getId)  // Возвращаем только ID пользователей
                .toList();
    }

    /** Обновление атрибутов машины. */
    private void updateCarAttributes(Car car, CarDto carDto) {
        if (carDto.getName() != null) {
            car.setName(carDto.getName());
        }

        if (carDto.getBodyType() != null) {
            car.setBodyType(carDto.getBodyType());
        }

        if (carDto.getColor() != null) {
            car.setColor(carDto.getColor());
        }

        if (carDto.getFuelType() != null) {
            car.setFuelType(carDto.getFuelType());
        }

        if (carDto.getPower() != 0) {
            car.setPower(carDto.getPower());
        }

        if (carDto.getEngineVolume() != 0) {
            car.setEngineVolume(carDto.getEngineVolume());
        }

        if (carDto.getFuelConsumption() != 0) {
            car.setFuelConsumption(carDto.getFuelConsumption());
        }

        if (carDto.getCylinders() != 0) {
            car.setCylinders(carDto.getCylinders());
        }

        if (carDto.getMaxSpeed() != 0) {
            car.setMaxSpeed(carDto.getMaxSpeed());
        }

        if (carDto.getAcceleration() != 0) {
            car.setAcceleration(carDto.getAcceleration());
        }

        if (carDto.getTrunkVolume() != 0) {
            car.setTrunkVolume(carDto.getTrunkVolume());
        }

        if (carDto.getPrice() != 0) {
            car.setPrice(carDto.getPrice());
        }

        if (carDto.getQuantityInStock() != 0) {
            car.setQuantityInStock(carDto.getQuantityInStock());
        }

        if (carDto.getUsersWhoOrderedIds() != null) {
            List<User> users = userRepository.findAllById(carDto.getUsersWhoOrderedIds());
            car.setUsersWhoOrdered(users);
        }
    }
}