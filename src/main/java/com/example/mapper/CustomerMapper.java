package com.example.mapper;


import com.example.dto.CustomerDto;
import com.example.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target="orderIds", ignore=true)
    CustomerDto toDTO(Customer c);

    @Mapping(target="purchaseOrders", ignore=true)
    Customer toEntity(CustomerDto dto);

    void update(CustomerDto dto, @MappingTarget Customer existing);

}

