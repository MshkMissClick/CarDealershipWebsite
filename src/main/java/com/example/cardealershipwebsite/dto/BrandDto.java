package com.example.cardealershipwebsite.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

/** Data. */
@Data
public class BrandDto {
    private Long id;
    @NotBlank(message = "Название бренда не может быть пустым")
    private String name;
    private List<CarDto> cars;
}
