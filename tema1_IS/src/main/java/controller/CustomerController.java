package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Book;
import service.book.BookService;
import service.user.AuthenticationService;
import view.CustomerView;

import java.util.List;

public class CustomerController {

    private final CustomerView customerView;
    private final BookService bookService;
    private final AuthenticationService authenticationService;

    public CustomerController(CustomerView customerView, BookService bookService, AuthenticationService authenticationService) {
        this.customerView = customerView;
        this.bookService = bookService;
        this.authenticationService = authenticationService;

        this.customerView.addViewBooksButtonListener(new ViewBooksButtonListener());
        this.customerView.addBuyBookButtonListener(new BuyBookButtonListener());
    }

    private class ViewBooksButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            /*Book book = new BookBuilder()
                    .setTitle("'A ride of a lifetime'")
                    .setAuthor("Robert Iger")
                    .setPublishedDate(LocalDate.of(2019,11,9))
                    .setQuantity(5)
                    .setPrice(BigDecimal.valueOf(69.99))
                    .build();
            Book book1 = new BookBuilder()
                    .setTitle("'A promised land'")
                    .setAuthor("Barak Obama")
                    .setPublishedDate(LocalDate.of(2020,9,23))
                    .setQuantity(2)
                    .setPrice(BigDecimal.valueOf(49.99))
                    .build();
            Book book2 = new BookBuilder()
                    .setTitle("'Atomic habits'")
                    .setAuthor("James Clear")
                    .setPublishedDate(LocalDate.of(2018,10,18))
                    .setQuantity(2)
                    .setPrice(BigDecimal.valueOf(59.99))
                    .build();
            Book book3 = new BookBuilder()
                    .setTitle("'The way of the superior man'")
                    .setAuthor("David Deida")
                    .setPublishedDate(LocalDate.of(2016,7,20))
                    .setQuantity(4)
                    .setPrice(BigDecimal.valueOf(79.99))
                    .build();
            bookService.save(book);
            bookService.save(book1);
            bookService.save(book2);
            bookService.save(book3);*/
            List<Book> books = bookService.findAll();

            customerView.displayBooks(books);
        }
    }

    private class BuyBookButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = customerView.getSelectedBook();

            if (selectedBook != null) {
                Long customerId = authenticationService.getLoggedInCustomerId();

                boolean purchaseSuccess = bookService.buyBook(customerId, selectedBook.getId());

                if (purchaseSuccess) {
                    customerView.showPurchaseSuccessMessage();
                } else {
                    customerView.showPurchaseFailureMessage();
                }
            } else {
                customerView.showNoBookSelectedMessage();
            }
        }
    }
}
