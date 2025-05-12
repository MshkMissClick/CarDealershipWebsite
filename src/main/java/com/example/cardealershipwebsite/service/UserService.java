package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.dto.UserCarIdDto;
import com.example.cardealershipwebsite.dto.UserDto;
import com.example.cardealershipwebsite.dto.UserUpdateDto;
import com.example.cardealershipwebsite.exception.PasswordHashingException;
import com.example.cardealershipwebsite.exception.UserUpdateException;
import com.example.cardealershipwebsite.mapper.UserMapper;
import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.CarRepository;
import com.example.cardealershipwebsite.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public UserDto createUser(UserDto userDto) throws NoSuchAlgorithmException {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        // Хэшируем пароль перед сохранением
        String hashedPassword = hashPassword(userDto.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        return userMapper.toDto(userRepository.save(user));
    }

    /** Обновление юзера.*/
    @Transactional
    public UserDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userUpdateDto.getEmail() != null) {
                        if (userUpdateDto.getEmail().isBlank()) {
                            throw new UserUpdateException("Адрес почты не может быть пустым.");
                        }
                        user.setEmail(userUpdateDto.getEmail());
                    }
                    if (userUpdateDto.getName() != null) {
                        if (userUpdateDto.getName().isBlank()) {
                            throw new UserUpdateException("Имя не может быть пустым.");
                        }
                        user.setName(userUpdateDto.getName());
                    }
                    if (userUpdateDto.getPasswordHash() != null) {
                        try {
                            user.setPasswordHash(hashPassword(userUpdateDto.getPasswordHash()));
                        } catch (NoSuchAlgorithmException e) {
                            throw new PasswordHashingException("Ошибка хеширования пароля", e);
                        }
                    }
                    return userMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(() -> new UserUpdateException("Пользователь с ID " + id + " не найден."));
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

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // используем SHA-256
        byte[] hashBytes = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b)); // преобразуем байты в строку в шестнадцатеричном формате
        }
        return hexString.toString();
    }

    public List<UserCarIdDto> getAllUserFavorites() {
        return userRepository.findAll().stream()
                .flatMap(user -> user.getFavorites().stream()
                        .map(car -> new UserCarIdDto(user.getId(), car.getId())))
                .toList();
    }

    public List<UserCarIdDto> getAllUserOrders() {
        return userRepository.findAll().stream()
                .flatMap(user -> user.getOrders().stream()
                        .map(order -> new UserCarIdDto(user.getId(), order.getId())))
                .toList();
    }

}
