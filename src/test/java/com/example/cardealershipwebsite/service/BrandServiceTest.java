package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.BrandDto;
import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.mapper.BrandMapper;
import com.example.cardealershipwebsite.model.Brand;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.BrandRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BrandMapper brandMapper;

    @Mock
    private Map<String, List<CarDto>> carFilterCache;

    @InjectMocks
    private BrandService brandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBrands() {
        Brand brand = new Brand();
        BrandDto brandDto = new BrandDto();

        when(brandRepository.findAll()).thenReturn(List.of(brand));
        when(brandMapper.toDto(brand)).thenReturn(brandDto);

        List<BrandDto> result = brandService.getAllBrands();

        assertEquals(1, result.size());
        verify(brandRepository).findAll();
        verify(brandMapper).toDto(brand);
    }

    @Test
    void testGetBrandById_Found() {
        Brand brand = new Brand();
        brand.setId(1L);

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        Brand result = brandService.getBrandById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetBrandById_NotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        Brand result = brandService.getBrandById(1L);
        assertNull(result);
    }

    @Test
    void testCreateBrand_WhenBrandExists() {
        Brand brand = new Brand();
        brand.setName("Toyota");

        when(brandRepository.findByName("Toyota")).thenReturn(brand);

        Brand result = brandService.createBrand(brand);
        assertNull(result);
    }

    @Test
    void testCreateBrand_Success() {
        Brand brand = new Brand();
        brand.setName("Honda");

        when(brandRepository.findByName("Honda")).thenReturn(null);
        when(brandRepository.save(brand)).thenReturn(brand);

        Brand result = brandService.createBrand(brand);
        assertEquals(brand, result);
    }

    @Test
    void testDeleteBrand_BrandNotFound() {
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                brandService.deleteBrand(1L));
        assertEquals("Brand not found", exception.getMessage());
    }

    @Test
    void testDeleteBrand_WithOrdersAndFavorites() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("BMW");

        Car car = new Car();
        car.setBodyType("Sedan");
        car.setBrand(brand);

        User user = new User();
        user.setOrders(new ArrayList<>(List.of(car)));

        car.setUserWhoOrdered(user);
        brand.setCars(List.of(car));

        User u1 = new User();
        u1.setFavorites(new ArrayList<>(List.of(car)));

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(userRepository.findAll()).thenReturn(List.of(u1));

        brandService.deleteBrand(1L);

        assertFalse(user.getOrders().contains(car));
        assertFalse(u1.getFavorites().contains(car));

        verify(brandRepository).delete(brand);
        verify(carFilterCache).remove("BMW");
        verify(carFilterCache).remove("Sedan");
    }
}
