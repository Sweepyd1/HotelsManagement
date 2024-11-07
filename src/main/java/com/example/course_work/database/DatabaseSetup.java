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

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {


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


            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                    "UserID SERIAL PRIMARY KEY," +
                    "UserSurname VARCHAR(100) NOT NULL," +
                    "UserName VARCHAR(100) NOT NULL," +
                    "UserPatronymic VARCHAR(100) NOT NULL," +
                    "UserLogin VARCHAR(100) UNIQUE NOT NULL," +
                    "UserPassword VARCHAR(255) NOT NULL," +
                    "UserRole VARCHAR(50) NOT NULL CHECK (UserRole IN ('user', 'admin'))" +
                    ")";
            statement.executeUpdate(createUsersTable);


            String createRoomsTable = "CREATE TABLE IF NOT EXISTS Rooms (" +
                    "RoomID SERIAL PRIMARY KEY," +
                    "RoomNumber VARCHAR(10) UNIQUE NOT NULL," +
                    "RoomDescription TEXT NOT NULL," +
                    "RoomCapacity INT NOT NULL," +
                    "RoomCost DECIMAL(19, 4) NOT NULL," +
                    "RoomStatus VARCHAR(20) NOT NULL CHECK (RoomStatus IN ('open', 'booked'))," +
                    "RoomPhoto TEXT" +
                    ")";
            statement.executeUpdate(createRoomsTable);


            String createServicesTable = "CREATE TABLE IF NOT EXISTS Services (" +
                    "ServiceID SERIAL PRIMARY KEY," +
                    "ServiceName VARCHAR(100) NOT NULL UNIQUE," +
                    "ServiceDescription TEXT NOT NULL," +
                    "ServiceCost DECIMAL(19, 4) NOT NULL" +
                    ")";
            statement.executeUpdate(createServicesTable);

            String insertAdditionalServices = "INSERT INTO Services (ServiceName, ServiceDescription, ServiceCost) VALUES " +
                    "('Wi-Fi', 'Бесплатный доступ к Wi-Fi на всей территории.', 10.00), " +
                    "('Завтрак включен', 'Континентальный завтрак включен в стоимость номера.', 500.00), " +
                    "('Спа-процедуры', 'Доступ к спа-процедурам и массажам.', 1500.00), " +
                    "('Допускаются домашние животные', 'Разрешено проживание с домашними животными.', 1000.00) " +
                    "ON CONFLICT (ServiceName) DO NOTHING";

            statement.executeUpdate(insertAdditionalServices);


            String createBookingsTable = "CREATE TABLE IF NOT EXISTS Bookings (" +
                    "BookingID SERIAL PRIMARY KEY," +
                    "UserID INT NOT NULL REFERENCES Users(UserID)," +
                    "BookingDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "CheckInDate DATE NOT NULL," +
                    "CheckOutDate DATE NOT NULL," +
                    "BookingStatus VARCHAR(20) NOT NULL CHECK (BookingStatus IN ('confirmed', 'canceled'))" +
                    ")";
            statement.executeUpdate(createBookingsTable);


            String createBookingRoomsTable = "CREATE TABLE IF NOT EXISTS BookingRooms (" +
                    "BookingID INT NOT NULL REFERENCES Bookings(BookingID)," +
                    "RoomID INT NOT NULL REFERENCES Rooms(RoomID)," +
                    "PRIMARY KEY (BookingID, RoomID)" +
                    ")";
            statement.executeUpdate(createBookingRoomsTable);


            String createBookingServicesTable = "CREATE TABLE IF NOT EXISTS BookingServices (" +
                    "BookingID INT NOT NULL REFERENCES Bookings(BookingID)," +
                    "ServiceID INT NOT NULL REFERENCES Services(ServiceID)," +
                    "PRIMARY KEY (BookingID, ServiceID)" +
                    ")";
            statement.executeUpdate(createBookingServicesTable);

            System.out.println("Таблицы созданы или уже существуют.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}