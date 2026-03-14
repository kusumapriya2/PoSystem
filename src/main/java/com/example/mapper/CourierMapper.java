package com.example.mapper;
import com.example.dto.CourierDto;
import com.example.entity.Courier;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface CourierMapper {
    Courier toEntity(CourierDto dto);
    CourierDto toDto(Courier courier);
}
