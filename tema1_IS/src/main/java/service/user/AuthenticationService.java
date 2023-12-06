package service.user;

import model.User;
import model.validator.Notification;

import java.util.List;

public interface AuthenticationService {
    /*boolean register(String username, String password);

    User login(String username, String password);

    boolean logout(User user);*/
    Notification<Boolean> register(String username, String password, String role);

    Notification<User> login(String username, String password, String user);

    boolean logout(User user);
    Long getLoggedInCustomerId();

    boolean createAdmin(String username, String password);

    boolean hasAdmin();

    List<User> getEmployees();

    boolean removeEmployee(Long id);

    Notification<Boolean> updateEmployeePassword(Long id, String newPassword);

    Notification<Boolean> updateEmployeeUsername(Long id, String newPassword);
}