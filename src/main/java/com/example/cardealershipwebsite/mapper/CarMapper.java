package com.example.cardealershipwebsite.mapper;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.dto.DisplayCarDto;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.repository.BrandRepository;
import org.springframework.stereotype.Component;

/** Car Mapper. */
@Component
public class CarMapper {

    private final UserMapper userMapper;
    private final BrandRepository brandRepository;

    /**Конструктор.*/
    public CarMapper(UserMapper userMapper, BrandRepository brandRepository) {
        this.userMapper = userMapper;
        this.brandRepository = brandRepository;
    }

    /** To Dto.*/
    public CarDto toDto(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand() != null ? car.getBrand().getName() : "Unknown");
        dto.setModel(car.getModel());
        dto.setYear(car.getYear());
        dto.setBodyType(car.getBodyType());
        dto.setColor(car.getColor());
        dto.setTransmission(car.getTransmission());
        dto.setFuelType(car.getFuelType());
        dto.setPower(car.getPower());
        dto.setEngineVolume(car.getEngineVolume());
        dto.setFuelConsumption(car.getFuelConsumption());
        dto.setTrunkVolume(car.getTrunkVolume());
        dto.setPrice(car.getPrice());
        if (car.getUserWhoOrdered() != null) {
            dto.setUserWhoOrdered(userMapper.toDisplayUserDto(car.getUserWhoOrdered()));
        }
        return dto;
    }

    /** ff.*/
    public DisplayCarDto toDisplayCarDto(Car car) {
        DisplayCarDto dto = new DisplayCarDto();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand() != null ? car.getBrand().getName() : "Unknown");
        dto.setModel(car.getModel());
        return dto;
    }

    /** To Entity. */
    public Car toEntity(CarDto dto) {
        Car car = new Car();
        car.setBrand(brandRepository.findByName(dto.getBrand()));

        if (car.getBrand() == null) {
            return null;
        }
        car.setModel(dto.getModel());
        car.setYear(dto.getYear());
        car.setBodyType(dto.getBodyType());
        car.setColor(dto.getColor());
        car.setTransmission(dto.getTransmission());
        car.setFuelType(dto.getFuelType());
        car.setPower(dto.getPower());
        car.setEngineVolume(dto.getEngineVolume());
        car.setFuelConsumption(dto.getFuelConsumption());
        car.setTrunkVolume(dto.getTrunkVolume());
        car.setPrice(dto.getPrice());
        return car;
    }
}
