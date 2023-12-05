package service.book;

import model.Book;
import model.Order;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book findById(Long id);

    boolean save(Book book);

    int getAgeOfBook(Long id);

    boolean buyBook(Long customerId, Long id);

    List<Order> getAllOrders();

    boolean removeBook(Long id);
}