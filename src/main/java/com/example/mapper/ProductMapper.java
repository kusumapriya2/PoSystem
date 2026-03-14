package com.example.mapper;
import com.example.dto.ProductDTO;
import com.example.entity.Product;
import org.mapstruct.*;
@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "vendorId", target = "vendor.vendorId")
    Product toEntity(ProductDTO dto);
    @Mapping(source = "vendor.vendorId", target = "vendorId")
    ProductDTO toDto(Product product);
}
