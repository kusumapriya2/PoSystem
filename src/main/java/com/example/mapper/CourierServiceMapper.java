package com.example.mapper;
import com.example.dto.CourierServiceAreaDto;
import com.example.entity.Courier;
import com.example.entity.CourierServiceArea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourierServiceMapper {
    @Mapping(source = "serviceId", target = "serviceAreaId")
    @Mapping(source = "courier.courierId", target = "courierId")
    CourierServiceAreaDto toDto(CourierServiceArea courierServiceArea);
    @Mapping(source = "serviceAreaId", target = "serviceId")
    @Mapping(source = "courierId", target = "courier")
    CourierServiceArea toEntity(CourierServiceAreaDto dto);
    default Courier map(Long courierId) {
        if (courierId == null) return null;
        Courier c = new Courier();
        c.setCourierId(courierId);
        return c;
    }

    void update(CourierServiceAreaDto courierServiceAreaDto,@MappingTarget CourierServiceArea existing);
}
