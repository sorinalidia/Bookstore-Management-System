package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import model.User;
import model.validator.UserValidator;
import repository.book.BookRepository;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import view.CustomerView;
import view.LoginView;

import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final UserValidator userValidator;
    private final BookRepository bookRepository;
    private final Stage primaryStage;

    public LoginController(LoginView loginView, AuthenticationService authenticationService, BookRepository bookRepository, UserValidator userValidator, Stage primaryStage) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.bookRepository = bookRepository;
        this.userValidator = userValidator;
        this.primaryStage = primaryStage;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());

    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            User user = authenticationService.login(username, password);

            if (user == null){
                loginView.setActionTargetText("Invalid Username or password!");
            }else{
                loginView.setActionTargetText("LogIn Successfull!");
                showCustomerView(user);
            }
        }
        private void showCustomerView(User user) {
            CustomerController customerController = new CustomerController(new CustomerView(primaryStage), new BookServiceImpl(bookRepository), authenticationService);
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            userValidator.validate(username, password);
            final List<String> errors = userValidator.getErrors();
            if (errors.isEmpty()) {
                if (authenticationService.register(username, password)){
                    loginView.setActionTargetText("Register successfull!");
                }else{
                    loginView.setActionTargetText("Register NOT successfull!");
                }
            } else {
                loginView.setActionTargetText(userValidator.getFormattedErrors());
            }
        }
    }
}