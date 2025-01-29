package org.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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
    private Person person;

}
