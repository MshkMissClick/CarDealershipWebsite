package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.BrandDto;
import com.example.cardealershipwebsite.mapper.BrandMapper;
import com.example.cardealershipwebsite.model.Brand;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.BrandRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Brand repos. */
@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandMapper brandMapper;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    public List<BrandDto> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDto).toList();
    }

    /** Get brand by id. */
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    /** Create brand. */
    public Brand createBrand(Brand brand) {
        Brand existingBrand = brandRepository.findByName(brand.getName());
        if (existingBrand != null) {
            return null;
        }
        return brandRepository.save(brand);
    }

    /** Delete brand. */
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));

        List<Car> cars = brand.getCars();

        for (Car car : cars) {
            User user = car.getUserWhoOrdered();
            if (user != null) {
                user.getOrders().remove(car);
            }

            List<User> users = userRepository.findAll();
            for (User u : users) {
                u.getFavorites().remove(car);
            }
        }

        brandRepository.delete(brand);
    }
}