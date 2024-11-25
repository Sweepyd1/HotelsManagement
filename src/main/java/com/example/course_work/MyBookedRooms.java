package com.example.course_work;
import com.example.course_work.database.DBCONN;
import com.example.course_work.models.BookedRoom;
import com.example.course_work.models.Room;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import com.example.course_work.database.RoomCrud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MyBookedRooms {

        @FXML
        private GridPane gridPane;

        @FXML
        public void initialize() {
            try (Connection connection = DBCONN.getConnection()) {

                RoomCrud roomCrud = new RoomCrud(connection);
                List<BookedRoom> freeRooms = roomCrud.getBookedRoomsForUser(SessionManager.getInstance().getId());

                if (freeRooms != null && !freeRooms.isEmpty()) {
                    for (int i = 0; i < freeRooms.size(); i++) {
                        BookedRoom room = freeRooms.get(i);
                        try {

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("bookedroom_management.fxml")); // Укажите правильный путь к вашему FXML
                            VBox roomVBox = loader.load();

                            MyBookedRoomController myBookedRooms = loader.getController();
                            String imagePath = room.getPhoto();
                            myBookedRooms.setRoomData(room.getRoomNumber(), room.getDescription(), "Количество: " + room.getCapacity(), imagePath, room.getIndate(), room.getOutdate(), room.getBookingdate()); // Используем данные из базы

                            int column = i % 3;
                            int row = i / 3;

                            GridPane.setMargin(roomVBox, new javafx.geometry.Insets(40));
                            gridPane.add(roomVBox, column, row);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            if (connection != null) {
                                try {
                                    connection.close(); // Закрываем соединение
                                } catch (SQLException e) {
                                    e.printStackTrace(); // Обработка исключений при закрытии
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Нет свободных комнат.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }

