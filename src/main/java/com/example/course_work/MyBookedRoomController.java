package com.example.course_work;
import com.example.course_work.database.DBCONN;
import com.example.course_work.database.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
        private int roombookingdate;



        public void setRoomData( String title, String description, String count, String imagePath, LocalDate in, LocalDate out, int bookingdate) {

            this.roomTitle = title;
            this.roomin = in;
            this.roomout = out;
            long numberOfDays = ChronoUnit.DAYS.between(in, out); // Получаем количество дней между датами

            // Вычисляем roombookingdate как произведение количества дней на bookingdate
            this.roombookingdate = (int) (numberOfDays * bookingdate);

            titleLabel.setText(title);
            descriptionLabel.setText(description);
            countLabel.setText(count);
            indate.setText(String.valueOf(in));

            outdate.setText(String.valueOf(out));
            bookingdateLabel.setText(String.valueOf(this.roombookingdate));

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
            int userId = Session.getInstance().getId();
            System.out.println("Добавлено в корзину: " + roomTitle + ", пользователь ID: " + userId + ", количество дней: " + daysCount + "in " +roomin + "out: " + roomout);


            if (userId <= 0) {
                System.out.println("Пользователь не авторизован.");
                return;
            }
            try (Connection connection = DBCONN.getConnection()) {
                Room room = new Room(connection);
                room.deletedBooked(userId, roomin, roomout);
                addToCartButton.setText("удалено");
                addToCartButton.setStyle("-fx-background-color:#d992a9; -fx-text-fill: white;");

                showCancellationDialog(userId, roomTitle);

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null); // Убираем заголовок
                    alert.setContentText("Извините, нельзя отказаться от бронирования меньше чем за сутки до заезда"); // Текст сообщения
                    alert.showAndWait();


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


    private void showCancellationDialog(int userId, String roomTitle) throws SQLException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Причина отмены");
        dialog.setHeaderText("Пожалуйста, укажите причину отмены бронирования:");
        dialog.setContentText("Причина:");

        Optional<String> result;

        do {
            result = dialog.showAndWait();

            System.out.println(Session.getInstance().getId());
            System.out.println(roomTitle);


            // Проверяем, введена ли причина
            if (result.isPresent() && result.get().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Причина отмены не может быть пустой. Пожалуйста, укажите причину.");
                alert.showAndWait();
            }

        } while (!result.isPresent() || result.get().trim().isEmpty());
        String cancellationReason = result.get();
        // Здесь можно добавить логику для обработки причины отмены
        try (Connection connection = DBCONN.getConnection()) {
            Room room = new Room(connection);

            // Получаем идентификатор комнаты по названию
            int roomId = room.getSelectedRoomIdByTitle(roomTitle);

            // Вставляем новый отказ в таблицу reject
            String insertRejectionQuery = "INSERT INTO reject (UserID, description, roomId) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertRejectionQuery)) {
                preparedStatement.setInt(1, userId); // Устанавливаем UserID
                preparedStatement.setString(2, cancellationReason); // Устанавливаем описание
                preparedStatement.setInt(3, roomId); // Устанавливаем roomId

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Отказ успешно добавлен в таблицу reject.");
                } else {
                    System.out.println("Не удалось добавить отказ в таблицу reject.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении отказа: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Произошла ошибка при добавлении отказа. Пожалуйста, попробуйте еще раз.");
            alert.showAndWait();
        }
    }

}








