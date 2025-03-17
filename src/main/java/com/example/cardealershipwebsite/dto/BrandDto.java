package com.example.cardealershipwebsite.dto;

import lombok.Data;

import java.util.List;

@Data
public class BrandDto {
    private Long id;
    private String name;
    private List<CarDto> cars;
}
