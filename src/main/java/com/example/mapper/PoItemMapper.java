package com.example.mapper;
import com.example.dto.PoItemDto;
import com.example.entity.Order;
import com.example.entity.PoItem;
import com.example.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")
public interface PoItemMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "price", target = "unitPrice")
    @Mapping(source = "amount", target = "lineTotal")
    PoItemDto toDto(PoItem item);

    @Mapping(source = "productId", target = "product")
    PoItem toEntity(PoItemDto dto);
    default Product mapProduct(Long productId) {
        if (productId == null) return null;
        Product p = new Product();
        p.setProductId(productId);
        return p;
    }

    void update(PoItemDto dto, @MappingTarget PoItem existing);
}
