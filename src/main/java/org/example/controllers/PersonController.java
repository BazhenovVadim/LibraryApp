package org.example.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.models.Person;
import org.example.models.dto.BookDto;
import org.example.models.dto.PersonDto;
import org.example.services.BookService;
import org.example.services.PersonService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/people")
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final BookService bookService;


    @GetMapping()
    public List<PersonDto> getPeople(){
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public PersonDto getPeopleById(@PathVariable("id") UUID id) {
        PersonDto person = personService.findById(id);
        if (person != null) {
            List<BookDto> books = personService.getBooksByPersonId(id);
            bookService.populateLateStatus(books);
        }
        return person;
    }


    @PostMapping()
    public PersonDto createPerson( @Valid @RequestBody PersonDto person, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException();
        }
        return personService.save(person);
    }

    @PatchMapping("/{id}")
    public PersonDto update(@PathVariable("id") UUID id, @Valid @RequestBody PersonDto person,
                         BindingResult bindingResult){
        return personService.update(id, person);

    }

    @DeleteMapping ("/{id}/delete")
    public PersonDto delete(@PathVariable("id") UUID id, @ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        return personService.delete(id);
    }
}