package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.CarDto;
import com.example.cardealershipwebsite.dto.CarUpdateDto;
import com.example.cardealershipwebsite.dto.DisplayUserDto;
import com.example.cardealershipwebsite.mapper.CarMapper;
import com.example.cardealershipwebsite.model.Brand;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.Transmission;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.BrandRepository;
import com.example.cardealershipwebsite.repository.CarRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import static com.example.cardealershipwebsite.model.FuelType.GASOLINE;
import static com.example.cardealershipwebsite.model.Transmission.MANUAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarMapper carMapper;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private Map<String, List<CarDto>> carFilterCache;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCars() {
        List<Car> cars = List.of(new Car(), new Car());
        List<CarDto> carDtos = List.of(new CarDto(), new CarDto());

        when(carRepository.findAll()).thenReturn(cars);
        when(carMapper.toDto(any(Car.class))).thenReturn(new CarDto());

        List<CarDto> result = carService.getAllCars();

        assertEquals(2, result.size());
        verify(carRepository).findAll();
        verify(carMapper, times(2)).toDto(any(Car.class));
    }

    @Test
    void testGetCarById_Found() {
        Car car = new Car();
        CarDto carDto = new CarDto();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(carDto);

        Optional<CarDto> result = carService.getCarById(1L);

        assertTrue(result.isPresent());
        assertEquals(carDto, result.get());
    }

    @Test
    void testGetCarById_NotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CarDto> result = carService.getCarById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateCar_WithUser() {
        CarDto carDto = new CarDto();
        Car car = new Car();
        User user = new User();
        user.setId(1L);

        DisplayUserDto displayUserDto = new DisplayUserDto();
        displayUserDto.setId(1L); // если есть сеттер для ID

        carDto.setUserWhoOrdered(displayUserDto);

        when(carMapper.toEntity(carDto)).thenReturn(car);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(carDto);

        CarDto result = carService.createCar(carDto);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(carRepository).save(car);
    }

    @Test
    void testCreateCar_WithoutUser() {
        CarDto carDto = new CarDto();
        Car car = new Car();

        when(carMapper.toEntity(carDto)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(carDto);

        CarDto result = carService.createCar(carDto);

        assertNotNull(result);
        verify(carRepository).save(car);
    }

    @Test
    void testUpdateCar_ValidUpdate() {
        CarUpdateDto dto = new CarUpdateDto();
        dto.setModel("NewModel");
        Car car = new Car();
        car.setId(1L);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(new CarDto());

        CarDto result = carService.updateCar(1L, dto);

        assertNotNull(result);
        verify(carRepository).save(car);
    }

    @Test
    void testUpdateCar_NotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                carService.updateCar(1L, new CarUpdateDto()));
    }
    @Test
    void testDeleteCar_Existing() {
        Car car = new Car();
        car.setId(1L);
        Brand brand = new Brand();
        brand.setName("BrandName");
        car.setBrand(brand);
        car.setBodyType("Sedan");

        User user = new User();
        user.setOrders(new ArrayList<>(List.of(car))); // теперь список изменяемый
        car.setUserWhoOrdered(user);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(userRepository.findAllByFavoritesContains(car)).thenReturn(List.of());

        carService.deleteCar(1L);

        verify(carRepository).deleteById(1L);
        verify(userRepository).saveAll(anyList());
        verify(carFilterCache).remove("BrandName");
        verify(carFilterCache).remove("Sedan");
    }



    @Test
    void testGetUserIdWhoOrderedCar_WithUser() {
        Car car = new Car();
        User user = new User();
        user.setId(2L);
        car.setUserWhoOrdered(user);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Optional<Long> result = carService.getUserIdWhoOrderedCar(1L);

        assertTrue(result.isPresent());
        assertEquals(2L, result.get());
    }

    @Test
    void testFilterCarsByBrand_CacheHit() {
        List<CarDto> cached = List.of(new CarDto());

        when(carFilterCache.containsKey("Toyota")).thenReturn(true);
        when(carFilterCache.get("Toyota")).thenReturn(cached);

        List<CarDto> result = carService.filterCarsByBrand("Toyota");

        assertEquals(1, result.size());
        verify(carFilterCache).get("Toyota");
    }

    @Test
    void testFilterCarsByBrand_CacheMiss() {
        Car car = new Car();
        CarDto carDto = new CarDto();

        when(carFilterCache.containsKey("Toyota")).thenReturn(false);
        when(carRepository.findCarsByBrandName("Toyota")).thenReturn(List.of(car));
        when(carMapper.toDto(car)).thenReturn(carDto);

        List<CarDto> result = carService.filterCarsByBrand("Toyota");

        assertEquals(1, result.size());
        verify(carFilterCache).put("Toyota", List.of(carDto));
    }

    @Test
    void testFilterCarsByBodyType_CacheHit() {
        List<CarDto> cached = List.of(new CarDto());

        when(carFilterCache.containsKey("SUV")).thenReturn(true);
        when(carFilterCache.get("SUV")).thenReturn(cached);

        List<CarDto> result = carService.filterCarsByBodyType("SUV");

        assertEquals(1, result.size());
    }

    @Test
    void testFilterCarsByBodyType_CacheMiss() {
        Car car = new Car();
        CarDto carDto = new CarDto();

        when(carFilterCache.containsKey("SUV")).thenReturn(false);
        when(carRepository.findCarsByBodyType("SUV")).thenReturn(List.of(car));
        when(carMapper.toDto(car)).thenReturn(carDto);

        List<CarDto> result = carService.filterCarsByBodyType("SUV");

        assertEquals(1, result.size());
        verify(carFilterCache).put("SUV", List.of(carDto));
    }

    @Test
    void testClearCacheForValues_RemovesNonNullValues() {
        when(carFilterCache.remove("Toyota")).thenReturn(null);
        when(carFilterCache.remove("Sedan")).thenReturn(null);

        carServiceTestAccess_clearCacheForValues("Toyota", "Sedan");

        verify(carFilterCache).remove("Toyota");
        verify(carFilterCache).remove("Sedan");
    }

    @Test
    void testClearCacheForValues_SkipNullValues() {
        carServiceTestAccess_clearCacheForValues(null, null);

        verify(carFilterCache, never()).remove(anyString());
    }

    @Test
    void testClearCacheForCar_RemovesNonNullFields() {
        CarDto carDto = new CarDto();
        carDto.setBrand("BMW");
        carDto.setBodyType("Coupe");

        carServiceTestAccess_clearCacheForCar(carDto);

        verify(carFilterCache).remove("BMW");
        verify(carFilterCache).remove("Coupe");
    }

    @Test
    void testUpdateCarBasicAttributes_AllFields() {
        Car car = new Car();
        CarUpdateDto dto = new CarUpdateDto();
        dto.setBrand("Toyota");
        dto.setModel("Corolla");
        dto.setYear(2020);
        dto.setBodyType("Sedan");
        dto.setColor("Red");
        dto.setTransmission(MANUAL);
        dto.setFuelType(GASOLINE);

        when(brandRepository.findByName("Toyota")).thenReturn(new com.example.cardealershipwebsite.model.Brand());

        boolean result = carService.updateCarBasicAttributes(car, dto);

        assertTrue(result);
        assertEquals("Corolla", car.getModel());
        assertEquals(2020, car.getYear());
        assertEquals("Sedan", car.getBodyType());
        assertEquals("Red", car.getColor());
        assertEquals(MANUAL, car.getTransmission());
        assertEquals(GASOLINE, car.getFuelType());
    }

    @Test
    void testUpdateCarTechnicalAttributes_AllFields() {
        Car car = new Car();
        CarUpdateDto dto = new CarUpdateDto();
        dto.setPower(150);
        dto.setEngineVolume(2.0);
        dto.setFuelConsumption(8.0);
        dto.setTrunkVolume(400);
        dto.setPrice(20000);

        boolean result = carService.updateCarTechnicalAttributes(car, dto);

        assertTrue(result);
        assertEquals(150, car.getPower());
        assertEquals(2.0, car.getEngineVolume());
        assertEquals(8.0, car.getFuelConsumption());
        assertEquals(400, car.getTrunkVolume());
        assertEquals(20000, car.getPrice());
    }

    @Test
    void testDeleteCar_ClearsCacheForValuesCalled() {
        Car car = new Car();
        car.setId(42L);

        car.setBodyType("SUV");
        Brand brand = new Brand();
        brand.setName("Mazda");
        car.setBrand(brand);

        User user = new User();
        user.setOrders(new ArrayList<>(List.of(car)));
        car.setUserWhoOrdered(user);

        when(carRepository.findById(42L)).thenReturn(Optional.of(car));
        when(userRepository.findAllByFavoritesContains(car)).thenReturn(List.of());

        carService.deleteCar(42L);

        verify(carRepository).deleteById(42L);
        verify(carFilterCache).remove("Mazda");
        verify(carFilterCache).remove("SUV");
    }

    void carServiceTestAccess_clearCacheForValues(String brand, String body) {
        try {
            Method method = CarService.class.getDeclaredMethod("clearCacheForValues", String.class, String.class);
            method.setAccessible(true);
            method.invoke(carService, brand, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void carServiceTestAccess_clearCacheForCar(CarDto carDto) {
        try {
            Method method = CarService.class.getDeclaredMethod("clearCacheForCar", CarDto.class);
            method.setAccessible(true);
            method.invoke(carService, carDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
