package repository.book;

import model.Book;
import model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator{
    private Cache<Book> cache;
    private Cache<Order> orderCache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache, Cache<Order> orderCache){
        super(bookRepository);
        this.cache = cache;
        this.orderCache = orderCache;
    }

    @Override
    public List<Book> findAll() {
        if (cache.hasResult()){
            return cache.load();
        }

        List<Book> books = decoratedRepository.findAll();
        cache.save(books);

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {

        if (cache.hasResult()){
            return cache.load()
                    .stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }

        return decoratedRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedRepository.save(book);
    }

    @Override
    public boolean buyBook(Long customerId, Long bookId, BigDecimal price) {
        boolean result = decoratedRepository.buyBook(customerId, bookId, price);

        cache.invalidateCache();

        return result;
    }

    @Override
    public List<Order> getCustomerOrders(Long customerId) {
        List<Order> orders = decoratedRepository.getCustomerOrders(customerId);

        if (!orders.isEmpty()) {
            orderCache.save(orders);
        }

        return orders;
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedRepository.removeAll();
    }

    @Override
    public boolean removeBookById(Long bookId) {
        boolean result = decoratedRepository.removeBookById(bookId);

        if (result) {
            cache.invalidateCache();
        }

        return result;
    }
}