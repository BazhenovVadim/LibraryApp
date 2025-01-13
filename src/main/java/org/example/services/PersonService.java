package org.example.services;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.BookRepository;
import org.example.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService{

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, BookRepository bookRepository) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }
    public Person findById(int id){
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }
    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }
    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);
    }
    @Transactional
    public void delete(int id){
        personRepository.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id) {
        return bookRepository.findBooksByPersonId(id);
    }


}
//TODO: доделать переход на страницу книги у человека. Добавить все ссылки для перехода между страницами.Ускорить работу сайта. Доделать пагинацию и сортировку
//TODO: Если назначаем книгу то назначается еще и текущее время. Переделать формы книг
//TODO: Проверять чтобы книга если у человека то только тогда проверяем срок годности
