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
                System.out.println(customerId);
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
