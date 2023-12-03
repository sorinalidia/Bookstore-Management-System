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
import view.LoginView;

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
            String user = loginView.getUserRole();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()){
                System.out.println("Login errors: " + loginNotification.getFormattedErrors());

                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("LogIn Successfull!");
                showCustomerView(loginNotification);
            }
        }
        private void showCustomerView(Notification<User> user) {
            CustomerController customerController = new CustomerController(new CustomerView(primaryStage), new BookServiceImpl(bookRepository), authenticationService);
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String user = loginView.getUserRole();
            Notification<Boolean> registerNotification = authenticationService.register(username, password, user);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}