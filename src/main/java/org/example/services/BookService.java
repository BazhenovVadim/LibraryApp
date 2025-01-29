package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.models.dto.BookDto;
import org.example.models.dto.PersonDto;
import org.example.models.entity.BookEntity;
import org.example.models.entity.PersonEntity;
import org.example.models.mapper.BookMapper;
import org.example.models.mapper.PersonMapper;
import org.example.repositories.BookRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.time.Period;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final PersonMapper personMapper;


    public List<BookDto> findAll() {
        List<BookEntity> bookEntityList = bookRepository.findAll();
        return bookEntityList.stream().map(bookMapper::toDto).toList();
    }

    public List<BookDto> findAll(boolean sortByYear) {
        if (sortByYear)
            return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "year"))
                    .stream().map(bookMapper::toDto).toList();
        else
            return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }

    public Page<BookDto> getBooksPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    public BookDto findById(UUID id) {
        return bookRepository.findById(id).stream().map(bookMapper::toDto).findFirst().orElse(null);
    }

    @Transactional
    public BookDto save(BookDto book) {
        if (book == null) {
            log.error("Attempt to save a null book");
            throw new IllegalArgumentException("Book cannot be null");
        }
        BookEntity bookEntity = bookMapper.toEntity(book);
        bookRepository.save(bookEntity);
        log.info("Book with ID {} saved successfully", book.getId());
        return bookMapper.toDto(bookEntity);
    }

    @Transactional
    public BookDto update(UUID id, BookDto updatedBook) {
        if (updatedBook == null || id == null) {
            log.error("Attempt to update a null book");
            throw new IllegalArgumentException("Book cannot be null");
        }
        BookEntity updatedBookEntity = bookMapper.toEntity(updatedBook);
        updatedBookEntity.setId(id);
        bookRepository.save(updatedBookEntity);
        log.info("Book with ID {} updated successfully", id);
        return bookMapper.toDto(updatedBookEntity);
    }

    @Transactional
    public BookDto delete(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Person not found");
        }
        bookRepository.deleteById(id);
        return bookMapper.toDto(bookRepository.findById(id).orElse(null));
    }

    @Transactional
    public BookDto assign(UUID id, PersonDto selectedPersonDto) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        PersonEntity person = personMapper.toEntity(selectedPersonDto);

        book.setPerson(person);
        book.setTimeOfTaking(LocalDateTime.now());

        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Transactional
    public BookDto release(UUID id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setPerson(null);
            book.setTimeOfTaking(null);
            bookRepository.save(book);
        });
        return bookMapper.toDto(bookRepository.findById(id).orElse(null));
    }

    @Transactional
    public Optional<PersonDto> findBookOwner(UUID id) {
        return bookRepository.findBookOwner(id).stream().map(personMapper::toDto).findFirst();
    }

    public BookDto findBookByName(String name) {
        return bookRepository.findByName(name).stream().map(bookMapper::toDto).findFirst().orElse(null);
    }

    public List<BookDto> findBooksByPrefix(String prefix) {
        return bookRepository.findByNameStartingWith(prefix).stream().map(bookMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean checkLateDate(UUID id) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            BookEntity book = optionalBook.get();
            if (book.getTimeOfTaking() != null) {
                Period period = Period.between(book.getTimeOfTaking().toLocalDate(), LocalDate.now());
                return period.getDays() > 10;
            }
        }
        return false;
    }

    public Map<UUID, Boolean> checkLateDates(List<BookDto> books) {
        Map<UUID, Boolean> lateBooks = new HashMap<>();

        for (BookDto book : books) {
            if (book.getTimeOfTaking() != null) {
                Period period = Period.between(book.getTimeOfTaking().toLocalDate(), LocalDate.now());
                lateBooks.put(book.getId(), period.getDays() > 10);
            } else {
                lateBooks.put(book.getId(), false);
            }
        }
        return lateBooks;
    }

    public void populateLateStatus(List<BookDto> books) {
        books.forEach(book -> book.setLate(calculateLate(book)));
    }

    private boolean calculateLate(BookDto book) {
        return book.getTimeOfTaking().isBefore(LocalDateTime.now());
    }
}
