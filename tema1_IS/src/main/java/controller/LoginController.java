package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import model.User;
import model.validator.Notification;
import repository.book.BookRepository;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import view.CustomerView;
import view.EmployeeView;
import view.LoginView;

import static database.Constants.Roles.CUSTOMER;
import static database.Constants.Roles.EMPLOYEE;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final BookRepository bookRepository;
    private final Stage primaryStage;

    public LoginController(LoginView loginView, AuthenticationService authenticationService, BookRepository bookRepository, Stage primaryStage) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.bookRepository = bookRepository;
        this.primaryStage = primaryStage;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());

    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String role = loginView.getUserRole();

            Notification<User> loginNotification = authenticationService.login(username, password, role);

            if (loginNotification.hasErrors()){
                System.out.println("Login errors: " + loginNotification.getFormattedErrors());

                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("LogIn Successful!");
                if(role!=null){
//                    System.out.println(loginNotification.getResult().displayRoles());
                    if(loginNotification.getResult().hasRole(role)){
                        if (role.equals(CUSTOMER)){
                           showCustomerView(loginNotification);
                        }
                        else if (role.equals(EMPLOYEE)) {
                            showEmployeeView(loginNotification);
                        }
                    }
                    else{
                        loginView.setActionTargetText("Access denied!");
                    }
                }else {
                    loginView.setActionTargetText("Login failed!");
                }

            }
        }
        private void showCustomerView(Notification<User> user) {
            CustomerController customerController = new CustomerController(new CustomerView(primaryStage), new BookServiceImpl(bookRepository), authenticationService);
        }

        private void showEmployeeView(Notification<User> user) {
            EmployeeController employeeController = new EmployeeController(new EmployeeView(primaryStage), new BookServiceImpl(bookRepository), authenticationService);
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String userRole = loginView.getUserRole();
            Notification<Boolean> registerNotification = authenticationService.register(username, password, userRole);

            if(userRole!=null) {
                if (registerNotification.hasErrors()) {
                    loginView.setActionTargetText(registerNotification.getFormattedErrors());
                } else {
                    loginView.setActionTargetText("Register successful!");
                }
            }else{
                loginView.setActionTargetText("Register failed!");
            }
        }
    }
}