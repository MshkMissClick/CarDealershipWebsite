package com.example.cardealershipwebsite.mapper;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.model.Car;
import org.springframework.stereotype.Component;

/** Car Mapper. */
@Component
public class CarMapper {

    /** To Dto.*/
    public CarDto toDto(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand());
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
        dto.setUserWhoOrderedId(car.getUserWhoOrdered() != null ? car.getUserWhoOrdered().getId() : null);
        return dto;
    }

    /** To Entity. */
    public Car toEntity(CarDto dto) {
        Car car = new Car();
        car.setBrand(dto.getBrand());
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
