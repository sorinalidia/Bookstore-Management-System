package service.book;

import model.Book;
import model.Order;
import repository.book.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookServiceImpl implements BookService{

    final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);

        LocalDate now = LocalDate.now();

        return (int)ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }

    @Override
    public boolean buyBook(Long customerId, Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        System.out.println(book.toString());
        if (book != null && customerId!=null && book.getQuantity()>=1 && book.getEmployeeId()!=null) {
            return bookRepository.buyBook(customerId, book);
        }

        return false;
    }
    @Override
    public List<Order> getAllOrders(){
        return bookRepository.findAllOrders();
    }

    @Override
    public boolean removeBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book != null) {
            return bookRepository.removeBookById(bookId);
        }

        return false;
    }

    @Override
    public boolean updateBook(Book updatedBook) {
        if (updatedBook != null) {
            return bookRepository.update(updatedBook);
        }

        return false;
    }

}