package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Book;

import org.example.models.dto.BookDto;
import org.example.models.dto.PersonDto;
import org.example.models.entity.BookEntity;
import org.example.models.entity.PersonEntity;
import org.example.models.mapper.BookMapper;
import org.example.models.mapper.PersonMapper;
import org.example.repositories.BookRepository;
import org.example.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final PersonMapper personMapper;
    private final BookMapper bookMapper;

    public List<PersonDto> findAll() {
        List<PersonEntity> people = personRepository.findAll();
        return people.stream().map(personMapper::toDto).toList();
    }

    public PersonDto findById(UUID id) {
        return personRepository.findById(id)
                .map(personMapper::toDto)
                .orElse(null);
    }

    @Transactional
    public PersonDto save(PersonDto personDTO) {
        PersonEntity person = personMapper.toEntity(personDTO);
        personRepository.save(person);
        return personMapper.toDto(person);
    }

    @Transactional
    public PersonDto update(UUID id, PersonDto updatedPersonDTO) {
        PersonEntity existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        PersonEntity updatedPerson = personMapper.toEntity(updatedPersonDTO);
        updatedPerson.setId(existingPerson.getId());
        personRepository.save(updatedPerson);
        return personMapper.toDto(updatedPerson);
    }

    @Transactional
    public PersonDto delete(UUID id) {
        if (!personRepository.existsById(id)) {
            throw new EntityNotFoundException("Person not found");
        }
        personRepository.deleteById(id);
        return personMapper.toDto(personRepository.findById(id).get());
    }

    public List<BookDto> getBooksByPersonId(UUID id) {
        List<BookEntity> books = bookRepository.findBooksByPersonId(id);
        return books.stream().map(bookMapper::toDto).toList();
    }
}



