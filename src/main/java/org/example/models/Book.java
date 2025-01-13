package org.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
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

    public Book(){
    }
    public Book(String name, String author, int year, LocalDateTime timeOfTaking) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.timeOfTaking = timeOfTaking;
    }

    public LocalDateTime getTimeOfTaking() {
        return timeOfTaking;
    }

    public void setTimeOfTaking(LocalDateTime timeOfTaking) {
        this.timeOfTaking = timeOfTaking;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }
}
