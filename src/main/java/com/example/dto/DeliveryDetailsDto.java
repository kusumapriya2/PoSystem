package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DeliveryDetailsDto {

    private Long deliveryId;

    @NotNull(message = "orderId is required")
    @Positive(message = "orderId must be positive")
    private Long orderId;

    @NotNull(message = "courierId is required")
    @Positive(message = "courierId must be positive")
    private Long courierId;

    @NotBlank(message = "courierName cannot be blank")
    @Size(min = 2, max = 80, message = "courierName must be between 2 and 80 characters")
    private String courierName;
    @Size(min = 8, max = 50, message = "trackingNumber must be between 8 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9\\-]+$",
            message = "trackingNumber can contain only letters, numbers and '-'"
    )
    private String trackingNumber;

    @NotBlank(message = "deliveryStatus is required")
    @Pattern(
            regexp = "^(ASSIGNED|SHIPPED|DELIVERED|CANCELLED|IN_TRANSIT|PENDING|READYFORPICKUP)$",
            message = "deliveryStatus must be one of: ASSIGNED, SHIPPED, IN_TRANSIT, DELIVERED, CANCELLED"
    )
    private String deliveryStatus;
    @NotNull(message = "etaDate is required")
    @FutureOrPresent(message = "etaDate cannot be in the past")
    private LocalDate etaDate;
    private LocalDate deliveryDate;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;

    @NotBlank(message = "addressSnapshot is required")
    @Size(min = 10, max = 255, message = "addressSnapshot must be between 10 and 255 characters")
    private String addressSnapshot;
}
