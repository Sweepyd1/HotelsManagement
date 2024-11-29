package com.example.course_work;

import com.example.course_work.database.DBCONN;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import com.example.course_work.database.Room;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.Label;

public class AllRooms {
    @FXML
    private GridPane gridPane;
    @FXML
    public Button openFilter;

    @FXML
    public void initialize() {
        try (Connection connection = DBCONN.getConnection()) {
            Room roomCrud = new Room(connection);
            List<com.example.course_work.models.Room> freeRooms = roomCrud.getFilteredRooms();

            // Проверяем, есть ли свободные комнаты
            if (freeRooms != null && !freeRooms.isEmpty()) {
                for (int i = 0; i < freeRooms.size(); i++) {
                    com.example.course_work.models.Room room = freeRooms.get(i);
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("hotel.fxml")); // Укажите правильный путь к вашему FXML
                        VBox roomVBox = loader.load();

                        // Получение контроллера для комнаты
                        RoomCartController roomController = loader.getController();
                        String imagePath = room.getPhoto();

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
                showNoRoomsMessage();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void showNoRoomsMessage() { // Метод для отображения сообщения о том, что нет свободных комнат
        Label noRoomsLabel = new Label("Ничего не найдено"); // Создаем метку с текстом "Ничего не найдено"
        noRoomsLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;"); // Устанавливаем стиль для метки

        GridPane.setConstraints(noRoomsLabel, 0, 0); // Устанавливаем позицию метки в GridPane
        GridPane.setColumnSpan(noRoomsLabel, 3); // Разрешаем метке занимать несколько колонок (все три)
        GridPane.setMargin(noRoomsLabel, new javafx.geometry.Insets(10)); // Устанавливаем отступы вокруг метки

        gridPane.getChildren().clear(); // Очищаем все элементы из gridPane перед добавлением новой метки
        gridPane.add(noRoomsLabel, 0, 0); // Добавляем метку в GridPane на позицию (0,0)
    }

}
