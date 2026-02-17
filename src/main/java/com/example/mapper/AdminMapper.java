package com.example.mapper;
import com.example.dto.AdminDto;
import com.example.entity.Admin;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDto toDTO(Admin admin);
    Admin toEntity(AdminDto adminDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(AdminDto dto, @MappingTarget Admin admin);
}
