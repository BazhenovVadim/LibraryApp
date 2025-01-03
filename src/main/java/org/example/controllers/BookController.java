package org.example.controllers;

import jakarta.validation.Valid;
import org.example.DAO.BookDAO;
import org.example.DAO.PeopleDAO;
import org.example.models.Book;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final PeopleDAO peopleDAO;
    @Autowired
    public BookController(BookDAO bookDAO, PeopleDAO peopleDAO) {
        this.peopleDAO = peopleDAO;
        this.bookDAO = bookDAO;
    }
    @GetMapping()
    public String getBooks(Model model){
        model.addAttribute("books", bookDAO.getBooks());
        return "books/getBooks";
    }
    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id,@ModelAttribute("person") Person person,
                              Model model){
        model.addAttribute("book", bookDAO.getBookById(id));


        Optional<Person> bookOwner = bookDAO.getBookOwner(id);

        if (bookOwner.isPresent()){
            model.addAttribute("owner", bookOwner.get());
            System.out.println("owner");
        }
        else {
            model.addAttribute("people", peopleDAO.getPeople());
            System.out.println("people");
        }
        return "books/getBookById";
    }

    @GetMapping("/new")
    public String getform(@ModelAttribute("book") Book book){
        return "books/new";
    }
    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookDAO.getBookById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/edit";
        }
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/delete")
    public String preDelete(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookDAO.getBookById(id));
        return "books/delete";
    }

    @DeleteMapping ("/{id}/delete")
    public String delete(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @PostMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookDAO.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id,@ModelAttribute("person") Person selectedPerson){
        bookDAO.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }
}
