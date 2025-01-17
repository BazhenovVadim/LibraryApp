package org.example.services;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.Period;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }
    public List<Book> findAll(boolean sortByYear){
        if (sortByYear) {
            return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "year"));
        } else {
            return bookRepository.findAll();
        }
    }

    public Page<Book> getBooksPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

    public Book findById(int id){
        return bookRepository.findById(id).orElse(null);
    }
    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }
    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }
    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }
    @Transactional
    public void assign(int id, Person selectedPerson){
        bookRepository.findById(id).ifPresent(book -> book.setPerson(selectedPerson));
        bookRepository.findById(id).ifPresent(book -> book.setTimeOfTaking(LocalDateTime.now()));
    }

    @Transactional
    public void release(int id){
        bookRepository.findById(id).ifPresent(book -> book.setPerson(null));
        bookRepository.findById(id).ifPresent(book -> book.setTimeOfTaking(null));
    }
    @Transactional
    public Optional<Person> findBookOwner(int id){
        return bookRepository.findBookOwner(id);
    }

    public Book findBookByName(String name){
        return bookRepository.findByName(name).orElse(null);
    }

    public List<Book> findBooksByPrefix(String prefix){
        return bookRepository.findByNameStartingWith(prefix);
    }
    @Transactional
    public boolean checkLateDate(int id){
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null && book.getTimeOfTaking() != null){
            Period period = Period.between(book.getTimeOfTaking().toLocalDate(), LocalDate.now());
            return period.getDays() > 10;
            }
        return false;
    }

    public Map<Integer, Boolean> checkLateDates(List<Book> books) {
        Map<Integer, Boolean> lateBooks = new HashMap<>();

        for (Book book : books) {
            if (book.getTimeOfTaking() != null) {
                Period period = Period.between(book.getTimeOfTaking().toLocalDate(), LocalDate.now());
                lateBooks.put(book.getId(), period.getDays() > 10); // true, если просрочена
            } else {
                lateBooks.put(book.getId(), false); // если дата не установлена
            }
        }
        return lateBooks;
    }
}
