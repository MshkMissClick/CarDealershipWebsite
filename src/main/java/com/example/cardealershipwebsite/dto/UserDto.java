package com.example.cardealershipwebsite.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** UserDto. */
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String passwordHash; // Можно убрать, если не нужен в DTO
    private LocalDateTime createdAt;
    private List<Long> orderCarIds;
    private List<Long> favoriteCarIds;
}
