package com.example.course_work.database;

import java.math.BigDecimal;
import java.sql.*;

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

            // Создание таблицы пользователей
            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                    "UserID SERIAL PRIMARY KEY," +
                    "UserSurname TEXT NOT NULL," +
                    "UserName TEXT NOT NULL," +

                    "UserLogin TEXT UNIQUE NOT NULL," +
                    "UserPassword TEXT NOT NULL," +
                    "UserRole TEXT NOT NULL CHECK (UserRole IN ('user', 'admin'))" +
                    ")";
            statement.executeUpdate(createUsersTable);

            // Создание таблицы услуг
            String createServicesTable = "CREATE TABLE IF NOT EXISTS Services (" +
                    "ServiceID SERIAL PRIMARY KEY," +
                    "ServiceName TEXT NOT NULL UNIQUE," +
                    "ServiceDescription TEXT NOT NULL," +
                    "ServiceCost DECIMAL(19, 4) NOT NULL" +
                    ")";
            statement.executeUpdate(createServicesTable);

            // Вставка дополнительных услуг
            String insertAdditionalServices = "INSERT INTO Services (ServiceName, ServiceDescription, ServiceCost) VALUES " +
                    "('Wi-Fi', 'Бесплатный доступ к Wi-Fi на всей территории.', 10.00), " +
                    "('Завтрак включен', 'Континентальный завтрак включен в стоимость номера.', 500.00), " +
                    "('Спа-процедуры', 'Доступ к спа-процедурам и массажам.', 1500.00), " +
                    "('Допускаются домашние животные', 'Разрешено проживание с домашними животными.', 1000.00) " +
                    "ON CONFLICT (ServiceName) DO NOTHING";
            statement.executeUpdate(insertAdditionalServices);

            // Создание таблицы комнат с колонкой ServiceID
            String createRoomsTable = "CREATE TABLE IF NOT EXISTS Rooms (" +
                    "RoomID SERIAL PRIMARY KEY," +
                    "RoomNumber TEXT UNIQUE NOT NULL," +
                    "RoomDescription TEXT NOT NULL," +
                    "RoomCapacity INT NOT NULL," +
                    "RoomCost INT NOT NULL," +
                    "RoomPhoto TEXT," +
                    "ServiceID INT REFERENCES Services(ServiceID)" + // Добавляем колонку ServiceID
                    ")";
            statement.executeUpdate(createRoomsTable);

            // Создание таблицы броней
            String createBookingsTable = "CREATE TABLE IF NOT EXISTS Bookings (" +
                    "BookingID SERIAL PRIMARY KEY," +
                    "UserID INT NOT NULL REFERENCES Users(UserID)," +
                    "BookingDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "CheckInDate DATE NOT NULL," +
                    "CheckOutDate DATE NOT NULL," +
                    "peoples TEXT[] DEFAULT NULL"+
                    ")";
            statement.executeUpdate(createBookingsTable);


            String createRejectionTable = "CREATE TABLE IF NOT EXISTS reject (" +
                    "rejectId SERIAL PRIMARY KEY," +
                    "UserID INT NOT NULL REFERENCES Users(UserID)," +
                    "description TEXT NOT NULL," +
                    "roomId INT NOT NULL REFERENCES Rooms(roomId)" +
                    ")";
            statement.executeUpdate(createRejectionTable);

            //                    "BookingStatus TEXT NOT NULL CHECK (BookingStatus IN ('confirmed', 'canceled'))" +

            // Создание таблицы комнат в броне
            String createBookingRoomsTable = "CREATE TABLE IF NOT EXISTS BookingRooms (" +
                    "BookingID INT NOT NULL REFERENCES Bookings(BookingID)," +
                    "RoomID INT NOT NULL REFERENCES Rooms(RoomID)," +
                    "PRIMARY KEY (BookingID, RoomID)" +
                    ")";
            statement.executeUpdate(createBookingRoomsTable);

            createTrigger(statement);

            // Создание таблицы услуг в броне
//            String createBookingServicesTable = "CREATE TABLE IF NOT EXISTS BookingServices (" +
//                    "BookingID INT NOT NULL REFERENCES Bookings(BookingID)," +
//                    "ServiceID INT NOT NULL REFERENCES Services(ServiceID)," +
//                    "PRIMARY KEY (BookingID, ServiceID)" +
//                    ")";
//            statement.executeUpdate(createBookingServicesTable);

            // Добавление 20 комнат с различными услугами
            for (int i = 1; i <= 20; i++) {
                String insertRoomSQL =
                        "INSERT INTO Rooms (RoomNumber, RoomDescription, RoomCapacity, RoomCost,RoomPhoto, ServiceID) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement roomPstmt = connection.prepareStatement(insertRoomSQL)) {

                    roomPstmt.setString(1, String.valueOf(100 + i)); // Номер комнаты
                    roomPstmt.setString(2, "Описание номера #" + i); // Описание номера
                    roomPstmt.setInt(3, 2);
                    roomPstmt.setInt(4, 3000);


                    roomPstmt.setString(5, "1.png"); // Фото (можно указать путь к фото или оставить null)

                    // Случайный выбор услуги из таблицы Services
                    int randomServiceId = (int) (Math.random() * 4) + 1; // Предполагая, что у вас 4 услуги с ID от 1 до 4
                    roomPstmt.setInt(6, randomServiceId);

                    roomPstmt.executeUpdate();
                }
            }



            System.out.println("Таблицы созданы или уже существуют и заполнены.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTrigger(Statement statement) {
        // Create the function if it doesn't exist
        String createFunctionSQL = "CREATE OR REPLACE FUNCTION prevent_booking_deletion() " +
                "RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "IF (OLD.CheckInDate::TIMESTAMP - CURRENT_TIMESTAMP) < INTERVAL '24 hours' THEN " +
                "RAISE EXCEPTION 'Cannot delete booking less than 24 hours before check-in'; " +
                "END IF; " +
                "RETURN OLD; " +
                "END; $$ LANGUAGE plpgsql;";

        try {
            statement.executeUpdate(createFunctionSQL);
        } catch (SQLException e) {
            // Ignore if function already exists or handle accordingly
            System.out.println("Функция prevent_booking_deletion уже существует или ошибка при создании.");
        }

        // Create the trigger
        String createTriggerSQL = "CREATE TRIGGER prevent_booking_deletion_trigger " +
                "BEFORE DELETE ON Bookings " +
                "FOR EACH ROW EXECUTE FUNCTION prevent_booking_deletion();";

        try {
            statement.executeUpdate(createTriggerSQL);
        } catch (SQLException e) {
            // Ignore if trigger already exists or handle accordingly
            System.out.println("Триггер prevent_booking_deletion_trigger уже существует или ошибка при создании.");
        }
    }


}