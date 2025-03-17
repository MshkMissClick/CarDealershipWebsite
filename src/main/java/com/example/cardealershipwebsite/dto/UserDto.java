package com.example.cardealershipwebsite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/** User Dto. */
@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    @JsonProperty
    private String passwordHash;
    private List<DisplayCarDto> orderCars;
    private List<DisplayCarDto> favoriteCars;
}
