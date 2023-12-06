package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.*;

public class RightsRolesRepositoryMySQL implements RightsRolesRepository {

    private final Connection connection;

    public RightsRolesRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRole(String role) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ROLE + " values (null, ?)");
            insertStatement.setString(1, role);
            insertStatement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public void addRight(String right) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO `" + RIGHT + "` values (null, ?)");
            insertStatement.setString(1, right);
            insertStatement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public Role findRoleByTitle(String role) {
        PreparedStatement preparedStatement = null;
        ResultSet roleResultSet = null;

        try {
            String fetchRoleSql = "SELECT * FROM " + ROLE + " WHERE `role` = ?";
            preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setString(1, role);

            roleResultSet = preparedStatement.executeQuery();

            if (roleResultSet.next()) {
                Long roleId = roleResultSet.getLong("id");
                String roleTitle = roleResultSet.getString("role");
                return new Role(roleId, roleTitle, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (roleResultSet != null) {
                    roleResultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Role findRoleById(Long roleId) {
        PreparedStatement preparedStatement = null;
        ResultSet roleResultSet = null;

        try {
            String fetchRoleSql = "SELECT * FROM " + ROLE + " WHERE `id` = ?";
            preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setLong(1, roleId);

            roleResultSet = preparedStatement.executeQuery();

            if (roleResultSet.next()) {
                String roleTitle = roleResultSet.getString("role");
                return new Role(roleId, roleTitle, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (roleResultSet != null) {
                    roleResultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Right findRightByTitle(String right) {
        PreparedStatement preparedStatement = null;
        ResultSet rightResultSet = null;

        try {
            String fetchRoleSql = "SELECT * FROM `" + RIGHT + "` WHERE `right` = ?";
            preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setString(1, right);

            rightResultSet = preparedStatement.executeQuery();

            if (rightResultSet.next()) {
                Long rightId = rightResultSet.getLong("id");
                String rightTitle = rightResultSet.getString("right");
                return new Right(rightId, rightTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rightResultSet != null) {
                    rightResultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        try {
            for (Role role : roles) {
                PreparedStatement insertUserRoleStatement = connection
                        .prepareStatement("INSERT INTO `user_role` VALUES (null, ?, ?)");
                insertUserRoleStatement.setLong(1, user.getId());
                insertUserRoleStatement.setLong(2, role.getId());
                insertUserRoleStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Role> findRolesForUser(Long userId) {
        PreparedStatement preparedStatement = null;
        ResultSet userRoleResultSet = null;

        try {
            List<Role> roles = new ArrayList<>();
            String fetchRoleSql = "SELECT * FROM " + USER_ROLE + " WHERE `user_id` = ?";
            preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setLong(1, userId);

            userRoleResultSet = preparedStatement.executeQuery();

            while (userRoleResultSet.next()) {
                long roleId = userRoleResultSet.getLong("role_id");
                roles.add(findRoleById(roleId));
            }

            return roles;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (userRoleResultSet != null) {
                    userRoleResultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    @Override
    public void addRoleRight(Long roleId, Long rightId) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ROLE_RIGHT + " values (null, ?, ?)");
            insertStatement.setLong(1, roleId);
            insertStatement.setLong(2, rightId);
            insertStatement.executeUpdate();
        } catch (SQLException e) {

        }
    }
}