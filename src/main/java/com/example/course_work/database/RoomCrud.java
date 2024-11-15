package com.example.course_work.database;

import com.example.course_work.SessionManager;
import com.example.course_work.models.BookedRoom;
import com.example.course_work.models.Room;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomCrud {

    private Connection connection;

    public RoomCrud(Connection connection) {
        this.connection = connection;
    }

    public List<Room> getFilteredRooms() throws SQLException {
        String sql = "SELECT * FROM Rooms WHERE RoomID NOT IN (SELECT br.RoomID FROM BookingRooms br " +
                "JOIN Bookings b ON br.BookingID = b.BookingID " +
                "WHERE (b.CheckInDate < ? AND b.CheckOutDate > ?))";

        // Получаем параметры фильтрации из SessionManager
        LocalDate checkInDate = SessionManager.getInstance().getInDate();
        LocalDate checkOutDate = SessionManager.getInstance().getOutDate();
        double minPrice = SessionManager.getInstance().getMinPrice();
        double maxPrice = SessionManager.getInstance().getMaxPrice();
        int capacity = SessionManager.getInstance().getCapacity();
        boolean wifi = SessionManager.getInstance().isWifi();
        boolean breakfast = SessionManager.getInstance().isBreakfast();
        boolean spa = SessionManager.getInstance().isSpa();
        boolean petFriendly = SessionManager.getInstance().isPetFriendly();

        // Добавляем условия фильтрации
        List<String> conditions = new ArrayList<>();

        if (minPrice >= 0) {
            conditions.add("RoomCost >= ?");
        }

        if (maxPrice >= 0) {
            conditions.add("RoomCost <= ?");
        }

        if (capacity > 0) {
            conditions.add("RoomCapacity >= ?");
        }

        // Добавляем условия для удобств
        if (wifi) {
            conditions.add("ServiceID IN (SELECT ServiceID FROM Services WHERE ServiceName = 'Wi-Fi')");
        }

        if (breakfast) {
            conditions.add("ServiceID IN (SELECT ServiceID FROM Services WHERE ServiceName = 'Завтрак включен')");
        }

        if (spa) {
            conditions.add("ServiceID IN (SELECT ServiceID FROM Services WHERE ServiceName = 'Спа-процедуры')");
        }

        if (petFriendly) {
            conditions.add("ServiceID IN (SELECT ServiceID FROM Services WHERE ServiceName = 'Допускаются домашние животные')");
        }

        // Добавляем условия к SQL запросу
        if (!conditions.isEmpty()) {
            sql += " AND " + String.join(" AND ", conditions);
        }

        List<Room> filteredRooms = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int index = 1;

            // Устанавливаем параметры для проверки дат
            pstmt.setDate(index++, Date.valueOf(checkOutDate)); // Дата выезда
            pstmt.setDate(index++, Date.valueOf(checkInDate)); // Дата заезда

            if (minPrice >= 0) {
                pstmt.setDouble(index++, minPrice);
            }

            if (maxPrice >= 0) {
                pstmt.setDouble(index++, maxPrice);
            }

            if (capacity > 0) {
                pstmt.setInt(index++, capacity);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String roomNumber = rs.getString("RoomNumber");
                String description = rs.getString("RoomDescription");
                int roomCapacity = rs.getInt("RoomCapacity");
                double price = rs.getDouble("RoomCost");
                String photo = rs.getString("RoomPhoto");

                Room room = new Room(roomNumber, description, roomCapacity, price, photo);
                filteredRooms.add(room);
            }
        }

        return filteredRooms;
    }

    public void bookRoom(int userId, int roomId, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        // Проверка наличия существующих броней для данной комнаты
        String checkBookingSQL = "SELECT COUNT(*) FROM BookingRooms br JOIN Bookings b ON br.BookingID = b.BookingID " +
                "WHERE br.RoomID = ? AND ((b.CheckInDate < ? AND b.CheckOutDate > ?) OR " +
                "(b.CheckInDate < ? AND b.CheckOutDate > ?))";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkBookingSQL)) {
            checkStmt.setInt(1, roomId);
            checkStmt.setDate(2, java.sql.Date.valueOf(checkOutDate)); // Дата выезда
            checkStmt.setDate(3, java.sql.Date.valueOf(checkInDate)); // Дата заезда
            checkStmt.setDate(4, java.sql.Date.valueOf(checkOutDate)); // Дата выезда
            checkStmt.setDate(5, java.sql.Date.valueOf(checkInDate)); // Дата заезда

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Ошибка: Комната уже забронирована на эти даты.");
            }
        }

        // Обновление статуса комнаты на 'booked'
//        String updateRoomStatusSQL = "UPDATE Rooms SET RoomStatus = 'booked' WHERE RoomID = ?";
//        try (PreparedStatement updateRoomStmt = connection.prepareStatement(updateRoomStatusSQL)) {
//            updateRoomStmt.setInt(1, roomId);
//            updateRoomStmt.executeUpdate();
//        }

        // Добавление записи в таблицу заказов
        String insertBookingSQL = "INSERT INTO Bookings (UserID, CheckInDate, CheckOutDate, BookingStatus) VALUES (?, ?, ?, 'confirmed') RETURNING BookingID";
        int bookingId;
        try (PreparedStatement insertBookingStmt = connection.prepareStatement(insertBookingSQL)) {
            insertBookingStmt.setInt(1, userId);
            insertBookingStmt.setDate(2, java.sql.Date.valueOf(checkInDate));
            insertBookingStmt.setDate(3, java.sql.Date.valueOf(checkOutDate));

            ResultSet rs = insertBookingStmt.executeQuery();
            if (rs.next()) {
                bookingId = rs.getInt("BookingID");
                System.out.println("Бронирование добавлено с ID: " + bookingId);
            } else {
                throw new SQLException("Не удалось создать бронирование.");
            }
        }

        // Добавление записи о комнате в таблицу BookingRooms
        String insertBookingRoomSQL = "INSERT INTO BookingRooms (BookingID, RoomID) VALUES (?, ?)";
        try (PreparedStatement insertBookingRoomStmt = connection.prepareStatement(insertBookingRoomSQL)) {
            insertBookingRoomStmt.setInt(1, bookingId);
            insertBookingRoomStmt.setInt(2, roomId);
            insertBookingRoomStmt.executeUpdate();
            System.out.println("Комната добавлена в бронь.");
        }
    }


    public int getSelectedRoomIdByTitle(String roomTitle) throws SQLException {
        String sql = "SELECT RoomID FROM Rooms WHERE RoomNumber = ?";
        int roomId = -1; // Изначально устанавливаем в -1, чтобы обозначить, что комната не найдена

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, roomTitle); // Устанавливаем номер комнаты в запрос
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                roomId = rs.getInt("RoomID"); // Получаем ID комнаты
            }
        }

        return roomId; // Возвращаем ID или -1, если комната не найдена
    }


    public List<BookedRoom> getBookedRoomsForUser(int userId) throws SQLException {
        String sql = "SELECT " +
                "r.RoomID, " +
                "r.RoomNumber, " +
                "r.RoomDescription, " +
                "r.RoomCapacity, " +
                "r.RoomCost, " +
                "r.RoomPhoto, " +
                "b.CheckInDate, " +
                "b.bookingDate, "+
                "b.CheckOutDate, " +
                "b.BookingStatus " +
                "FROM Bookings b " +
                "JOIN BookingRooms br ON b.BookingID = br.BookingID " +
                "JOIN Rooms r ON br.RoomID = r.RoomID " +
                "WHERE b.UserID = ?";

        List<BookedRoom> bookedRooms = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId); // Устанавливаем ID пользователя
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Создаем объект Room с данными из результата запроса
                int roomId = rs.getInt("RoomID");
                String roomNumber = rs.getString("RoomNumber");
                String description = rs.getString("RoomDescription");
                int roomCapacity = rs.getInt("RoomCapacity");
                double roomCost = rs.getDouble("RoomCost");
                String roomPhoto = rs.getString("RoomPhoto");
                LocalDate checkInDate = rs.getDate("CheckInDate").toLocalDate();
                LocalDate bookingDate = rs.getDate("bookingDate").toLocalDate();
                LocalDate checkOutDate = rs.getDate("CheckOutDate").toLocalDate();
                String bookingStatus = rs.getString("BookingStatus");

                BookedRoom room = new BookedRoom(roomNumber, description, roomCapacity, roomCost,roomPhoto, checkInDate, checkOutDate, bookingDate);
                bookedRooms.add(room);
            }
        }
        System.out.println("booked rooms: "+ bookedRooms);

        return bookedRooms;
    }

    public void deletedBooked(int userId, LocalDate checkinDate, LocalDate checkoutDate) throws SQLException {
        String getBookingIdSql = "SELECT BookingID FROM Bookings WHERE UserID = ? AND CheckInDate = ? AND CheckOutDate = ?";
        String deleteBookingRoomsSql = "DELETE FROM BookingRooms WHERE BookingID = ?";
        String deleteBookingSql = "DELETE FROM Bookings WHERE BookingID = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hotel", "postgres", "sweepy2006")) {
            connection.setAutoCommit(false); // Начинаем транзакцию

            int bookingId = -1; // Идентификатор бронирования

            // Получаем BookingID
            try (PreparedStatement preparedStatement = connection.prepareStatement(getBookingIdSql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setDate(2, java.sql.Date.valueOf(checkinDate));
                preparedStatement.setDate(3, java.sql.Date.valueOf(checkoutDate));

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    bookingId = resultSet.getInt("BookingID");
                }
            }

            // Если найдено бронирование, удаляем его
            if (bookingId != -1) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteBookingRoomsSql)) {
                    preparedStatement.setInt(1, bookingId);
                    int rowsAffectedRooms = preparedStatement.executeUpdate();
                    System.out.println(rowsAffectedRooms + " booking room(s) deleted.");
                }

                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteBookingSql)) {
                    preparedStatement.setInt(1, bookingId);
                    int rowsAffectedBookings = preparedStatement.executeUpdate();
                    if (rowsAffectedBookings > 0) {
                        System.out.println("Booking deleted successfully.");
                    } else {
                        System.out.println("No booking found to delete.");
                    }
                }
            } else {
                System.out.println("No booking found for the given criteria.");
            }

            connection.commit(); // Подтверждаем транзакцию
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Повторно выбрасываем исключение
        }
    }
}





