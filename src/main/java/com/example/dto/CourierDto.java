package com.example.dto;
import com.example.entity.Courier;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class CourierDto {
    private Long courierId;
    @NotBlank(message = "Courier name cannot be empty")
    @Size(min = 2, max = 50, message = "Courier name must be between 2 and 50 characters")
    private String name;
    @NotNull(message = "Priority is required")
    @Min(value = 1, message = "Priority must be at least 1")
    @Max(value = 10, message = "Priority cannot be greater than 10")
    private Integer priority;
    @NotNull(message = "SLA days is required")
    @Min(value = 1, message = "SLA days must be at least 1")
    @Max(value = 30, message = "SLA days cannot be more than 30")
    private Integer slaDays;
    @NotNull(message = "Courier status is required")
    private Courier.Status status;
}
