package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Book;

import java.util.List;

public class CustomerView {

    private final ListView<Book> bookListView;
    private final Button viewBooksButton;
    private final Button buyBookButton;

    public CustomerView(Stage primaryStage) {
        bookListView = new ListView<>();
        viewBooksButton = new Button("View Books");
        buyBookButton = new Button("Buy Selected Book");

        HBox hbox = new HBox(viewBooksButton, buyBookButton, bookListView);

        Scene customerScene = new Scene(hbox, 720, 480);

        primaryStage.setScene(customerScene);
    }

    public void addViewBooksButtonListener(EventHandler<ActionEvent> listener) {
        viewBooksButton.setOnAction(listener);
    }

    public void addBuyBookButtonListener(EventHandler<ActionEvent> listener) {
        buyBookButton.setOnAction(listener);
    }

    public void displayBooks(List<Book> books) {
        bookListView.getItems().clear();
        bookListView.getItems().addAll(books);
    }

    public Book getSelectedBook() {
        return bookListView.getSelectionModel().getSelectedItem();
    }

    public void showPurchaseSuccessMessage() {
        System.out.println("Purchase successful!");
    }

    public void showPurchaseFailureMessage() {
        System.out.println("Purchase failed!");
    }

    public void showNoBookSelectedMessage() {
        System.out.println("No book selected!");
    }
}
