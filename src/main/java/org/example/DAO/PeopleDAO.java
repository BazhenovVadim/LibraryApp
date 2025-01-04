
package org.example.DAO;

import org.example.models.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.example.models.Person;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class PeopleDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public PeopleDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Transactional(readOnly = true)
    public List<Person> getPeople() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Person", Person.class).getResultList();
    }
    @Transactional(readOnly = true)
    public Person getPeopleById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }
    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }
    @Transactional
    public void update(int id ,Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();
        updatedPerson.setId(id);
        session.merge(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        person.getBookList().clear();
        session.delete(person);
    }

    @Transactional
    public Optional<Person> getPersonByFullName(String fullName) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("SELECT p FROM Person p WHERE p.fullName = :name", Person.class)
                .setParameter("name", fullName)
                .uniqueResultOptional();
    }

    @Transactional
    public List<Book> getBooksByPersonId(int id){
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        return person.getBookList();
    }
}
