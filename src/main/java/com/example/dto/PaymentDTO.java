package com.example.dto;

import com.example.entity.Order;
import com.example.entity.Payment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long paymentId;

    private Long orderId;

    private Double amountPaid;

    private Order.paymentMethod paymentMethod;

    private LocalDateTime paymentDate;
    private Payment.PaymentStatus status;

}
