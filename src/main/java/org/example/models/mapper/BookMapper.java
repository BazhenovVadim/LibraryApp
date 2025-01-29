package org.example.models.mapper;

import org.example.models.dto.BookDto;
import org.example.models.entity.BookEntity;
import org.example.models.mapper.enums.EnumMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EnumMapper.class})
public interface BookMapper {

    BookDto toDto(BookEntity entity);

    BookEntity toEntity(BookDto dto);

}
