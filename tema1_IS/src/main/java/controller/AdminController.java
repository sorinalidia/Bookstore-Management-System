package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Order;
import model.User;
import model.validator.Notification;
import service.book.BookService;
import service.report.PdfReportGenerator;
import service.user.AuthenticationService;
import view.AdminView;

import java.util.List;

import static database.Constants.Roles.EMPLOYEE;

public class AdminController {
    private final AdminView adminView;
    private final AuthenticationService authenticationService;
    private final BookService bookService;

    public AdminController(AdminView adminView, AuthenticationService authenticationService, BookService bookService){
        this.adminView = adminView;
        this.authenticationService = authenticationService;
        this.bookService = bookService;

        adminView.addViewEmployeeButtonListener(new ViewEmployeesButtonListener());
        adminView.addAddEmployeeButtonListener(new AddEmployeeButtonListener());
        adminView.addUpdateUsernameButtonListener(new UpdateUsernameButtonListener());
        adminView.addUpdatePasswordButtonListener(new UpdatePasswordButtonListener());
        adminView.addRemoveEmployeeButtonListener(new RemoveEmployeeButtonListener());
        adminView.addGeneratePdfReportButtonListener(new GenerateReportButtonListener() );
    }

    private class AddEmployeeButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            User newEmployee = adminView.getNewEmployee();
            Notification<Boolean> registerEmployeeNotification = authenticationService.register(newEmployee.getUsername(), newEmployee.getPassword(),EMPLOYEE);

            if ((registerEmployeeNotification.hasErrors())){
                adminView.showMessage(registerEmployeeNotification.getFormattedErrors());
            }else{
                adminView.showMessage("Employee added successfully!");
            }
        }
    }
    private class ViewEmployeesButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            List<User> employees = authenticationService.getEmployees();
            adminView.displayEmployees(employees);
        }
    }
    private class RemoveEmployeeButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            User selectedEmployee = adminView.getSelectedEmployee();

            if (selectedEmployee != null) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove the selected employee?", ButtonType.YES, ButtonType.NO);
                confirmation.showAndWait();

                if (confirmation.getResult() == ButtonType.YES) {
                    boolean removeSuccess = authenticationService.removeEmployee(selectedEmployee.getId());

                    if (removeSuccess) {
                        adminView.showMessage("Employee removed successfully!");
                    } else {
                        adminView.showMessage("Failed to remove employee!");
                    }
                }
            } else {
                adminView.showMessage("No employee selected!");
            }
        }
    }
    private class UpdateUsernameButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            User selectedEmployee = adminView.getSelectedEmployee();

            if (selectedEmployee != null) {
                String newUsername = adminView.getUsernameForUpdate();

                if (newUsername != null && !newUsername.isEmpty()) {
                    Notification<Boolean> updateNotification = authenticationService.updateEmployeeUsername(selectedEmployee.getId(), newUsername);

                    if (updateNotification.hasErrors()) {
                        adminView.showMessage(updateNotification.getFormattedErrors());
                    } else {
                        adminView.showMessage("Employee username updated successfully!");
                    }
                } else {
                    adminView.showMessage("Please enter a new username for the employee.");
                }
            } else {
                adminView.showMessage("No employee selected!");
            }
        }
    }

    private class UpdatePasswordButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            User selectedEmployee = adminView.getSelectedEmployee();

            if (selectedEmployee != null) {
                String newPassword = adminView.getPasswordForUpdate();

                if (newPassword != null && !newPassword.isEmpty()) {
                    Notification<Boolean> updateNotification = authenticationService.updateEmployeePassword(selectedEmployee.getId(), newPassword);

                    if (updateNotification.hasErrors()) {
                        adminView.showMessage(updateNotification.getFormattedErrors());
                    } else {
                        adminView.showMessage("Employee password updated successfully!");
                    }
                } else {
                    adminView.showMessage("Please enter a new password for the employee.");
                }
            } else {
                adminView.showMessage("No employee selected!");
            }
        }
    }
    private class GenerateReportButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            List<Order> orders = bookService.getAllOrders();
            generatePdfReport(orders);
        }
    }
    private void generatePdfReport(List<Order> orders) {

        PdfReportGenerator.generatePdfReport( orders);
    }
}
