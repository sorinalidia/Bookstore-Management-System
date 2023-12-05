package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Book;
import model.builder.BookBuilder;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmployeeView {
    private final TabPane tabPane;
    private final Tab viewBooksTab;
    private final Tab sellBookTab;
    private final Tab removeBookTab;
    private final Tab updateBookTab;
    private final ListView<HBox> bookListView;
    private final Button viewBooksButton;
    private final Button sellBookButton;
    private final Button removeBookButton;
    private final TextField titleField;
    private final TextField authorField;
    private final TextField quantityField;
    private final TextField priceField;
    private final DatePicker publicationDateField;
    private final Label messageLabel;

    private final Button updateBookButton;
    private final TextField updateTitleField;
    private final TextField updateAuthorField;
    private final TextField updateQuantityField;
    private final TextField updatePriceField;
    private final DatePicker updatePublicationDateField;
    private final Button generateReportButton;
    public EmployeeView(Stage primaryStage){
        tabPane = new TabPane();
        viewBooksTab = new Tab("View Books");
        sellBookTab = new Tab("Sell Book");
        removeBookTab = new Tab("Remove Book");
        updateBookTab = new Tab("Update Book");

        bookListView = new ListView<>();
        viewBooksButton = new Button("View Books");
        sellBookButton = new Button("Sell Selected Book");
        removeBookButton = new Button("Remove Selected Book");
        messageLabel = new Label();

        // Initialize fields for updating books
        updateBookButton = new Button("Update Selected Book");
        updateTitleField = new TextField();
        updateAuthorField = new TextField();
        updateQuantityField = new TextField();
        updatePriceField = new TextField();
        updatePublicationDateField = new DatePicker();

        titleField = new TextField();
        authorField = new TextField();
        quantityField = new TextField();
        priceField = new TextField();
        publicationDateField = new DatePicker();

        viewBooksButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        sellBookButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
        removeBookButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        updateBookButton.setStyle("fx-background-color: #ffffff; -fx-text-fill: black;");

        viewBooksButton.setPadding(new Insets(10));
        sellBookButton.setPadding(new Insets(10));
        removeBookButton.setPadding(new Insets(10));
        updateBookButton.setPadding(new Insets(10));

        generateReportButton = new Button("Generate Report of orders");
        generateReportButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        generateReportButton.setPadding(new Insets(10));

        VBox vbox = new VBox(10,
                viewBooksButton,
                removeBookButton,
                new Label("Add Book:"),
                new HBox(10, new Label("Title:"), titleField),
                new HBox(10, new Label("Author:"), authorField),
                new HBox(10, new Label("Quantity:"), quantityField),
                new HBox(10, new Label("Price:"), priceField),
                new HBox(10, new Label("Publication Date:"), publicationDateField),
                sellBookButton,
                updateBookButton,
                messageLabel,
                bookListView,
                generateReportButton);

        vbox.setPadding(new Insets(20));
        Scene employeeScene = new Scene(vbox, 1000, 800);

        primaryStage.setScene(employeeScene);

    }
    public void displayBooks(List<Book> books) {
        bookListView.getItems().clear();
        for (Book book : books) {
            HBox bookEntry = new HBox();
            Label titleLabel = new Label("Title: " + book.getTitle());
            Label authorLabel = new Label("Author: " + book.getAuthor());
            Label quantityLabel = new Label("Quantity: " + book.getQuantity());
            Label priceLabel = new Label("Price: " + book.getPrice());
            Label publicationDateLabel = new Label("Publication Date: " +
                    (book.getPublishedDate() != null ? book.getPublishedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A"));

            bookEntry.getChildren().addAll(titleLabel, authorLabel, quantityLabel, priceLabel, publicationDateLabel);

            bookEntry.setUserData(book);
            bookListView.getItems().add(bookEntry);
        }
    }
    public Book getSelectedBook() {
        int selectedIndex = bookListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            HBox selectedHBox = bookListView.getItems().get(selectedIndex);
            System.out.println(( selectedHBox.getUserData()).toString());
            return (Book) selectedHBox.getUserData();
        } else {
            return null;
        }
    }

    public Book getNewBook() {
        return new BookBuilder()
                .setTitle(titleField.getText())
                .setAuthor(authorField.getText())
                .setQuantity(Integer.parseInt(quantityField.getText()))
                .setPrice(BigDecimal.valueOf(Double.parseDouble(priceField.getText())))
                .setPublishedDate(publicationDateField.getValue())
                .build();
    }
    public Book getUpdatedBook() {
        int selectedIndex = bookListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            Book selectedBook = getSelectedBook();
            return new BookBuilder()
                    .setTitle(updateTitleField.getText())
                    .setAuthor(updateAuthorField.getText())
                    .setQuantity(Integer.parseInt(updateQuantityField.getText()))
                    .setPrice(BigDecimal.valueOf(Double.parseDouble(updatePriceField.getText())))
                    .setPublishedDate(updatePublicationDateField.getValue())
                    .build();
        } else {
            return null;
        }
    }
    public void addGenerateReportButtonListener(EventHandler<ActionEvent> listener) {
        generateReportButton.setOnAction(listener);
    }
    public void addViewBooksButtonListener(EventHandler<ActionEvent> listener) {
        viewBooksButton.setOnAction(listener);
    }
    public void addUpdateBooksButtonListener(EventHandler<ActionEvent> listener) {
        updateBookButton.setOnAction(listener);
    }

    public void addSellBookButtonListener(EventHandler<ActionEvent> listener) {
        sellBookButton.setOnAction(listener);
    }

    public void addRemoveBookButtonListener(EventHandler<ActionEvent> listener) {
        removeBookButton.setOnAction(listener);
    }
    public void showRemoveBookSuccessMessage() {
        setMessage("Remove successful!");
    }

    public void showRemoveBookFailureMessage() {
        setMessage("Remove failed!");
    }

    public void showAddBookSuccessMessage() {
        setMessage("New book added with success!");
    }

    public void showAddBookFailureMessage() {
        setMessage("Failed to add new book!");
    }

    public void showNoBookSelectedMessage() {
        setMessage("No book selected!");
    }

    public void showValidationError() {
        setMessage("Invalid book details");
    }

    private void setMessage(String message) {
        messageLabel.setText(message);
    }

}
