package com.example.cardealershipwebsite.mapper;

import com.example.cardealershipwebsite.dto.UserDto;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import org.springframework.stereotype.Component;


/** User Mapper. */
@Component
public class UserMapper {

    /** To Dto. */
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPasswordHash(user.getPasswordHash());
        dto.setOrderCarIds(user.getOrders().stream().map(Car::getId).toList());
        dto.setFavoriteCarIds(user.getFavorites().stream().map(Car::getId).toList());
        return dto;
    }
}
