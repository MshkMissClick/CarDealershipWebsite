package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.UserDto;
import com.example.cardealershipwebsite.dto.UserUpdateDto;
import com.example.cardealershipwebsite.exception.PasswordHashingException;
import com.example.cardealershipwebsite.exception.UserUpdateException;
import com.example.cardealershipwebsite.mapper.UserMapper;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.CarRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private CarRepository carRepository;
    @Mock private UserMapper userMapper;

    @InjectMocks private UserService userService;

    private final User user = new User();
    private final Car car = new Car();
    private final UserDto userDto = new UserDto();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setPasswordHash("hashed");

        car.setId(10L);

        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setName("Test");
        userDto.setPasswordHash("password");
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        var result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));
    }

    @Test
    void getUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        var result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(userDto, result.get());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        var result = userService.getUserById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void createUser_success() throws NoSuchAlgorithmException {
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toDto(any())).thenReturn(userDto);

        UserDto created = userService.createUser(userDto);

        assertEquals(userDto, created);
    }

    @Test
    void updateUser_success() {
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setEmail("new@mail.com");
        updateDto.setName("Updated");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto updated = userService.updateUser(1L, updateDto);

        assertEquals(userDto, updated);
    }

    @Test
    void updateUser_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserUpdateException.class,
                () -> userService.updateUser(1L, new UserUpdateDto()));
    }

    @Test
    void updateUser_blankEmail() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setEmail("");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(UserUpdateException.class, () -> userService.updateUser(1L, dto));
    }

    @Test
    void updateUser_passwordHashError() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setPasswordHash("error");

        try (MockedStatic<MessageDigest> mdMock = mockStatic(MessageDigest.class)) {
            mdMock.when(() -> MessageDigest.getInstance("SHA-256")).thenThrow(NoSuchAlgorithmException.class);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThrows(PasswordHashingException.class, () -> userService.updateUser(1L, dto));
        }
    }

    @Test
    void deleteUser_success() {
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void getUserOrderCarIds_found() {
        user.setOrders(List.of(car));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var result = userService.getUserOrderCarIds(1L);

        assertEquals(List.of(10L), result);
    }

    @Test
    void getUserOrderCarIds_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertTrue(userService.getUserOrderCarIds(1L).isEmpty());
    }

    @Test
    void addCarToOrders_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));
        when(userMapper.toDto(user)).thenReturn(userDto);

        var result = userService.addCarToOrders(1L, 10L);

        assertTrue(result.isPresent());
        verify(carRepository).save(car);
        verify(userRepository).save(user);
    }

    @Test
    void addCarToOrders_carAlreadyOrdered() {
        car.setUserWhoOrdered(new User());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));

        assertThrows(IllegalStateException.class, () -> userService.addCarToOrders(1L, 10L));
    }

    @Test
    void removeCarFromOrders_success() {
        user.getOrders().add(car);
        car.setUserWhoOrdered(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));
        when(userMapper.toDto(user)).thenReturn(userDto);

        var result = userService.removeCarFromOrders(1L, 10L);

        assertTrue(result.isPresent());
        verify(carRepository).save(car);
        verify(userRepository).save(user);
    }

    @Test
    void removeCarFromOrders_notInOrders() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));

        var result = userService.removeCarFromOrders(1L, 10L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getFavoriteCarIds_found() {
        user.setFavorites(List.of(car));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var result = userService.getFavoriteCarIds(1L);

        assertEquals(List.of(10L), result);
    }

    @Test
    void getFavoriteCarIds_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertTrue(userService.getFavoriteCarIds(1L).isEmpty());
    }

    @Test
    void addCarToFavorites_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));

        userService.addCarToFavorites(1L, 10L);

        verify(userRepository).save(user);
    }

    @Test
    void addCarToFavorites_alreadyInFavorites() {
        user.getFavorites().add(car);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));

        userService.addCarToFavorites(1L, 10L);

        verify(userRepository, never()).save(user);
    }

    @Test
    void removeCarFromFavorites_success() {
        user.getFavorites().add(car);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));

        userService.removeCarFromFavorites(1L, 10L);

        verify(userRepository).save(user);
    }

    @Test
    void removeCarFromFavorites_notInFavorites() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));

        userService.removeCarFromFavorites(1L, 10L);

        verify(userRepository, never()).save(user);
    }

    @Test
    void addCarToFavorites_invalidIds() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.addCarToFavorites(1L, 10L));
    }

    @Test
    void removeCarFromFavorites_invalidIds() {
        when(carRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.removeCarFromFavorites(1L, 10L));
    }
}
