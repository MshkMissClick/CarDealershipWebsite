package com.example.cardealershipwebsite.dto;

import lombok.Data;

/** Dto для обновления данных пользователя. */
@Data
public class UserUpdateDto {
    private String name;
    private String email;
    private String passwordHash;
}