package org.example.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.Person;

@Setter
@Getter
@Entity
@Table(name = "Book")
@NoArgsConstructor
public class BookEntity extends AbstractJpaPersistable{

    @NotEmpty(message = "Name should be not null")
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "Author name should be not null")
    @Column(name = "author")
    private String author;
    @Min(value = 0, message = "Year should be gratest tnan 0")
    @Column(name = "year")
    private int year;

    @Column(name = "time_of_taking")
    private LocalDateTime timeOfTaking;
    @ManyToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private PersonEntity person;

}
