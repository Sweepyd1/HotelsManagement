package com.example.course_work.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_NAME = "hotel";
    private static final String USER = "postgres";
    private static final String PASSWORD = "sweepy2006";

    public DatabaseSetup() {
        createDatabaseAndTables();
    }

    private void createDatabaseAndTables() {
        // Создание базы данных
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Создание базы данных, если она не существует
            String createDatabaseQuery = "CREATE DATABASE " + DB_NAME;
            try {
                statement.executeUpdate(createDatabaseQuery);
                System.out.println("База данных создана.");
            } catch (SQLException e) {
                System.out.println("База данных уже существует или ошибка при создании.");
            }

            // Подключение к созданной базе данных
            try (Connection dbConnection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD)) {
                createTables(dbConnection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {

            // Создание таблицы пользователей
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL UNIQUE," +
                    "password VARCHAR(255) NOT NULL," +
                    "role VARCHAR(20) CHECK (role IN ('client', 'employee')) NOT NULL" +
                    ")";
            statement.executeUpdate(createUsersTable);

            // Создание таблицы категорий
            String createCategoriesTable = "CREATE TABLE IF NOT EXISTS categories (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(50) UNIQUE NOT NULL" +
                    ")";
            statement.executeUpdate(createCategoriesTable);

            // Создание таблицы производителей
            String createManufacturersTable = "CREATE TABLE IF NOT EXISTS manufacturers (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(100) UNIQUE NOT NULL," +
                    "contact_info TEXT" +
                    ")";
            statement.executeUpdate(createManufacturersTable);

            // Создание таблицы товаров
            String createProductsTable = "CREATE TABLE IF NOT EXISTS products (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "description TEXT NOT NULL," +
                    "price DECIMAL(10, 2) NOT NULL," +
                    "stock_quantity INT NOT NULL," +
                    "photo_url VARCHAR(255)," +
                    "category_id INT REFERENCES categories(id)," +  // Связь с категориями
                    "manufacturer_id INT REFERENCES manufacturers(id)" + // Связь с производителями
                    ")";
            statement.executeUpdate(createProductsTable);

            // Создание таблицы заказов
            String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                    "id SERIAL PRIMARY KEY," +
                    "user_id INT REFERENCES users(id)," +
                    "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            statement.executeUpdate(createOrdersTable);

            // Создание таблицы позиций заказа
            String createOrderItemsTable = "CREATE TABLE IF NOT EXISTS order_items (" +
                    "id SERIAL PRIMARY KEY," +
                    "order_id INT REFERENCES orders(id)," +
                    "product_id INT REFERENCES products(id)," +
                    "quantity INT NOT NULL," +
                    "price_at_order_time DECIMAL(10, 2) NOT NULL" + // Хранение цены на момент заказа
                    ")";
            statement.executeUpdate(createOrderItemsTable);

            System.out.println("Таблицы созданы или уже существуют.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}