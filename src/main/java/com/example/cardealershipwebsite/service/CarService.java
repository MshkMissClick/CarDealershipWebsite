package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.mapper.CarMapper;
import com.example.cardealershipwebsite.model.Brand;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.BrandRepository;
import com.example.cardealershipwebsite.repository.CarRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Car Service. */
@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;
    private final BrandRepository brandRepository;
    private final Map<String, List<CarDto>> carFilterCache;

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
        if (carDto.getUserWhoOrdered() != null) {
            User user = userRepository.findById(carDto.getUserWhoOrdered().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            car.setUserWhoOrdered(user);
        }
        CarDto savedCar = carMapper.toDto(carRepository.save(car));

        log.info("[CACHE]: New car created: {}. Clearing affected cache.", savedCar);
        clearCacheForCar(savedCar);

        return savedCar;
    }

    /** Обновление машины. */
    public Optional<CarDto> updateCar(Long id, CarDto carDto) {
        return carRepository.findById(id).map(car -> {
            // Сохраняем старые значения для очистки кэша
            String oldBrand = car.getBrand() != null ? car.getBrand().getName() : null;
            String oldBodyType = car.getBodyType();

            updateCarAttributes(car, carDto);
            CarDto updatedCar = carMapper.toDto(carRepository.save(car));

            log.info("[CACHE]: Car updated (ID={}): {}. Clearing affected cache.", id, updatedCar);
            clearCacheForValues(oldBrand, oldBodyType);
            clearCacheForCar(updatedCar);

            return updatedCar;
        });
    }

    /** Удаление машины. */
    public void deleteCar(Long id) {
        carRepository.findById(id).ifPresent(car -> {
            String brandName = car.getBrand() != null ? car.getBrand().getName() : null;
            String bodyType = car.getBodyType();

            if (car.getUserWhoOrdered() != null) {
                car.getUserWhoOrdered().getOrders().remove(car);
            }

            List<User> usersWithCarInFavorites = userRepository.findAllByFavoritesContains(car);
            usersWithCarInFavorites.forEach(user -> user.getFavorites().remove(car));
            userRepository.saveAll(usersWithCarInFavorites);

            carRepository.deleteById(id);
            log.info("[CACHE]: Car deleted (ID={}). Clearing affected cache.", id);
            clearCacheForValues(brandName, bodyType);
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
            Brand brand = brandRepository.findByName(carDto.getBrand());
            if (brand != null) {
                car.setBrand(brand);
            }
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

    /** Filter by brand. */
    public List<CarDto> filterCarsByBrand(String brandName) {
        if (carFilterCache.containsKey(brandName)) {
            log.info("[CACHE]: Cache hit for filter: brand='{}'", brandName);
            return carFilterCache.get(brandName);
        }

        log.info("[CACHE]: Cache miss for filter: brand='{}'. Querying DB.", brandName);

        List<CarDto> filteredCars = carRepository.findCarsByBrandName(brandName)
                .stream()
                .map(carMapper::toDto)
                .toList();

        carFilterCache.put(brandName, filteredCars);
        log.info("[CACHE]: Cache populated for filter: brand='{}'", brandName);

        return filteredCars;
    }

    /** Filter by body type. */
    public List<CarDto> filterCarsByBodyType(String bodyType) {
        if (carFilterCache.containsKey(bodyType)) {
            log.info("[CACHE]: Cache hit for filter: bodyType='{}'", bodyType);
            return carFilterCache.get(bodyType);
        }

        log.info("[CACHE]: Cache miss for filter: bodyType='{}'. Querying DB.", bodyType);

        List<CarDto> filteredCars = carRepository.findCarsByBodyType(bodyType)
                .stream()
                .map(carMapper::toDto)
                .toList();

        carFilterCache.put(bodyType, filteredCars);
        log.info("[CACHE]: Cache populated for filter: bodyType='{}'", bodyType);

        return filteredCars;
    }

    /** Очистка кэша по данным машины. */
    private void clearCacheForCar(CarDto carDto) {
        if (carDto.getBrand() != null) {
            carFilterCache.remove(carDto.getBrand());
        }
        if (carDto.getBodyType() != null) {
            carFilterCache.remove(carDto.getBodyType());
        }
    }

    /** Очистка кэша по конкретным значениям. */
    private void clearCacheForValues(String brandName, String bodyType) {
        if (brandName != null) {
            carFilterCache.remove(brandName);
        }
        if (bodyType != null) {
            carFilterCache.remove(bodyType);
        }
    }
}
