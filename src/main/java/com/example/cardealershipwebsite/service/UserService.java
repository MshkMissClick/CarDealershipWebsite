package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.model.User;
import com.example.cardealershipwebsite.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/** Service. */
@Service
public class UserService {
    private final UserRepository userRepository;

    /**Конструктор.*/
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**Получить пользователя по ID.*/
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**Создать пользователя.*/
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**Обновить пользователя.*/
    public Optional<User> updateUser(Long id, User user) {
        if (!userRepository.existsById(id)) {
            return Optional.empty();
        }
        user.setId(id);
        return Optional.of(userRepository.save(user));
    }

    /**Удалить пользователя.*/
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
