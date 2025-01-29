package org.example.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PersonDto {
    private UUID id;

    private String fullName;

    private int birthYear;
}
