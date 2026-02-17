package com.example.mapper;

import com.example.dto.PaymentDTO;
import com.example.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    Payment toEntity(PaymentDTO dto);

    @Mapping(source = "order.orderId", target = "orderId")
    PaymentDTO toDto(Payment payment);
}

