package org.example.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BookDto {
    @NotNull
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String author;
    @NotNull
    private int year;
    @NotNull
    private LocalDateTime timeOfTaking;
    private boolean isLate = false;
}
