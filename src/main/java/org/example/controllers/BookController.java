package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.models.Book;
import org.example.models.Person;
import org.example.models.dto.BookDto;
import org.example.models.dto.PersonDto;
import org.example.services.BookService;
import org.example.services.PersonService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final PersonService personService;

    @GetMapping()
    public List<BookDto> getBooks(@RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "2") int size) {
//        Page<Book> booksPage = bookService.getBooksPaginated(page, size);
//        model.addAttribute("booksPage", booksPage);
//        model.addAttribute("booksSorted", bookService.findAll(sortByYear));
        return bookService.findAll();
        //TODO:пагинация и сортировка
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable("id") UUID id, @ModelAttribute("person") Person person) {
//        model.addAttribute("book", bookService.findById(id));
//        model.addAttribute("isBookLate", bookService.checkLateDate(id));
        return bookService.findById(id);
    }

    @PostMapping()
    public BookDto createBook(@Valid @RequestBody BookDto book, BindingResult bindingResult) {
        bookService.save(book);
        return bookService.save(book);
    }

    @PatchMapping("/{id}")
    public BookDto update(@PathVariable("id") UUID id, @Valid @RequestBody BookDto book,
                          BindingResult bindingResult) {
        return bookService.update(id, book);
    }


    @DeleteMapping("/{id}/delete")
    public BookDto delete(@PathVariable("id") UUID id, @Valid @RequestBody BookDto book,
                          BindingResult bindingResult) {
        return bookService.delete(id);

    }

    @PostMapping("/{id}/release")
    public BookDto release(@PathVariable("id") UUID id) {
        return bookService.release(id);
    }

    @PatchMapping("/{id}/assign")
    public BookDto assign(@PathVariable("id") UUID id, @RequestBody PersonDto selectedPerson) {
        return bookService.assign(id, selectedPerson);
    }

    @GetMapping("/search")
    public List<BookDto> findBook(@RequestParam(value = "name", required = false) String name) {

        if (name != null && !name.isEmpty()) {
            // Сначала пробуем найти книгу по точному совпадению
            BookDto book = bookService.findBookByName(name);
            if (book != null) {
                return new ArrayList<>(List.of(book));
            }
            // Если книга не найдена по точному совпадению, ищем книги по префиксу
            List<BookDto> books = bookService.findBooksByPrefix(name);
            if (!books.isEmpty()) {
                return books; // Добавляем список книг
            } else {
                throw new ObjectNotFoundException("Book not found", Book.class);
            }
        }
        return null;
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<String> autocomplete(@RequestParam("prefix") String prefix) {
        // Получаем список названий книг, начинающихся с префикса
        List<BookDto> books = bookService.findBooksByPrefix(prefix);
        List<String> bookNames = books.stream()
                .map(BookDto::getName)
                .collect(Collectors.toList());
        return bookNames;
    }


}
