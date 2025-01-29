package org.example.repositories;


import org.example.models.Book;
import org.example.models.Person;
import org.example.models.entity.BookEntity;
import org.example.models.entity.PersonEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, UUID> {
    @Query("SELECT b FROM Book b WHERE b.person.id = :personId")
    List<BookEntity> findBooksByPersonId(@Param("personId") UUID personId);
    @Query("SELECT b.person FROM Book b WHERE b.id = :bookId")
    Optional<PersonEntity> findBookOwner(@Param("bookId") UUID bookId);
    Optional<BookEntity> findByName(String name);
    List<BookEntity> findByNameStartingWith(String prefix);

}
