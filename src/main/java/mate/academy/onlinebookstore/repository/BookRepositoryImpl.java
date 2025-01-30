package mate.academy.onlinebookstore.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.exception.DataProcessingException;
import mate.academy.onlinebookstore.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
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
        } catch (Exception e) {
            throw new DataProcessingException("Cannot find all books", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        Book book;
        try (Session session = sessionFactory.openSession()) {
            book = session.find(Book.class, id);
        } catch (Exception e) {
            throw new DataProcessingException("Failed to retrieve books from the database", e);
        }
        return Optional.ofNullable(book);
    }
}
