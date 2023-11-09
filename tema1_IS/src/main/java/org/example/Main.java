package org.example;
import database.DatabaseConnectionFactory;
import model.*;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;

import java.time.LocalDate;
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");


        BookRepository bookRepository = new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection());

        Book book = new BookBuilder()
                .setTitle("Harry Potter")
                .setAuthor("J.K. Rowling")
                .setPublishedDate(LocalDate.of(2010, 7, 3))
                .build();

        // bookRepository.save(book);
        bookRepository.removeAll();
        System.out.println(book);
        System.out.println(bookRepository.findAll());
    }
}
