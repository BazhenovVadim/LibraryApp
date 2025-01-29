package org.example.models.mapper.enums;


import org.example.models.dto.enumdto.DescriptiveEnumDto;
import org.example.models.enums.IDescriptiveEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnumMapper {
    default DescriptiveEnumDto toDto(IDescriptiveEnum<?> enumValue) {
        return new DescriptiveEnumDto(enumValue.getCode(), enumValue.getDescription());
    }
}