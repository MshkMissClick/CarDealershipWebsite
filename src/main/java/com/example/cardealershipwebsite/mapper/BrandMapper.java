package com.example.cardealershipwebsite.mapper;

import com.example.cardealershipwebsite.dto.BrandDto;
import com.example.cardealershipwebsite.model.Brand;
import org.springframework.stereotype.Component;

/** Component. */
@Component
public class BrandMapper {
    private final CarMapper carMapper;

    /** Mapper. */
    public BrandMapper(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    /** To dto. */
    public BrandDto toDto(Brand brand) {
        BrandDto brandDto = new BrandDto();
        brandDto.setId(brand.getId());
        brandDto.setName(brand.getName());
        brandDto.setCars(brand.getCars().stream().map(carMapper::toDto).toList());
        return brandDto;
    }
}
