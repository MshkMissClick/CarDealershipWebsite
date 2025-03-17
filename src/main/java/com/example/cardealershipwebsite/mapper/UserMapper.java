package com.example.cardealershipwebsite.mapper;

import com.example.cardealershipwebsite.dto.DisplayUserDto;
import com.example.cardealershipwebsite.dto.UserDto;
import com.example.cardealershipwebsite.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


/** User Mapper. */
@Component
public class UserMapper {

    private final CarMapper carMapper;

    /**f.*/
    public UserMapper(@Lazy CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    /** To Dto. */
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPasswordHash(user.getPasswordHash());
        dto.setOrderCars(user.getOrders().stream().map(carMapper::toDisplayCarDto).toList());
        dto.setFavoriteCars(user.getFavorites().stream().map(carMapper::toDisplayCarDto).toList());
        return dto;
    }

    /**fsf.  */
    public DisplayUserDto toDisplayUserDto(User user) {
        DisplayUserDto dto = new DisplayUserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }
}
