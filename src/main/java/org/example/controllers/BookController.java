package org.example.controllers;

import jakarta.validation.Valid;
import org.example.models.Book;
import org.example.models.Person;
import org.example.services.BookService;
import org.example.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String getBooks(@RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                           @RequestParam(value = "page",  defaultValue = "0") int page,
                           @RequestParam(value = "size",  defaultValue = "2") int size,
                           Model model){
        Page<Book> booksPage = bookService.getBooksPaginated(page, size);
        model.addAttribute("booksPage", booksPage);
        model.addAttribute("booksSorted", bookService.findAll(sortByYear));
        return "books/getBooks";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id,@ModelAttribute("person") Person person,
                              Model model){
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("isBookLate", bookService.checkLateDate(id));
        Optional<Person> bookOwner = bookService.findBookOwner(id);

        if (bookOwner.isPresent()){
            model.addAttribute("owner", bookOwner.get());
            System.out.println("owner");
        }
        else {
            model.addAttribute("people", personService.findAll());
            System.out.println("people");
        }
        return "books/getBookById";
    }

    @GetMapping("/new")
    public String getForm(@ModelAttribute("book") Book book){
        return "books/new";
    }
    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/delete")
    public String preDelete(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookService.findById(id));
        return "books/delete";
    }

    @DeleteMapping ("/{id}/delete")
    public String delete(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        bookService.delete(id);
        return "redirect:/books";
    }

    @PostMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id,@ModelAttribute("person") Person selectedPerson){
        bookService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String findBook(@RequestParam(value = "name", required = false) String name, Model model) {

        if (name != null && !name.isEmpty()) {
            // Сначала пробуем найти книгу по точному совпадению
            Book book = bookService.findBookByName(name);
            if (book != null) {
                model.addAttribute("book", book);
            } else {
                // Если книга не найдена по точному совпадению, ищем книги по префиксу
                List<Book> books = bookService.findBooksByPrefix(name);
                if (!books.isEmpty()) {
                    model.addAttribute("books", books); // Добавляем список книг
                } else {
                    model.addAttribute("message", "Книга не найдена");
                }
            }
        }
        return "books/search";
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<String> autocomplete(@RequestParam("prefix") String prefix) {
        // Получаем список названий книг, начинающихся с префикса
        List<Book> books = bookService.findBooksByPrefix(prefix);
        List<String> bookNames = books.stream()
                .map(Book::getName)
                .collect(Collectors.toList());
        return bookNames;
    }



}
