package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.dto.UserDto;
import com.example.cardealershipwebsite.dto.UserUpdateDto;
import com.example.cardealershipwebsite.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** User Controller. */
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {
    private final UserService userService;

    /** Получить всех пользователей. */
    @Operation(summary = "Получить список всех пользователей", description = "Возвращает список всех пользователей в системе.")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /** Получить пользователя по ID. */
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о пользователе по его идентификатору.")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @Min(1) Long id
    ) {
        return ResponseEntity.of(userService.getUserById(id));
    }

    /** Создать пользователя. */
    @Operation(summary = "Создать нового пользователя", description = "Добавляет нового пользователя в систему.")
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Parameter(description = "Данные нового пользователя", required = true)
            @Valid @RequestBody UserDto userDto
    ) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    /** Update user. */
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя по указанному ID")
    @ApiResponses(value = {
        @ApiResponse (responseCode = "200", description = "Пользователь успешно обновлён"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDto));
    }

    /** Удалить пользователя. */
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из системы по его ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @Min(1) Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /** Получить заказы пользователя по ID. */
    @Operation(summary = "Получить список заказов пользователя", description = "Возвращает список ID машин, которые пользователь заказал.")
    @GetMapping("/{id}/orders")
    public List<Long> getUserOrders(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @Min(1) Long id
    ) {
        return userService.getUserOrderCarIds(id);
    }

    /** Добавить машину в заказы пользователя. */
    @Operation(summary = "Добавить машину в заказы пользователя", description = "Добавляет автомобиль в список заказов пользователя.")
    @PostMapping("/{id}/orders/{carId}")
    public ResponseEntity<UserDto> addCarToOrders(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "ID автомобиля", required = true)
            @PathVariable @Min(1) Long carId
    ) {
        return ResponseEntity.of(userService.addCarToOrders(id, carId));
    } //Добавить проверку на наличие у существующей машины заказчика

    /** Удалить машину из заказов пользователя. */
    @Operation(summary = "Удалить машину из заказов пользователя", description = "Удаляет указанный автомобиль из заказов пользователя.")
    @DeleteMapping("/{id}/orders/{orderId}")
    public ResponseEntity<UserDto> removeCarFromOrders(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "ID заказа", required = true)
            @PathVariable @Min(1) Long orderId
    ) {
        return ResponseEntity.of(userService.removeCarFromOrders(id, orderId));
    }

    /** Получить ID всех машин в избранном у пользователя. */
    @Operation(summary = "Получить избранные автомобили пользователя",
            description = "Возвращает список ID автомобилей, добавленных пользователем в избранное.")
    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<Long>> getUserFavorites(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @Min(1) Long id
    ) {
        return ResponseEntity.ok(userService.getFavoriteCarIds(id));
    }

    /** Добавить машину в избранное. */
    @Operation(summary = "Добавить автомобиль в избранное",
            description = "Добавляет указанный автомобиль в список избранного пользователя.")
    @PostMapping("/{userId}/favorites/{carId}")
    public ResponseEntity<Void> addCarToFavorites(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "ID автомобиля", required = true)
            @PathVariable @Min(1) Long carId
    ) {
        userService.addCarToFavorites(userId, carId);
        return ResponseEntity.ok().build();
    }

    /** Удалить машину из избранного. */
    @Operation(summary = "Удалить автомобиль из избранного", description = "Удаляет указанный автомобиль из списка избранного пользователя.")
    @DeleteMapping("/{userId}/favorites/{carId}")
    public ResponseEntity<Void> removeCarFromFavorites(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "ID автомобиля", required = true)
            @PathVariable @Min(1) Long carId
    ) {
        userService.removeCarFromFavorites(userId, carId);
        return ResponseEntity.noContent().build();
    }
}
