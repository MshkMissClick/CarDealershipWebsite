package com.example.cardealershipwebsite.dto;

public class UserCarIdDto {
    private Long userId;
    private Long carId;

    public UserCarIdDto(Long userId, Long carId) {
        this.userId = userId;
        this.carId = carId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}
