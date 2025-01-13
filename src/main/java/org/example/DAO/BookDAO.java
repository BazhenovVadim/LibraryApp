//package org.example.DAO;
//import org.example.models.Book;
//import org.example.models.Person;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@Transactional
//public class BookDAO {
//
//    private final SessionFactory sessionFactory;
//    @Autowired
//    public BookDAO(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Transactional
//    public List<Book> getBooks() {
//        Session session = sessionFactory.getCurrentSession();
//        return session.createQuery("from Book", Book.class).getResultList();
//    }
//
//    @Transactional
//    public Book getBookById(int id) {
//        Session session = sessionFactory.getCurrentSession();
//        return session.get(Book.class, id);
//    }
//
//    @Transactional
//    public void save(Book book) {
//        Session session = sessionFactory.getCurrentSession();
//        session.save(book);
//    }
//
//    @Transactional
//    public void update(int id ,Book updatedBook) {
//        Session session = sessionFactory.getCurrentSession();
//        updatedBook.setId(id);
//        session.merge(updatedBook);
//    }
//
//    @Transactional
//    public void delete(int id) {
//        Session session = sessionFactory.getCurrentSession();
//        session.delete(session.get(Book.class, id));
//    }
//
//    @Transactional
//    public Optional<Person> getBookOwner(int id){
//        Session session = sessionFactory.getCurrentSession();
//        Book book = session.get(Book.class, id);
//        return Optional.ofNullable(book).map(Book::getPerson);
//
//    }
//    @Transactional
//    public void assign(int id, Person selectedPerson){
//        Session session = sessionFactory.getCurrentSession();
//        Book book = session.get(Book.class, id);
//        book.setPerson(selectedPerson);
//    }
//
//    @Transactional
//    public void release(int id){
//        Session session = sessionFactory.getCurrentSession();
//        Book book = session.get(Book.class, id);
//        book.setPerson(null);
//    }
//}


