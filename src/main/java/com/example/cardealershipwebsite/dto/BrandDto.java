package com.example.cardealershipwebsite.dto;

import java.util.List;
import lombok.Data;

/** Data. */
@Data
public class BrandDto {
    private Long id;
    private String name;
    private List<CarDto> cars;
}
