package com.example.course_work;

import com.example.course_work.database.RoomCrud;
//import com.example.course_work.models.BookedRoom;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
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
        int userId = SessionManager.getInstance().getId();
        System.out.println("Добавлено в корзину: " + roomTitle + ", пользователь ID: " + userId + ", количество дней: " + daysCount);

        if (userId <= 0) {
            System.out.println("Пользователь не авторизован.");
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hotel", "postgres", "sweepy2006")) {
            RoomCrud roomCrud = new RoomCrud(connection);

        int roomId = roomCrud.getSelectedRoomIdByTitle(roomTitle);
        LocalDate checkInDate = SessionManager.getInstance().getInDate();
        LocalDate checkOutDate = SessionManager.getInstance().getOutDate();
//        BookedRoom bookedRoom = new BookedRoom(checkInDate, checkOutDate);

        try {
            roomCrud.bookRoom(userId, roomId, checkInDate, checkOutDate);
            System.out.println("выполнено");
            addToCartButton.setText("добавлено в брони");
            addToCartButton.setStyle("-fx-background-color:#9ea5db; -fx-text-fill: white;");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    }



    private void setRoomImage(String imagePath) {

        try {
            Image image = new Image(getClass().getResourceAsStream("/images/" + imagePath));
            roomImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка загрузки изображения: " + e.getMessage());
        }
    }


    public void getRoomInfo(MouseEvent mouseEvent) {
        System.out.println(this.roomTitle);
    }
}