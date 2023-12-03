package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Book;
import service.book.BookService;
import service.user.AuthenticationService;
import view.EmployeeView;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final BookService bookService;
    private final AuthenticationService authenticationService;
    public EmployeeController(EmployeeView employeeView, BookService bookService, AuthenticationService authenticationService){
        this.employeeView = employeeView;
        this.bookService = bookService;
        this.authenticationService = authenticationService;

        employeeView.addViewBooksButtonListener(new ViewBooksButtonListener());
        employeeView.addSellBookButtonListener(new BuyBookButtonListener());
        employeeView.addRemoveBookButtonListener(new RemoveBookButtonListener());
    }

    private class ViewBooksButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            employeeView.displayBooks(bookService.findAll());
        }
    }

    private class BuyBookButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Book newBook = employeeView.getNewBook();

            if (isValidBook(newBook)) {
                boolean addSuccess = bookService.save(newBook);

                if (addSuccess) {
                    employeeView.showAddBookSuccessMessage();
                } else {
                    employeeView.showAddBookFailureMessage();
                }
            } else {
                employeeView.showValidationError();
            }
        }

        private boolean isValidBook(Book book) {
            return !book.getTitle().isEmpty() && !book.getAuthor().isEmpty() && book.getQuantity() > 0;
        }
    }

    private class RemoveBookButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();

            if (selectedBook != null) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove the selected book?", ButtonType.YES, ButtonType.NO);
                confirmation.showAndWait();

                if (confirmation.getResult() == ButtonType.YES) {
                    boolean removeSuccess = bookService.removeBook(selectedBook.getId());

                    if (removeSuccess) {
                        employeeView.showRemoveBookSuccessMessage();
                    } else {
                        employeeView.showRemoveBookFailureMessage();
                    }
                }
            } else {
                employeeView.showNoBookSelectedMessage();
            }
        }
    }
}
