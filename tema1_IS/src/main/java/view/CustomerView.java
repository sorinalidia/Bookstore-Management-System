package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Book;

import java.util.List;

public class CustomerView {

    private final ListView<HBox> bookListView;
    private final Button viewBooksButton;
    private final Button buyBookButton;
    private final Label messageLabel; // Added label for displaying messages

    public CustomerView(Stage primaryStage) {
        bookListView = new ListView<>();
        viewBooksButton = new Button("View Books");
        buyBookButton = new Button("Buy Selected Book");
        messageLabel = new Label(); // Initialize label

        viewBooksButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        buyBookButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");

        viewBooksButton.setPadding(new Insets(10));
        buyBookButton.setPadding(new Insets(10));

        // Use a VBox for vertical alignment
        VBox vbox = new VBox(10, viewBooksButton, buyBookButton, messageLabel, bookListView); // Add label to the VBox
        vbox.setPadding(new Insets(20));

        Scene customerScene = new Scene(vbox, 720, 480);

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
        for (Book book : books) {
            HBox bookEntry = new HBox();
            Label titleLabel = new Label(" Title: " + book.getTitle());
            Label authorLabel = new Label("  Author: " + book.getAuthor());

            if (book.getQuantity() > 0) {
                bookEntry.getChildren().addAll(titleLabel,authorLabel);
            } else {
                Label outOfStockLabel = new Label("(Out of Stock)");
                outOfStockLabel.setStyle("-fx-text-fill: red;"); // Optional: Apply styling
                bookEntry.getChildren().addAll(titleLabel, authorLabel, outOfStockLabel);
            }

            bookEntry.setUserData(book);

            bookListView.getItems().add(bookEntry);
        }
    }

    public Book getSelectedBook() {
        int selectedIndex = bookListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            HBox selectedHBox = bookListView.getItems().get(selectedIndex);
            return (Book) selectedHBox.getUserData();
        } else {
            return null;
        }
    }


    public void showPurchaseSuccessMessage() {
        setMessage("Purchase successful!");
    }

    public void showPurchaseFailureMessage() {
        setMessage("Purchase failed!");
    }

    public void showNoBookSelectedMessage() {
        setMessage("No book selected!");
    }

    private void setMessage(String message) {
        messageLabel.setText(message);
    }
}
