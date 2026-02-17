package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class CourierServiceAreaDto {

    private Long serviceAreaId;

    @NotNull(message = "courierId is required")
    private Long courierId;

    @NotNull(message = "pincode is required")
    private Long pincode;
}

