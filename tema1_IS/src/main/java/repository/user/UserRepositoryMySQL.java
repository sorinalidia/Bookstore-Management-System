package repository.user;

import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();

        try {
            String fetchAllUsersSql = "SELECT id, username FROM user";

            try (PreparedStatement preparedStatement = connection.prepareStatement(fetchAllUsersSql);
                 ResultSet userResultSet = preparedStatement.executeQuery()) {

                while (userResultSet.next()) {
                    User user = new UserBuilder()
                            .setId(userResultSet.getLong("id"))
                            .setUsername(userResultSet.getString("username"))
                            .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                            .build();

                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }


    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();

        try {
            String fetchUserSql = "SELECT * FROM user WHERE `username`=? AND `password`=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet userResultSet = preparedStatement.executeQuery();

                if (userResultSet.next()) {
                    User user = new UserBuilder()
                            .setUsername(userResultSet.getString("username"))
                            .setPassword(userResultSet.getString("password"))
                            .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                            .build();

                    findByUsernameAndPasswordNotification.setResult(user);
                } else {
                    findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public Notification<Boolean> save(User user) {
        Notification<Boolean> saveNotification = new Notification<>();

        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?,?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            if(rs.next()) {
                long userId = rs.getLong(1);
                user.setId(userId);
                rightsRolesRepository.addRolesToUser(user, user.getRoles());
                saveNotification.setResult(true);
            }else{
                saveNotification.addError("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            saveNotification.addError("Something is wrong with the database!");
        }

        return saveNotification;
    }

    @Override
    public void removeAll() {
        try {
            String deleteAllUsersSql = "DELETE FROM user WHERE id >= 0";

            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteAllUsersSql)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean existsByUsername(String email) {
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + email + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Long findUserIdByUsername(String username) {
        try {
            String fetchUserIdSql = "SELECT id FROM `" + USER + "` WHERE `username`=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(fetchUserIdSql)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getLong("id");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existsByRoleTitle(String role) {
        try {
            String sql = "SELECT COUNT(*) FROM user_role ur " +
                    "JOIN user u ON ur.user_id = u.id " +
                    "JOIN role r ON ur.role_id = r.id " +
                    "WHERE r.role = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, role);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> findAllEmployees() {
        List<User> employeeList = new ArrayList<>();

        try {
            String fetchAllEmployeesSql = "SELECT u.id, u.username " +
                    "FROM user u " +
                    "JOIN user_role ur ON u.id = ur.user_id " +
                    "JOIN role r ON ur.role_id = r.id " +
                    "WHERE r.role = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(fetchAllEmployeesSql)) {
                preparedStatement.setString(1, "EMPLOYEE");

                try (ResultSet employeeResultSet = preparedStatement.executeQuery()) {
                    while (employeeResultSet.next()) {
                        User employee = new UserBuilder()
                                .setId(employeeResultSet.getLong("id"))
                                .setUsername(employeeResultSet.getString("username"))
                                .setRoles(rightsRolesRepository.findRolesForUser(employeeResultSet.getLong("id")))
                                .build();

                        employeeList.add(employee);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeList;
    }

    @Override
    public boolean removeUserById(Long id) {
        try {
            String deleteUserSql = "DELETE FROM user WHERE id = ?";
            try (PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserSql)) {
                deleteUserStatement.setLong(1, id);
                int affectedRows = deleteUserStatement.executeUpdate();

                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Notification<Boolean> updatePasswordById(Long id, String newPassword) {
        Notification<Boolean> updateNotification = new Notification<>();

        try {
            String updatePasswordSql = "UPDATE user SET password = ? WHERE id = ?";
            try (PreparedStatement updatePasswordStatement = connection.prepareStatement(updatePasswordSql)) {
                updatePasswordStatement.setString(1, newPassword);
                updatePasswordStatement.setLong(2, id);

                int affectedRows = updatePasswordStatement.executeUpdate();

                if (affectedRows > 0) {
                    updateNotification.setResult(true);
                } else {
                    updateNotification.addError("User not found or password update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            updateNotification.addError("Something is wrong with the database!");
        }

        return updateNotification;
    }
    @Override
    public Notification<Boolean> updateUsernameById(Long employeeId, String newUsername) {
        Notification<Boolean> notification = new Notification<>();

        try {
            String updateUsernameSql = "UPDATE user SET username = ? WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateUsernameSql)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setLong(2, employeeId);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    notification.setResult(true);
                } else {
                    notification.addError("Failed to update employee username. Employee not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            notification.addError("Something went wrong with the database.");
        }

        return notification;
    }


}