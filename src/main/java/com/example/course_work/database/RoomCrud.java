package com.example.course_work.database;

import com.example.course_work.models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomCrud {

    private Connection connection;

    public RoomCrud(Connection connection) {
        this.connection = connection;
    }

    public List<Room> getFreeRooms() throws SQLException {
        String sql = "SELECT * FROM rooms WHERE roomstatus = 'open'";
        List<Room> freeRooms = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Предполагается, что в таблице есть соответствующие поля
                String roomNumber = rs.getString("roomnumber");
                String description = rs.getString("roomdescription");
                int capacity = rs.getInt("roomcapacity");
                double price = rs.getDouble("roomcost");
                String status = rs.getString("roomstatus");
                String photo = rs.getString("roomphoto");

                Room room = new Room(roomNumber, description, capacity, price, status, photo);
                freeRooms.add(room);
            }
        }

        return freeRooms; // Возвращаем список свободных номеров
    }
}
