package com.example.course_work;
import com.example.course_work.database.RoomCrud;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class MyBookedRoomController {





        @FXML
        private Label titleLabel;

        @FXML
        private Label indate;


        @FXML
        private Label outdate;


        @FXML
        private Label bookingdateLabel;



        @FXML
        private Label daysCountLabel;

        @FXML
        private ImageView roomImageView;

        private int daysCount = 1;

        @FXML
        private Label descriptionLabel;

        @FXML
        private Label countLabel;

        @FXML
        private Button addToCartButton;

        private String roomTitle;

        private LocalDate roomin;
        private LocalDate roomout;
        private LocalDate roombookingdate;



        public void setRoomData( String title, String description, String count, String imagePath, LocalDate in, LocalDate out, LocalDate bookingdate) {


            this.roomTitle = title;
            this.roomin = in;
            this.roomout = out;
            this.roombookingdate = bookingdate;
            titleLabel.setText(title);
            descriptionLabel.setText(description);
            countLabel.setText(count);
            indate.setText(String.valueOf(in));

            outdate.setText(String.valueOf(out));
            bookingdateLabel.setText(String.valueOf(bookingdate));

            setRoomImage(imagePath);

            addToCartButton.setOnAction(event -> {
                try {
                    deleteBooked();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        private void deleteBooked() throws SQLException {
            int userId = SessionManager.getInstance().getId();
            System.out.println("Добавлено в корзину: " + roomTitle + ", пользователь ID: " + userId + ", количество дней: " + daysCount + "in " +roomin + "out: " + roomout);


            if (userId <= 0) {
                System.out.println("Пользователь не авторизован.");
                return;
            }
            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hotel", "postgres", "sweepy2006")) {
                RoomCrud roomCrud = new RoomCrud(connection);
                roomCrud.deletedBooked(userId, roomin, roomout);
                addToCartButton.setText("удалено");
                addToCartButton.setStyle("-fx-background-color:#d992a9; -fx-text-fill: white;");

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
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

}







