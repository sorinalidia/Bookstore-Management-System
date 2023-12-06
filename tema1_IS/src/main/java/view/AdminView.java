package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import model.builder.UserBuilder;

import java.util.List;
import java.util.Optional;

public class AdminView {
    private final TabPane tabPane;
    private final Tab addEmployeeTab;
    private final Tab updateEmployeeTab;
    private final Tab deleteEmployeeTab;
    private final ListView<User> employeeListView;
    private final Button viewEmployeesButton;
    private final Button addEmployeeButton;
    private final Button updateUsernameButton;
    private final Button updatePasswordButton;
    private final Button deleteEmployeeButton;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Label messageLabel;
    private final Button generateReportButton;

    public AdminView(Stage primaryStage) {
        tabPane = new TabPane();
        addEmployeeTab = new Tab("Add Employee");
        updateEmployeeTab = new Tab("Update Employee");
        deleteEmployeeTab = new Tab("Delete Employee");

        employeeListView = new ListView<>();
        viewEmployeesButton = new Button("View Employees");
        addEmployeeButton = new Button("Add Employee");
        updateUsernameButton = new Button("Change username");
        updatePasswordButton = new Button("Change password");
        deleteEmployeeButton = new Button("Delete Employee");
        usernameField = new TextField();
        passwordField = new PasswordField();
        messageLabel = new Label();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        Scene adminScene = new Scene(vbox, 720, 720);

        HBox addEmployeeBox = new HBox(usernameField, passwordField, addEmployeeButton);
        HBox updateEmployeeBox = new HBox(updateUsernameButton,updatePasswordButton);
        HBox deleteEmployeeBox = new HBox(deleteEmployeeButton,employeeListView);

        addEmployeeTab.setContent(addEmployeeBox);
        updateEmployeeTab.setContent(updateEmployeeBox);
        deleteEmployeeTab.setContent(deleteEmployeeBox);

        generateReportButton = new Button("Generate Report of orders");

        tabPane.getTabs().addAll( addEmployeeTab, updateEmployeeTab, deleteEmployeeTab);

        vbox.getChildren().addAll(tabPane, messageLabel, viewEmployeesButton,employeeListView,generateReportButton );

        primaryStage.setScene(adminScene);
    }

    public void addAddEmployeeButtonListener(EventHandler<ActionEvent> listener) {
        addEmployeeButton.setOnAction(listener);
    }
    public void addViewEmployeeButtonListener(EventHandler<ActionEvent> listener) {
        viewEmployeesButton.setOnAction(listener);
    }
    public void addUpdateUsernameButtonListener(EventHandler<ActionEvent> listener) {
        updateUsernameButton.setOnAction(listener);
    }
    public void addUpdatePasswordButtonListener(EventHandler<ActionEvent> listener) {
        updatePasswordButton.setOnAction(listener);
    }
    public void addRemoveEmployeeButtonListener(EventHandler<ActionEvent> listener) {
        deleteEmployeeButton.setOnAction(listener);
    }
    public void addGeneratePdfReportButtonListener(EventHandler<ActionEvent> listener) {
        generateReportButton.setOnAction(listener);
    }

    public void displayEmployees(List<User> employees){
        ObservableList<User> employeeObservableList = FXCollections.observableArrayList(employees);
        employeeListView.setItems(employeeObservableList);
    }

    public User getSelectedEmployee() {
        return employeeListView.getSelectionModel().getSelectedItem();
    }

    public User getNewEmployee() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        return new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();
    }
    public String getPasswordForUpdate() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Password");
        dialog.setHeaderText("Enter new password:");
        dialog.setContentText("New Password:");

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }
    public String getUsernameForUpdate() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Username");
        dialog.setHeaderText("Enter new username:");
        dialog.setContentText("New username:");

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }


}
