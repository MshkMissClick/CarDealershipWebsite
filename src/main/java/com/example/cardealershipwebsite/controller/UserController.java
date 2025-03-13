package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.dto.UserDto;
import com.example.cardealershipwebsite.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** User Controller. */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /** Получить всех пользователей. */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /** Получить пользователя по ID. */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.of(userService.getUserById(id));
    }

    /** Создать пользователя. */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    /** Обновить пользователя. */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.of(userService.updateUser(id, userDto));
    }

    /** Удалить пользователя. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /** Получить заказы пользователя по ID. */
    @GetMapping("/{id}/orders")
    public List<Long> getUserOrders(@PathVariable Long id) {
        return userService.getUserOrderCarIds(id);
    }

    /** Добавить машину в заказы пользователя. */
    @PostMapping("/{id}/orders/{carId}")
    public ResponseEntity<UserDto> addCarToOrders(@PathVariable Long id, @PathVariable Long carId) {
        return ResponseEntity.of(userService.addCarToOrders(id, carId));
    } //Добавить проверку на наличие у существующей машины заказчика

    /** Удалить машину из заказов пользователя. */
    @DeleteMapping("/{id}/orders/{orderId}")
    public ResponseEntity<UserDto> removeCarFromOrders(@PathVariable Long id, @PathVariable Long orderId) {
        return ResponseEntity.of(userService.removeCarFromOrders(id, orderId));
    }

    /** Получить ID всех машин в избранном у пользователя. */
    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<Long>> getUserFavorites(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFavoriteCarIds(id));
    }

    /** Добавить машину в избранное. */
    @PostMapping("/{userId}/favorites/{carId}")
    public ResponseEntity<Void> addCarToFavorites(@PathVariable Long userId, @PathVariable Long carId) {
        userService.addCarToFavorites(userId, carId);
        return ResponseEntity.ok().build();
    }

    /** Удалить машину из избранного. */
    @DeleteMapping("/{userId}/favorites/{carId}")
    public ResponseEntity<Void> removeCarFromFavorites(@PathVariable Long userId, @PathVariable Long carId) {
        userService.removeCarFromFavorites(userId, carId);
        return ResponseEntity.noContent().build();
    }
}
