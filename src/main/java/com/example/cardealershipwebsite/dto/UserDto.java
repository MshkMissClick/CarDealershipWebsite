package com.example.cardealershipwebsite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

/** User Dto. */
@Data
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Почта не должна быть пустой")
    private String email;
    @JsonProperty
    @NotBlank(message = "Хеш пароля не может быть пустым")
    private String passwordHash;
    private List<DisplayCarDto> orderCars;
    private List<DisplayCarDto> favoriteCars;
}
