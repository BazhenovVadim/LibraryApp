package org.example.controllers;

import jakarta.validation.Valid;
import org.example.DAO.BookDAO;
import org.example.DAO.PeopleDAO;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PeopleDAO peopleDAO;
    private final BookDAO bookDAO;
    @Autowired
    public PersonController(PeopleDAO peopleDAO, BookDAO bookDAO) {
        this.bookDAO = bookDAO;
        this.peopleDAO = peopleDAO;
    }

    @GetMapping()
    public String getPeople(Model model){
        model.addAttribute("people", peopleDAO.getPeople());
        return "people/getPeople";
    }
    @GetMapping("/{id}")
    public String getPeopleById(@PathVariable("id") int id,  Model model){
        model.addAttribute("person", peopleDAO.getPeopleById(id));
        model.addAttribute("books",peopleDAO.getBooksByPersonId(id));
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
        peopleDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleDAO.getPeopleById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "people/edit";
        }
        peopleDAO.update(id, person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/delete")
    public String preDelete(@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleDAO.getPeopleById(id));
        return "people/delete";
    }

    @DeleteMapping ("/{id}/delete")
    public String delete(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        peopleDAO.delete(id);
        return "redirect:/people";
    }
}