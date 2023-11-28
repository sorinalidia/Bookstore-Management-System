package repository.book;

import model.Book;
import model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{

    private List<Book> books;
    private List<Order> orders;

    public BookRepositoryMock(){
        books = new ArrayList<>();
        orders = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean buyBook(Long customerId, Long bookId, BigDecimal price) {
        Optional<Book> book = findById(bookId);

        if (book.isPresent()) {
            Order order = new Order(null, customerId, bookId, LocalDate.now());
            orders.add(order);
            return true;
        }

        return false;
    }

    @Override
    public List<Order> getCustomerOrders(Long customerId) {
        List<Order> customerOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getCustomerId().equals(customerId)) {
                customerOrders.add(order);
            }
        }

        return customerOrders;
    }

    @Override
    public void removeAll() {
        books.clear();
    }
}