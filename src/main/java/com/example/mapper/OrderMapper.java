package com.example.mapper;

import com.example.dto.OrderDto;
import com.example.entity.Order;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PoItemMapper.class, DeliveryDetailsMapper.class})
public interface OrderMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrder(OrderDto dto, @MappingTarget Order order);

    @Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "vendor.vendorId", target = "vendorId")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "deliveryDetails", target = "deliveryDetailsDto")
    @Mapping(source = "netTotal",target = "netTotal")
    OrderDto toDto(Order order);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "vendor", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "deliveryDetails", ignore = true)
    @Mapping(target = "courier", ignore = true)
    Order toEntity(OrderDto dto);
}
