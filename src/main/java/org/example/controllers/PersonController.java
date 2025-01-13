package org.example.controllers;

import jakarta.validation.Valid;
import org.example.models.Book;
import org.example.models.Person;
import org.example.services.BookService;
import org.example.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonService personService;
    private final BookService bookService;

    @Autowired
    public PersonController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String getPeople(Model model){
        model.addAttribute("people", personService.findAll());
        return "people/getPeople";
    }
    @GetMapping("/{id}")
    public String getPeopleById(@PathVariable("id") int id,  Model model) {
        Person person = personService.findById(id);

        if (person != null) {
            List<Book> books = personService.getBooksByPersonId(id);
            Map<Integer, Boolean> lateBooks = bookService.checkLateDates(books);

            System.out.println("Late Books: " + lateBooks);
            model.addAttribute("lateBooks", lateBooks);
            model.addAttribute("person", person);
            model.addAttribute("books", books);
        }
        return "people/getPeopleById";
    }

    @GetMapping("/new")
    public String getForm(@ModelAttribute("person") Person person){
        return "people/new";
    }
    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "/people/new";
        }
        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personService.findById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "people/edit";
        }
        personService.update(id, person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/delete")
    public String preDelete(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personService.findById(id));
        return "people/delete";
    }

    @DeleteMapping ("/{id}/delete")
    public String delete(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        personService.delete(id);
        return "redirect:/people";
    }
}