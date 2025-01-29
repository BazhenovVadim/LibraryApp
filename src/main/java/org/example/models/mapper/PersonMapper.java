package org.example.models.mapper;

import org.example.models.dto.PersonDto;
import org.example.models.entity.PersonEntity;
import org.example.models.mapper.enums.EnumMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EnumMapper.class})
public interface PersonMapper {
    PersonDto toDto(PersonEntity entity);

    PersonEntity toEntity(PersonDto dto);
}
