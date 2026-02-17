package com.example.dto;

import com.example.entity.Order;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Data
public class OrderDto {

    private Long orderId;

    @NotNull(message = "Shipping pincode cannot be null")
    @Min(value = 100000, message = "Shipping pincode must be 6 digits")
    @Max(value = 999999, message = "Shipping pincode must be 6 digits")
    private Long shippingPincode;

    private DeliveryDetailsDto deliveryDetailsDto;
    public enum PaymentStatus { DUE, PARTIAL, PAID }
    private BigDecimal subTotal;

    private BigDecimal taxAmount;

    private BigDecimal shippingCharge;

    private BigDecimal discountAmount;



    @NotBlank(message = "Shipping address line cannot be blank")
    @Size(max = 255, message = "Shipping address line must not exceed 255 characters")
    private String shippingAddressLine;

    @NotBlank(message = "Shipping city cannot be blank")
    @Size(max = 100, message = "Shipping city must not exceed 100 characters")
    private String shippingCity;

    @NotBlank(message = "Shipping state cannot be blank")
    @Size(max = 100, message = "Shipping state must not exceed 100 characters")
    private String shippingState;
    @NotNull(message = "Must be add payment method")
    private Order.paymentMethod paymentMethod;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    @NotNull(message = "Delivery date cannot be null")
    @FutureOrPresent(message = "Delivery date must be today or in the future")
    private LocalDate expectedDeliveryDate;
    private BigDecimal grandTotal;


    private BigDecimal amountPaid;
    private Order.POStatus poStatus;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Vendor ID cannot be null")
    private Long vendorId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<PoItemDto> items;
    private String discountReason;
    private BigDecimal netTotal;

}
