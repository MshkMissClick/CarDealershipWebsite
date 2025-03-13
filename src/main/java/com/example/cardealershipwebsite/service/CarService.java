package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.mapper.CarMapper;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.CarRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Car Service. */
@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;

    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(carMapper::toDto).toList();
    }

    /** Получение машин по айди. */
    public Optional<CarDto> getCarById(Long id) {
        return carRepository.findById(id).map(carMapper::toDto);
    }

    /** Создание машины. */
    public CarDto createCar(CarDto carDto) {
        Car car = carMapper.toEntity(carDto);
        if (carDto.getUserWhoOrderedId() != null) {
            User user = userRepository.findById(carDto.getUserWhoOrderedId()).orElseThrow(() -> new RuntimeException("User not found"));
            car.setUserWhoOrdered(user);
        }
        return carMapper.toDto(carRepository.save(car));
    }

    /** Обновление машины. */
    public Optional<CarDto> updateCar(Long id, CarDto carDto) {
        return carRepository.findById(id).map(car -> {
            updateCarAttributes(car, carDto);
            return carMapper.toDto(carRepository.save(car));
        });
    }

    /** Удаление машины. */
    public void deleteCar(Long id) {
        carRepository.findById(id).ifPresent(car -> {
            // Убираем машину из заказов пользователей
            if (car.getUserWhoOrdered() != null) {
                car.getUserWhoOrdered().getOrders().remove(car);
            }

            // Убираем машину из избранного у всех пользователей
            List<User> usersWithCarInFavorites = userRepository.findAllByFavoritesContains(car);
            usersWithCarInFavorites.forEach(user -> user.getFavorites().remove(car));
            userRepository.saveAll(usersWithCarInFavorites);

            carRepository.deleteById(id);
        });
    }

    /** Получает ID пользователя, заказавшего указанную машину. */
    public Optional<Long> getUserIdWhoOrderedCar(Long carId) {
        return carRepository.findById(carId)
                .map(car -> car.getUserWhoOrdered() != null ? car.getUserWhoOrdered().getId() : null);
    }

    /** Обновление атрибутов машины. */
    private void updateCarAttributes(Car car, CarDto carDto) {
        if (carDto.getBrand() != null) {
            car.setBrand(carDto.getBrand());
        }
        if (carDto.getModel() != null) {
            car.setModel(carDto.getModel());
        }
        if (carDto.getYear() != null) {
            car.setYear(carDto.getYear());
        }
        if (carDto.getBodyType() != null) {
            car.setBodyType(carDto.getBodyType());
        }
        if (carDto.getColor() != null) {
            car.setColor(carDto.getColor());
        }
        if (carDto.getTransmission() != null) {
            car.setTransmission(carDto.getTransmission());
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
        if (carDto.getTrunkVolume() != 0) {
            car.setTrunkVolume(carDto.getTrunkVolume());
        }
        if (carDto.getPrice() != 0) {
            car.setPrice(carDto.getPrice());
        }
    }
}
