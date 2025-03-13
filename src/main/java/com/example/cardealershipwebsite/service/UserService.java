package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.UserDto;
import com.example.cardealershipwebsite.mapper.UserMapper;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.CarRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** User Service. */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final UserMapper userMapper;


    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    /**Получить юзера по айди. */
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    /** Создать юзера. */
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(userDto.getPasswordHash());
        return userMapper.toDto(userRepository.save(user));
    }

    /** Обновление юзера.*/
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id).map(user -> {
            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            }
            if (userDto.getPasswordHash() != null) {
                user.setPasswordHash(userDto.getPasswordHash());
            }
            return userMapper.toDto(userRepository.save(user));
        });
    }

    /** Удаление юзера. */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /** Получить заказы пользователя. */
    public List<Long> getUserOrderCarIds(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getOrders().stream().map(Car::getId).toList())
                .orElse(List.of());
    }

    /** Добавить машину в заказы пользователя. */
    public Optional<UserDto> addCarToOrders(Long userId, Long carId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Car> carOpt = carRepository.findById(carId);

        if (userOpt.isEmpty() || carOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        Car car = carOpt.get();

        // Проверка, что у машины нет заказчика
        if (car.getUserWhoOrdered() != null) {
            throw new IllegalStateException("У машины уже есть заказчик.");

        }

        car.setUserWhoOrdered(user);
        user.getOrders().add(car);

        carRepository.save(car);
        userRepository.save(user);

        return Optional.of(userMapper.toDto(user));
    }

    /** Удалить машину из заказов пользователя. */
    public Optional<UserDto> removeCarFromOrders(Long userId, Long carId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Car> carOpt = carRepository.findById(carId);

        if (userOpt.isEmpty() || carOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        Car car = carOpt.get();

        if (!user.getOrders().contains(car)) {
            return Optional.empty();
        }

        car.setUserWhoOrdered(null);
        user.getOrders().remove(car);

        carRepository.save(car);
        userRepository.save(user);

        return Optional.of(userMapper.toDto(user));
    }

    /** Получить избранные машины пользователя. */
    public List<Long> getFavoriteCarIds(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getFavorites().stream().map(Car::getId).toList())
                .orElse(List.of());
    }

    /** Добавить машину в избранное. */
    public void addCarToFavorites(Long userId, Long carId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Car> carOpt = carRepository.findById(carId);

        if (userOpt.isEmpty() || carOpt.isEmpty()) {
            throw new IllegalArgumentException("Пользователь или машина не найдены.");
        }

        User user = userOpt.get();
        Car car = carOpt.get();

        if (!user.getFavorites().contains(car)) {
            user.getFavorites().add(car);
            userRepository.save(user);
        }

    }

    /** Удалить машину из избранного. */
    public void removeCarFromFavorites(Long userId, Long carId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Car> carOpt = carRepository.findById(carId);

        if (userOpt.isEmpty() || carOpt.isEmpty()) {
            throw new IllegalArgumentException("Пользователь или машина не найдены.");
        }

        User user = userOpt.get();
        Car car = carOpt.get();

        if (user.getFavorites().contains(car)) {
            user.getFavorites().remove(car);
            userRepository.save(user);
        }
    }
}
