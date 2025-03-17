package com.example.cardealershipwebsite.mapper;

import com.example.cardealershipwebsite.dto.BrandDto;
import com.example.cardealershipwebsite.model.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {
    private final CarMapper carMapper;

    public BrandMapper(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    public BrandDto toDto(Brand brand) {
        BrandDto brandDto = new BrandDto();
        brandDto.setId(brand.getId());
        brandDto.setName(brand.getName());
        brandDto.setCars(brand.getCars().stream().map(carMapper::toDto).toList());
        return brandDto;
    }
}
