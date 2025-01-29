package org.example.models.dto.enumdto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@RequiredArgsConstructor
@Getter
@SuperBuilder
public class DescriptiveEnumDto {
    private final String code;
    private final String description;
}
