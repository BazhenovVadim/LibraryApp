package org.example.repositories;


import org.example.models.Book;
import org.example.models.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE b.person.id = :personId")
    List<Book> findBooksByPersonId(@Param("personId") int personId);
    @Query("SELECT b.person FROM Book b WHERE b.id = :bookId")
    Optional<Person> findBookOwner(@Param("bookId") int bookId);
    Optional<Book> findByName(String name);
    List<Book> findByNameStartingWith(String prefix);

}
