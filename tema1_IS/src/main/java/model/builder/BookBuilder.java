package model.builder;

import model.AudioBook;
import model.Book;
import model.EBook;
import model.PhysicalBook;

import java.time.LocalDate;

public class BookBuilder {

    private Book book;

    public BookBuilder() {
        book = new PhysicalBook();
    }

    public BookBuilder setId(Long id) {
        book.setId(id);
        return this;
    }

    public BookBuilder setAuthor(String author) {
        book.setAuthor(author);
        return this;
    }

    public BookBuilder setTitle(String title) {
        book.setTitle(title);
        return this;
    }

    public BookBuilder setPublishedDate(LocalDate publishedDate) {
        book.setPublishedDate(publishedDate);
        return this;
    }

    public Book build() {
        return book;
    }

    public AudioBook buildAudioBook() {
        AudioBook audioBook = new AudioBook();
        audioBook.setId(book.getId());
        audioBook.setAuthor(book.getAuthor());
        audioBook.setTitle(book.getTitle());
        audioBook.setPublishedDate(book.getPublishedDate());
        return audioBook;
    }

    public EBook buildEBook() {
        EBook eBook = new EBook();
        eBook.setId(book.getId());
        eBook.setAuthor(book.getAuthor());
        eBook.setTitle(book.getTitle());
        eBook.setPublishedDate(book.getPublishedDate());
        return eBook;
    }

    public BookBuilder setRunTime(int runTime) {
        if (book instanceof AudioBook) {
            ((AudioBook) book).setRunTime(runTime);
        }
        return this;
    }

    public BookBuilder setFormat(String format) {
        if (book instanceof EBook) {
            ((EBook) book).setFormat(format);
        }
        return this;
    }
}
