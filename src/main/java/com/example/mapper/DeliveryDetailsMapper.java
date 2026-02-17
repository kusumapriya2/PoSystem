package com.example.mapper;

import com.example.dto.DeliveryDetailsDto;
import com.example.entity.Courier;
import com.example.entity.DeliveryDetails;
import com.example.entity.Order;
import org.mapstruct.*;

import java.time.LocalDate;
@Mapper(componentModel = "spring" )
public interface DeliveryDetailsMapper {

    @Mapping(source = "order.orderId", target = "orderId")
    @Mapping(source = "courier.courierId", target = "courierId")
    @Mapping(source = "courier.name", target = "courierName")
    @Mapping(source = "deliveryStatus", target = "deliveryStatus", qualifiedByName = "statusToString")
        DeliveryDetailsDto toDto(DeliveryDetails deliveryDetails);
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "courier", ignore = true)
    @Mapping(target = "deliveryStatus", ignore = true)
    DeliveryDetails toEntity(DeliveryDetailsDto dto);
    @Mapping(target = "deliveryId", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "courier", source = "courier")
    @Mapping(target = "trackingNumber", source = "trackingNumber")
    @Mapping(target = "etaDate", source = "etaDate")
    @Mapping(target = "deliveryStatus", source = "status")
    @Mapping(target = "addressSnapshot", source = "addressSnapshot")
    @Mapping(target = "shippedAt", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    DeliveryDetails create(Order order,
                           Courier courier,
                           String trackingNumber,
                           LocalDate etaDate,
                           DeliveryDetails.DeliveryStatus status,
                           String addressSnapshot);
    @Named("statusToString")
    default String statusToString(DeliveryDetails.DeliveryStatus status) {
        return status == null ? null : status.name();
    }
}
