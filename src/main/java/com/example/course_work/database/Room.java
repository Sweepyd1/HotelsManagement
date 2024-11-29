package com.example.course_work.database;

import com.example.course_work.Session;
import com.example.course_work.models.BookedRoom;
import com.example.course_work.models.BookingInfo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {

    private Connection connection;

    public Room(Connection connection) {
        this.connection = connection;
    }

    public List<com.example.course_work.models.Room> getFilteredRooms() throws SQLException {
        String sql = "SELECT * FROM Rooms WHERE RoomID NOT IN (SELECT br.RoomID FROM BookingRooms br " +
                "JOIN Bookings b ON br.BookingID = b.BookingID " +
                "WHERE (b.CheckInDate < ? AND b.CheckOutDate > ?))";

        // Получаем параметры фильтрации из SessionManager
        LocalDate checkInDate = Session.getInstance().getInDate();
        LocalDate checkOutDate = Session.getInstance().getOutDate();
        double minPrice = Session.getInstance().getMinPrice();
        double maxPrice = Session.getInstance().getMaxPrice();
        int capacity = Session.getInstance().getCapacity();
        boolean wifi = Session.getInstance().isWifi();
        boolean breakfast = Session.getInstance().isBreakfast();
        boolean spa = Session.getInstance().isSpa();
        boolean petFriendly = Session.getInstance().isPetFriendly();

        
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

        List<com.example.course_work.models.Room> filteredRooms = new ArrayList<>();

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
                int price = rs.getInt("RoomCost");
                String photo = rs.getString("RoomPhoto");
                int serviceId = rs.getInt("serviceid");

                com.example.course_work.models.Room room = new com.example.course_work.models.Room(roomNumber, description, roomCapacity, price, photo, serviceId);
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
        String insertBookingSQL = "INSERT INTO Bookings (UserID, CheckInDate, CheckOutDate) VALUES (?, ?, ?) RETURNING BookingID";
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
                "b.bookingDate, " +
                "b.CheckOutDate " +
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
                int bookingDate = rs.getInt("roomcost");
                LocalDate checkOutDate = rs.getDate("CheckOutDate").toLocalDate();
//                String bookingStatus = rs.getString("BookingStatus");

                BookedRoom room = new BookedRoom(roomNumber, description, roomCapacity, roomCost, roomPhoto, checkInDate, checkOutDate, bookingDate);
                bookedRooms.add(room);
            }
        }
        System.out.println("booked rooms: " + bookedRooms);

        return bookedRooms;
    }

    public void deletedBooked(int userId, LocalDate checkinDate, LocalDate checkoutDate) throws SQLException {
        String getBookingIdSql = "SELECT BookingID FROM Bookings WHERE UserID = ? AND CheckInDate = ? AND CheckOutDate = ?";
        String deleteBookingRoomsSql = "DELETE FROM BookingRooms WHERE BookingID = ?";
        String deleteBookingSql = "DELETE FROM Bookings WHERE BookingID = ?";

        try (Connection connection = DBCONN.getConnection()) {
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


    public void addNewRoom(String name, String description, int capacity, int cost, String photo, int serviceId) {
        String insertRoomSQL =
                "INSERT INTO Rooms (RoomNumber, RoomDescription, RoomCapacity, RoomCost,RoomPhoto, ServiceID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement roomPstmt = connection.prepareStatement(insertRoomSQL)) {

            roomPstmt.setString(1, name);
            roomPstmt.setString(2, description);
            roomPstmt.setInt(3, capacity);
            roomPstmt.setInt(4, cost);
            roomPstmt.setString(5, photo);
            roomPstmt.setInt(6, serviceId);

            roomPstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateRoomData(String name, String description, int capacity, int cost, String photo, int serviceId, String oldName) {
        int roomId; // Variable to hold the found room ID

        // First, find the Room ID based on RoomNumber
        System.out.println("old name: " + oldName);
        String selectRoomSQL = "SELECT RoomID FROM Rooms WHERE RoomNumber = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectRoomSQL)) {
            selectStmt.setString(1, oldName); // Assuming 'name' is actually the RoomNumber

            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                roomId = resultSet.getInt("RoomID"); // Get the RoomID
            } else {
                System.out.println("No room found with the specified RoomNumber.");
                return; // Exit if no room is found
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving room ID: " + e.getMessage(), e);
        }

        // Now update the room using the found RoomID
        String updateRoomSQL = "UPDATE Rooms SET roomNumber = ?, RoomDescription = ?, RoomCapacity = ?, RoomCost = ?, RoomPhoto = ?, ServiceID = ? WHERE RoomID = ?";

        try (PreparedStatement updateStmt = connection.prepareStatement(updateRoomSQL)) {
            updateStmt.setString(1, name);
            updateStmt.setString(2, description);
            updateStmt.setInt(3, capacity);
            updateStmt.setInt(4, cost);
            updateStmt.setString(5, photo);
            updateStmt.setInt(6, serviceId);
            updateStmt.setInt(7, roomId);

            int affectedRows = updateStmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Room updated successfully.");
            } else {
                System.out.println("No changes were made to the room.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating room data: " + e.getMessage(), e);
        }
    }


    public void deleteRoom(String roomNumber) {
        String insertRoomSQL =
                "DELETE from Rooms WHERE roomNumber = ?";
        try (PreparedStatement roomPstmt = connection.prepareStatement(insertRoomSQL)) {

            roomPstmt.setString(1, roomNumber);
            roomPstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<com.example.course_work.models.Room> getAllRoomData() {
        String selectRoomSQL = "SELECT * FROM Rooms";
        List<com.example.course_work.models.Room> rooms = new ArrayList<>();

        try (PreparedStatement roomPstmt = connection.prepareStatement(selectRoomSQL);
             ResultSet resultSet = roomPstmt.executeQuery()) { // Use executeQuery for SELECT

            while (resultSet.next()) {
                // Assuming Room class has a constructor that matches your table structure
                com.example.course_work.models.Room room = new com.example.course_work.models.Room(

                        resultSet.getString("RoomNumber"),
                        resultSet.getString("RoomDescription"),
                        resultSet.getInt("RoomCapacity"),
                        resultSet.getInt("RoomCost"),
                        resultSet.getString("RoomPhoto"),
                        resultSet.getInt("ServiceID")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving room data: " + e.getMessage(), e);
        }

        return rooms; // Return the list of rooms
    }

    public List<BookingInfo> getAllBookings() {
        String query = "SELECT b.BookingID, u.UserID, u.UserName, u.UserSurname, "
                + "b.CheckInDate, b.CheckOutDate, b.BookingDate, "
                + "(b.CheckOutDate - b.CheckInDate) AS countDay, "
                + "(r.RoomCost * (b.CheckOutDate - b.CheckInDate) + COALESCE(s.ServiceCost, 0) * (b.CheckOutDate - b.CheckInDate)) AS total_price, "
                + "r.RoomNumber "
                + "FROM Bookings b "
                + "JOIN Users u ON b.UserID = u.UserID "
                + "JOIN BookingRooms br ON b.BookingID = br.BookingID "
                + "JOIN Rooms r ON br.RoomID = r.RoomID "
                + "LEFT JOIN Services s ON r.ServiceID = s.ServiceID"; // Добавлено соединение с таблицей Services

        List<BookingInfo> bookings = new ArrayList<>();
        // Остальная часть кода для выполнения запроса и обработки результатов


        try (Connection connection = DBCONN.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                BookingInfo booking = new BookingInfo(
                        rs.getInt("BookingID"),
                        rs.getInt("UserID"),
                        rs.getString("UserName"),
                        rs.getString("UserSurname"),
                        rs.getDate("CheckInDate"),
                        rs.getDate("CheckOutDate"),
                        rs.getTimestamp("BookingDate"),
                        rs.getInt("countDay"), // Это будет разница в днях
                        rs.getDouble("total_price"),
                        rs.getString("roomNumber")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }


    public boolean isRoomBooked(String roomname) throws SQLException {
        int roomId = this.getSelectedRoomIdByTitle(roomname);
        String query = "SELECT COUNT(*) FROM bookingrooms WHERE roomid = ?"; // Предполагаем, что у вас есть таблица bookings с полем room_id
        try (Connection connection = DBCONN.getConnection())
             {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, roomId); // Устанавливаем ID комнаты в запрос
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Возвращаем true, если есть бронирования
                }
            }
            return false; // Возвращаем false, если нет бронирований
        }
        }

}







