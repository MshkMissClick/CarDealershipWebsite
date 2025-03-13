package com.example.cardealershipwebsite.dto;

import java.util.List;
import lombok.Data;

/** User Dto. */
@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private List<Long> orderCarIds;
    private List<Long> favoriteCarIds;
}
