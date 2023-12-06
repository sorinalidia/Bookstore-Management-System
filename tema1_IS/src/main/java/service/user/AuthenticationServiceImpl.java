package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private User loggedInUser;
    public AuthenticationServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> register(String username, String password, String role) {
        Notification<Boolean> userRegisterNotification = new Notification<>();
        boolean alreadyExists= userRepository.existsByUsername(username);

        if((!alreadyExists) && (role!=null)){
            Role customerRole = rightsRolesRepository.findRoleByTitle(role);

            User user = new UserBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .setRoles(Collections.singletonList(customerRole))
                    .build();

            UserValidator userValidator = new UserValidator(user);

            boolean userValid = userValidator.validate();

            if (!userValid){
                userValidator.getErrors().forEach(userRegisterNotification::addError);
                userRegisterNotification.setResult(Boolean.FALSE);
            } else {
                user.setPassword(hashPassword(password));
                userRegisterNotification.setResult(userRepository.save(user).getResult());
            }

        }else{
            userRegisterNotification.setResult(Boolean.FALSE);
            userRegisterNotification.addError("Username already in use!");
        }

        return userRegisterNotification;
    }
    @Override
    public Notification<User> login(String username, String password, String role) {
        Notification<User> notificationFindByUsernameAndPassword = userRepository.findByUsernameAndPassword(username, hashPassword(password));

        if (notificationFindByUsernameAndPassword.hasErrors()) {
            return notificationFindByUsernameAndPassword;
        }

        loggedInUser = notificationFindByUsernameAndPassword.getResult();
        loggedInUser.setId(userRepository.findUserIdByUsername(username));
        return notificationFindByUsernameAndPassword;
    }

    @Override
    public boolean logout(User user) {
        loggedInUser = null;
        return true;
    }

    @Override
    public Long getLoggedInCustomerId() {
        if (loggedInUser != null) {
            return loggedInUser.getId();
        }
        return null;
    }

    private String hashPassword(String password) {
        try {
            // Sercured Hash Algorithm - 256
            // 1 byte = 8 biți
            // 1 byte = 1 char
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    @Override
    public boolean createAdmin(String username, String password) {
        if (hasAdmin()) {
            System.out.println("Admin already exists. Cannot create another admin.");
            return false;
        }else {
            Role adminRole = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
            User admin = new UserBuilder()
                    .setUsername(username)
                    .setPassword(hashPassword(password))
                    .setRoles(Collections.singletonList(adminRole))
                    .build();
            userRepository.save(admin);
            return true;
        }
    }

    @Override
    public boolean hasAdmin() {
        return userRepository.existsByRoleTitle(ADMINISTRATOR);
    }

    @Override
    public List<User> getEmployees() {
        return userRepository.findAllEmployees();
    }

    @Override
    public boolean removeEmployee(Long id) {
        return userRepository.removeUserById(id);
    }

    @Override
    public Notification<Boolean> updateEmployeePassword(Long id, String newPassword) {
        Notification<Boolean> notification = new Notification<>();

        try {
            Role role = rightsRolesRepository.findRolesForUser(id).get(0);
            User updatedUser = new UserBuilder()
                    .setId(id)
                    .setUsername("dummy@gmail.com")     //it's just for validating the password, it won't change the username
                    .setPassword(newPassword)
                    .setRoles(Collections.singletonList(role))
                    .build();

            UserValidator userValidator = new UserValidator(updatedUser);
            boolean userValid = userValidator.validate();

            if (!userValid) {
                userValidator.getErrors().forEach(notification::addError);
            } else {
                notification.setResult(userRepository.updatePasswordById(id, hashPassword(newPassword)).getResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
            notification.addError("Something went wrong with the database.");
        }

        return notification;
    }
    @Override
    public Notification<Boolean> updateEmployeeUsername(Long id, String newUsername) {
        Notification<Boolean> notification = new Notification<>();

        boolean usernameExists = userRepository.existsByUsername(newUsername);
        if (usernameExists) {
            notification.addError("Username already in use. Please choose another one.");
            return notification;
        }

        try {
            Role role = rightsRolesRepository.findRolesForUser(id).get(0);
            User updatedUser = new UserBuilder()
                    .setId(id)
                    .setUsername(newUsername)
                    .setPassword("Dummy123!")   //it's just for validating the username, it won't change the password
                    .setRoles(Collections.singletonList(role))
                    .build();

            UserValidator userValidator = new UserValidator(updatedUser);
            boolean userValid = userValidator.validate();

            if (!userValid) {
                userValidator.getErrors().forEach(notification::addError);
            } else {
                notification.setResult(userRepository.updateUsernameById(id, newUsername).getResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
            notification.addError("Something went wrong with the database.");
        }

        return notification;
    }

}