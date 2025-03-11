package com.example.cardealershipwebsite.mapper;

import com.example.cardealershipwebsite.dto.UserDto;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.CarRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/** UserMapper. */
@Component
public class UserMapper {

    private final CarRepository carRepository;

    /** Юзер Маппер. */
    public UserMapper(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /** Юзер ДТО. */
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPasswordHash(user.getPasswordHash());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getOrders() != null) {
            dto.setOrderCarIds(user.getOrders().stream()
                    .map(Car::getId)
                    .toList());
        } else {
            dto.setOrderCarIds(Collections.emptyList());
        }

        if (user.getFavorites() != null) {
            dto.setFavoriteCarIds(user.getFavorites().stream()
                    .map(Car::getId)
                    .toList());
        } else {
            dto.setFavoriteCarIds(Collections.emptyList());
        }

        return dto;
    }

    /** ToEntity. */
    public User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPasswordHash());
        user.setCreatedAt(dto.getCreatedAt());

        // Здесь мы используем репозиторий для поиска машин по их id
        List<Car> orders = carRepository.findAllById(dto.getOrderCarIds()); // Загружаем все автомобили по списку id
        user.setOrders(orders);

        List<Car> favorites = carRepository.findAllById(dto.getFavoriteCarIds()); // Загружаем все автомобили по списку id
        user.setFavorites(favorites);

        return user;
    }
}
