package com.example.course_work;

import com.example.course_work.database.DBCONN;
import com.example.course_work.database.Room;
//import com.example.course_work.models.BookedRoom;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class RoomCartController {

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView roomImageView;

    @FXML
    private Label daysCountLabel;

    private int daysCount = 1;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label countLabel;

    @FXML
    private Button addToCartButton;

    private String roomTitle;
    


    public void setRoomData( String title, String description, String count, String imagePath) {


        this.roomTitle = title;
        titleLabel.setText(title);
        descriptionLabel.setText(description);
        countLabel.setText(count);

        setRoomImage(imagePath);

        addToCartButton.setOnAction(event -> {
            try {
                handleAddToCartRoom();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }



    private void handleAddToCartRoom() throws SQLException {
        int userId = Session.getInstance().getId();
        System.out.println("Добавлено в корзину: " + roomTitle + ", пользователь ID: " + userId + ", количество дней: " + daysCount);

        if (userId <= 0) {
            System.out.println("Пользователь не авторизован.");
            return;
        }

        Connection connection = null; // Инициализируем переменную для соединения
        try {
            connection = DBCONN.getConnection(); // Получаем соединение
            Room room = new Room(connection);

            int roomId = room.getSelectedRoomIdByTitle(roomTitle);
            LocalDate checkInDate = Session.getInstance().getInDate();
            LocalDate checkOutDate = Session.getInstance().getOutDate();

            // Бронирование комнаты
            room.bookRoom(userId, roomId, checkInDate, checkOutDate);
            System.out.println("выполнено");
            addToCartButton.setText("добавлено в брони");
            addToCartButton.setStyle("-fx-background-color:#9ea5db; -fx-text-fill: white;");

        } catch (SQLException e) {
            e.printStackTrace(); // Обработка исключений
        } finally {
            if (connection != null) {
                try {
                    connection.close(); // Закрываем соединение
                } catch (SQLException e) {
                    e.printStackTrace(); // Обработка исключений при закрытии
                }
            }
        }
    }





    private void setRoomImage(String imagePath) {
        try {
            File file = new File("src/main/resources/images/" + imagePath);
            FileInputStream fis = new FileInputStream(file);
            Image image = new Image(fis);
            fis.close();
            roomImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка загрузки изображения: " + e.getMessage());
            roomImageView.setImage(null);
        }
    }


    public void getRoomInfo(MouseEvent mouseEvent) {
        System.out.println(this.roomTitle);
    }
}