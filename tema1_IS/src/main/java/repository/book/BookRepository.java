package repository.book;

import model.Book;
import model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    boolean save(Book book);
    boolean buyBook(Long customerId, Long id, BigDecimal price);

    List<Order> getCustomerOrders(Long customerId);

    void removeAll();
    boolean removeBookById(Long bookId);

}