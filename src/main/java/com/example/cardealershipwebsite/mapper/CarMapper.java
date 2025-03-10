package com.example.cardealershipwebsite.mapper;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import org.springframework.stereotype.Component;

/** Car Mapper. */
@Component
public class CarMapper {

    /** Преобразование Car в CarDto. */
    public CarDto toDto(Car car) {
        if (car == null) {
            return null;
        }
        CarDto carDto = new CarDto();
        carDto.setId(car.getId());
        carDto.setName(car.getName());
        carDto.setBodyType(car.getBodyType());
        carDto.setColor(car.getColor());
        carDto.setFuelType(car.getFuelType());
        carDto.setPower(car.getPower());
        carDto.setEngineVolume(car.getEngineVolume());
        carDto.setFuelConsumption(car.getFuelConsumption());
        carDto.setCylinders(car.getCylinders());
        carDto.setMaxSpeed(car.getMaxSpeed());
        carDto.setAcceleration(car.getAcceleration());
        carDto.setTrunkVolume(car.getTrunkVolume());
        carDto.setPrice(car.getPrice());
        carDto.setQuantityInStock(car.getQuantityInStock());

        // Добавляем новые поля
        if (car.getUsersWhoOrdered() != null) {
            carDto.setUsersWhoOrderedIds(
                    car.getUsersWhoOrdered().stream()
                            .map(User::getId)
                            .toList()
            ); // id пользователей, которые заказали эту машину
        }
        return carDto;
    }

    /** Преобразование CarDto в Car. */
    public Car toEntity(CarDto carDto) {
        if (carDto == null) {
            return null;
        }
        Car car = new Car();
        car.setId(carDto.getId());
        car.setName(carDto.getName());
        car.setBodyType(carDto.getBodyType());
        car.setColor(carDto.getColor());
        car.setFuelType(carDto.getFuelType());
        car.setPower(carDto.getPower());
        car.setEngineVolume(carDto.getEngineVolume());
        car.setFuelConsumption(carDto.getFuelConsumption());
        car.setCylinders(carDto.getCylinders());
        car.setMaxSpeed(carDto.getMaxSpeed());
        car.setAcceleration(carDto.getAcceleration());
        car.setTrunkVolume(carDto.getTrunkVolume());
        car.setPrice(carDto.getPrice());
        car.setQuantityInStock(carDto.getQuantityInStock());

        // Для преобразования из userId в User объект, можно будет использовать userId для поиска в базе
        // и добавление объекта пользователя в Car.
        // Для поля usersWhoOrdered можно будет также добавить соответствующих пользователей.

        return car;
    }
}
