package com.example.course_work.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCONN {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_NAME = "hotel";
    private static final String USER = "postgres";
    private static final String PASSWORD = "sweepy2006";


    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}