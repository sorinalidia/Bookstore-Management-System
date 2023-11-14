package repository.book;

import model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository<T extends Book> {
    List<T> findAll();

    Optional<T> findById(Long id);

    boolean save(T book);

    void removeAll();
}
