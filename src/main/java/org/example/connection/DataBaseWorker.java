package org.example.connection;

import org.example.entity.User;

import java.sql.*;

public class DataBaseWorker {
    private static final String URL = "jdbc:postgresql://192.168.0.129:5432/mydb";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    public User getUserByLogin(String login) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = connection.createStatement();
            String query = "SELECT * FROM users u JOIN emails e ON u.login = e.login WHERE u.login = '" + login + "'";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new User(
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getDate("date")
                );
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
            }
        }
        return null;
    }

    public int insertUser(User user) {
        String insertUserSQL = "INSERT INTO users (login, password, date) VALUES (?, ?, ?)";
        String insertEmailSQL = "INSERT INTO emails (login, email) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement userStatement = connection.prepareStatement(insertUserSQL);
             PreparedStatement emailStatement = connection.prepareStatement(insertEmailSQL)) {

            connection.setAutoCommit(false);

            userStatement.setString(1, user.getLogin());
            userStatement.setString(2, user.getPassword());
            userStatement.setDate(3, new java.sql.Date(user.getRegistrationDate().getTime()));
            int userRows = userStatement.executeUpdate();

            emailStatement.setString(1, user.getLogin());
            emailStatement.setString(2, user.getEmail());
            int emailRows = emailStatement.executeUpdate();

            connection.commit();

            return userRows + emailRows;
        } catch (SQLException e) {
            System.err.println("Ошибка при вставке пользователя: " + e.getMessage());
            return 0;
        }
    }
}
