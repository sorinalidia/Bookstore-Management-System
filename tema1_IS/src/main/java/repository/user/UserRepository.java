package repository.user;

import model.User;
import model.validator.Notification;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);

    Notification<Boolean> save(User user);

    void removeAll();

    boolean existsByUsername(String username);
    Long findUserIdByUsername(String username);

    boolean existsByRoleTitle(String roleTitle);

    List<User> findAllEmployees();

    boolean removeUserById(Long id);

    Notification<Boolean> updatePasswordById(Long id, String newPassword);

    Notification<Boolean> updateUsernameById(Long employeeId, String newUsername);
}