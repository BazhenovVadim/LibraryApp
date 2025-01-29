package org.example.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.Book;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Table(name = "Person")
@Entity
@NoArgsConstructor
public class PersonEntity extends AbstractJpaPersistable{

    @NotEmpty(message = "Full name should be not null")
    @Column(name = "full_name")
    private String fullName;

    @Min(value = 0, message = "birthdayYear should be gratest tnan 0")
    @Column(name = "birth_year")
    private int birthYear;

    @OneToMany(mappedBy = "person")
    private List<BookEntity> bookList;

}
