package mate.academy.onlinebookstore.repository.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.exception.DataProcessingException;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.merge(book);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DataProcessingException("Cannot save book to DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Book", Book.class).list();
        }
    }
}
