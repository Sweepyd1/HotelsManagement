package com.example.course_work.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.course_work.models.User;

public class UserCrud {
    private Connection connection;

    public UserCrud(Connection connection) {
        this.connection = connection;
    }

    // Создание нового пользователя
    public void createUser(String name, String password,String login, String surname, String role) throws SQLException {
        String sql = "INSERT INTO users (username, userpassword,userlogin, usersurname,userrole ) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, login);
            pstmt.setString(4, surname);
            pstmt.setString(5, role);
            pstmt.executeUpdate();
            System.out.println("Пользователь успешно создан.");
        }
    }

    // Получение пользователя по ID
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE userid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("userid"), rs.getString("username"),rs.getString("usersurname"),rs.getString("userlogin"), rs.getString("userpassword"), rs.getString("userrole"));
            }
        }
        return null; // Пользователь не найден
    }

    // Обновление пользователя
    public void updateUser(int id, String username, String password, String role) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setInt(4, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Пользователь успешно обновлен.");
            } else {
                System.out.println("Пользователь не найден.");
            }
        }
    }

    // Удаление пользователя
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Пользователь успешно удален.");
            } else {
                System.out.println("Пользователь не найден.");
            }
        }
    }


    public boolean userExists(String username, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE userlogin = ? and userpassword = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public Integer getUserId(String username, String password) throws SQLException {
        String sql = "SELECT userid FROM users WHERE userlogin = ? AND userpassword = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("userid"); // Возвращаем ID пользователя
            }
        }
        return null; // Если пользователь не найден, возвращаем null
    }


    public String getUserRole(String username, String password) throws SQLException {
        String sql = "SELECT userrole FROM users WHERE userlogin = ? AND userpassword = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("userrole"); // Возвращаем ID пользователя
            }
        }
        return null; // Если пользователь не найден, возвращаем null
    }


    public List<User> getAllRoomData() {
        String selectRoomSQL = "SELECT * FROM users";
        List<User> rooms = new ArrayList<>();

        try (PreparedStatement roomPstmt = connection.prepareStatement(selectRoomSQL);
             ResultSet resultSet = roomPstmt.executeQuery()) { // Use executeQuery for SELECT

            while (resultSet.next()) {
                // Assuming Room class has a constructor that matches your table structure
                User user = new User(

                        resultSet.getInt("userid"),
                        resultSet.getString("username"),
                        resultSet.getString("usersurname"),
                        resultSet.getString("userpassword"),
                        resultSet.getString("userlogin"),
                        resultSet.getString("userrole")

                );
                rooms.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving room data: " + e.getMessage(), e);
        }

        return rooms; // Return the list of rooms
    }

}


