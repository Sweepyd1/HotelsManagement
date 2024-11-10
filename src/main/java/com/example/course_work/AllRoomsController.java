package com.example.course_work;

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

public class AllRoomsController {
    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hotel", "postgres", "sweepy2006")) {
            RoomCrud roomCrud = new RoomCrud(connection);
            List<Room> freeRooms = roomCrud.getFilteredRooms();

            // Проверяем, есть ли свободные комнаты
            if (freeRooms != null && !freeRooms.isEmpty()) {
                for (int i = 0; i < freeRooms.size(); i++) {
                    Room room = freeRooms.get(i);
                    try {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("hotel.fxml")); // Укажите правильный путь к вашему FXML
                        VBox roomVBox = loader.load();

                        // Получение контроллера для комнаты
                        RoomCartController roomController = loader.getController();
                        String imagePath = "hotel (1).png";
                        roomController.setRoomData(room.getRoomNumber(), room.getDescription(), "Количество: " + room.getCapacity(), imagePath); // Используем данные из базы

                        // Определяем позицию в GridPane
                        int column = i % 3; // Определяем колонку (0, 1 или 2)
                        int row = i / 3; // Определяем строку

                        // Установка отступов между карточками
                        GridPane.setMargin(roomVBox, new javafx.geometry.Insets(40)); // Устанавливаем отступы вокруг VBox

                        // Добавление VBox в GridPane
                        gridPane.add(roomVBox, column, row); // Добавляем по колонкам и строкам
                    } catch (IOException e) {
                        e.printStackTrace();
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
