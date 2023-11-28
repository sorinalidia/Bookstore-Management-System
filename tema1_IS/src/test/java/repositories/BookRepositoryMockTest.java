package repositories;

import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMock;
import repository.book.Cache;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMockTest {

    private static BookRepository bookRepository;

    @BeforeAll
    public static void setupClass(){
        bookRepository = new BookRepositoryCacheDecorator(
                new BookRepositoryMock(),
                new Cache<>(),
                new Cache<>()
        );
    }

    @Test
    public void findAll(){
        assertEquals(0, bookRepository.findAll().size());
    }

    @Test
    public void findById(){
        final Optional<Book> books = bookRepository.findById(1L);
        assertTrue(books.isEmpty());
    }

    @Test
    public void save(){
        Book book = new BookBuilder()
                .setAuthor("Author")
                .setTitle("Title")
                .setPublishedDate(LocalDate.of(2001,6,19))
                .setQuantity(2)
                .setPrice(BigDecimal.valueOf(12.99))
                .build();

        assertTrue(bookRepository.save(book));
    }



}