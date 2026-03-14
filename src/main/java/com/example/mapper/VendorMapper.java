package com.example.mapper;
import com.example.dto.VendorDto;
import com.example.entity.Order;
import com.example.entity.Product;
import com.example.entity.Vendor;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface VendorMapper {
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Vendor toEntity(VendorDto dto);
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "orders", ignore = true)
    VendorDto toDTO(Vendor vendor);
    @AfterMapping
    default void fillRelationIds(Vendor vendor, @MappingTarget VendorDto dto) {
        List<Long> productIds = vendor.getProducts() == null ? Collections.emptyList()
                : vendor.getProducts().stream().map(Product::getProductId).collect(Collectors.toList());

        List<Long> orderIds = vendor.getOrders() == null ? Collections.emptyList()
                : vendor.getOrders().stream().map(Order::getOrderId).collect(Collectors.toList());

        dto.setProducts(productIds);
        dto.setOrders(orderIds);
    }
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateVendorFromDto(VendorDto dto, @MappingTarget Vendor vendor);
}
