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
import org.springframework.transaction.annotation.Transactional;

/** User Service. */
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь не найден";
    private static final String CAR_NOT_FOUND_MESSAGE = "Машина не найдена";

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final UserMapper userMapper;

    /** Получить всех пользователей в виде DTO. */
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /** Получить пользователя по ID. */
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    /** Создать пользователя. */
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    /** Обновить пользователя. */
    @Transactional
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id).map(existingUser -> {

            if (userDto.getName() != null) {
                existingUser.setName(userDto.getName());
            }

            if (userDto.getEmail() != null) {
                existingUser.setEmail(userDto.getEmail());
            }

            if (userDto.getPasswordHash() != null) {
                existingUser.setPasswordHash(userDto.getPasswordHash());
            }

            // Обновляем избранные машины
            if (userDto.getFavoriteCarIds() != null) {
                List<Car> favoriteCars = carRepository.findAllById(userDto.getFavoriteCarIds());
                existingUser.setFavorites(favoriteCars);
            }

            // Обновляем заказанные машины
            if (userDto.getOrderCarIds() != null) {
                List<Car> orderCars = carRepository.findAllById(userDto.getOrderCarIds());
                existingUser.setOrders(orderCars);
            }

            userRepository.save(existingUser);
            return userMapper.toDto(existingUser);
        });
    }



    /** Удалить пользователя. */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.getOrders().clear();  // Удаляем связи ManyToMany с машинами
            user.getFavorites().clear(); // Удаляем связи OneToMany с избранными машинами
            userRepository.delete(user); // Теперь удаляем пользователя
        });
    }

    /** Получение айди машин. */
    public List<Long> getUserOrderCarIds(Long userId) {
        return userRepository.findUserOrderCarIds(userId);
    }

    /** Добавление машины. */
    @Transactional
    public Optional<UserDto> addCarToOrders(Long userId, Long carId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Car> carOptional = carRepository.findById(carId);

        if (userOptional.isPresent() && carOptional.isPresent()) {
            User user = userOptional.get();
            Car car = carOptional.get();

            // Проверяем, есть ли уже эта машина в заказах
            if (!user.getOrders().contains(car)) {
                user.getOrders().add(car); // Добавляем машину в список заказов
                userRepository.save(user);  // Сохраняем обновленного пользователя
            }
            return Optional.of(userMapper.toDto(user));
        }
        return Optional.empty();  // Возвращаем пустой Optional, если пользователь или машина не найдены
    }


    /** Удалить машину из заказов пользователя. */
    @Transactional
    public Optional<UserDto> removeCarFromOrders(Long userId, Long carId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Car> carOptional = carRepository.findById(carId);

        if (userOptional.isPresent() && carOptional.isPresent()) {
            User user = userOptional.get();
            Car car = carOptional.get();

            // Удаляем машину из списка заказов
            user.getOrders().remove(car);
            userRepository.save(user);  // Сохраняем обновленного пользователя
            return Optional.of(userMapper.toDto(user));
        }
        return Optional.empty();  // Возвращаем пустой Optional, если пользователь или машина не найдены
    }

    /** Получить ID всех машин в избранном у пользователя. */
    public List<Long> getFavoriteCarIds(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getFavorites().stream().map(Car::getId).toList())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE));
    }

    /** Добавить машину в избранное. */
    @Transactional
    public void addCarToFavorites(Long userId, Long carId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException(CAR_NOT_FOUND_MESSAGE));

        if (!user.getFavorites().contains(car)) {
            user.getFavorites().add(car);
            userRepository.save(user);
        }
    }

    /** Удалить машину из избранного. */
    @Transactional
    public void removeCarFromFavorites(Long userId, Long carId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE));
        Car carToRemove = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException(CAR_NOT_FOUND_MESSAGE));
        user.getFavorites().remove(carToRemove);
        userRepository.save(user);
    }
}
