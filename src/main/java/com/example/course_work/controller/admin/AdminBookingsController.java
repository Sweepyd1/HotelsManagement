package com.example.course_work.controller.admin;

import com.example.course_work.database.DBCONN;
import com.example.course_work.database.RoomCrud;
import com.example.course_work.database.UserCrud;
import com.example.course_work.models.BookingInfo;
import com.example.course_work.models.Room;
import com.example.course_work.models.UserForAdmin;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AdminBookingsController {

    @FXML
    private TableView<String[]> tableView;


    @FXML
    private TableColumn<String[], String> userId;

    @FXML
    private TableColumn<String[], String> username;

    @FXML
    private TableColumn<String[], String> surname;

    @FXML
    private TableColumn<String[], String> checkindate;

    @FXML
    private TableColumn<String[], String> checkoutdate;

    @FXML
    private TableColumn<String[], String> bookingdate;

    @FXML
    private TableColumn<String[], String> countDay;

    @FXML
    private TableColumn<String[], String> total_price;



    @FXML
    public TableColumn<String[], String> titleRoom;

//    @FXML
//    public TableColumn<String[], String> status;

    private ObservableList<String[]> data;

    @FXML
    public void initialize() {
        data = FXCollections.observableArrayList();
        tableView.setItems(data);
        loadAlldata();

        userId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0])); // ID бронирования
        username.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1])); // Имя пользователя
        surname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));  // Фамилия пользователя
        checkindate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3])); // Дата заезда
        checkoutdate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4])); // Дата выезда
        bookingdate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));  // Дата бронирования
        countDay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[6]));   // Количество дней
        total_price.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[7]));
        titleRoom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[8]));// Итоговая цена

        // Добавляем слушателя для обработки кликов мыши на таблице
        tableView.setOnMouseClicked(this::handleRowSelection);
    }

    @FXML
    private void handleAddButtonAction() {

        try (Connection connection = DBCONN.getConnection()) {
            RoomCrud roomCrud = new RoomCrud(connection);

            String[] inputData = showInputDialog("Добавить комнату", "Введите данные комнаты:");
            if (inputData != null) {
                LocalDate currentDate = LocalDate.now();
                int userId = Integer.parseInt(inputData[0]);
                int roomId = Integer.parseInt(inputData[1]);
                LocalDate checkInDate = LocalDate.parse(inputData[2]);
                LocalDate checkOutDate = LocalDate.parse(inputData[3]);


                if (checkInDate == null || checkOutDate == null) {
                    showAlert("Ошибка", "Пожалуйста, выберите даты заезда и выезда.");
                    return;
                }

                // Проверка, что даты не меньше текущего дня
                if (checkInDate.isBefore(currentDate) || checkOutDate.isBefore(currentDate)) {
                    showAlert("Ошибка", "Выбранные даты не могут быть меньше текущего дня.");
                    return;
                }

                // Проверка, что дата заезда не больше даты выезда
                if (checkInDate.isAfter(checkOutDate)) {
                    showAlert("Ошибка", "Дата заезда не может быть позже даты выезда.");
                    return;
                }

                System.out.println(userId);

                roomCrud.bookRoom(userId, roomId, checkInDate, checkOutDate);
                data.clear();
                loadAlldata();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    @FXML
    private void handleEditButtonAction() throws SQLException {
        String[] selectedRoom = tableView.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            System.out.println(selectedRoom[5]);

            String[] updatedDetails = showInputDialog("Редактировать комнату", "Введите новые данные комнаты:", selectedRoom);
            if (updatedDetails != null) {

                try (Connection connection = DBCONN.getConnection()) {
                    RoomCrud roomCrud = new RoomCrud(connection);

                    int index2 = Integer.parseInt(updatedDetails[2]); // Convert index 2 to int
                    int index3 = Integer.parseInt(updatedDetails[3]); // Convert index 3 to int
                    int index5 = Integer.parseInt(updatedDetails[5]); // Convert index 5 to int
                    roomCrud.updateRoomData(updatedDetails[0], updatedDetails[1], index2, index3, updatedDetails[4], index5, selectedRoom[0]);
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                int index = data.indexOf(selectedRoom);
                data.set(index, updatedDetails);
            }
        } else {
            showAlert("Ошибка", "Пожалуйста, выберите комнату для редактирования.");
        }
    }

    @FXML
    private void handleDeleteButtonAction() {
        String[] selectedRoom = tableView.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            try (Connection connection = DBCONN.getConnection()) {
                RoomCrud roomCrud = new RoomCrud(connection);
                System.out.println(Integer.parseInt(selectedRoom[0]));
                System.out.println(LocalDate.parse(selectedRoom[3]));
                System.out.println(LocalDate.parse(selectedRoom[4]));
                roomCrud.deletedBooked(Integer.parseInt(selectedRoom[0]), LocalDate.parse(selectedRoom[3]),LocalDate.parse(selectedRoom[4]));
            }
            catch (SQLException e) {


                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null); // Убираем заголовок
                alert.setContentText("Завтра туда заедет клиент, удалять уже поздно");

                // Отображение окна
                alert.showAndWait();
            }
            data.clear();
            loadAlldata();
        } else {
            showAlert("Ошибка", "Пожалуйста, выберите комнату для удаления.");
        }
    }

    private void handleRowSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                System.out.println("Selected Room Data:");
                System.out.println("Номер комнаты: " + selectedRow[0]);
                System.out.println("Описание: " + selectedRow[1]);
                System.out.println("Вместимость: " + selectedRow[2]);
                System.out.println("Стоимость: " + selectedRow[3]);
                System.out.println("Фото: " + selectedRow[4]);
                System.out.println("сервис: " + selectedRow[5]);
            }
        }
    }


    private String[] showInputDialog(String title, String headerText) {
        return showInputDialog(title, headerText, null);
    }

    private String[] showInputDialog(String title, String headerText, String[] defaultValues) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        // Create input fields
        TextField userIdField = new TextField(defaultValues != null ? defaultValues[0] : ""); // ID пользователя
        TextField roomIdField = new TextField(defaultValues != null ? defaultValues[1] : ""); // ID комнаты
        DatePicker checkInDatePicker = new DatePicker(defaultValues != null ? LocalDate.parse(defaultValues[2]) : LocalDate.now()); // Дата заезда
        DatePicker checkOutDatePicker = new DatePicker(defaultValues != null ? LocalDate.parse(defaultValues[3]) : LocalDate.now().plusDays(1)); // Дата выезда

        GridPane grid = new GridPane();
        grid.add(new Label("ID пользователя:"), 0, 0);
        grid.add(userIdField, 1, 0);

        grid.add(new Label("ID комнаты:"), 0, 1);
        grid.add(roomIdField, 1, 1);

        grid.add(new Label("Дата заезда:"), 0, 2);
        grid.add(checkInDatePicker, 1, 2);

        grid.add(new Label("Дата выезда:"), 0, 3);
        grid.add(checkOutDatePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new String[]{
                        userIdField.getText(),           // ID пользователя
                        roomIdField.getText(),           // ID комнаты
                        checkInDatePicker.getValue() != null ? checkInDatePicker.getValue().toString() : "", // Дата заезда
                        checkOutDatePicker.getValue() != null ? checkOutDatePicker.getValue().toString() : "" // Дата выезда
                };
            }
            return null;
        });

        return dialog.showAndWait().orElse(null); // Show dialog and wait for result
    }

    public void loadAlldata() {
        try (Connection connection = DBCONN.getConnection()) {
            RoomCrud roomCrud = new RoomCrud(connection);
            List<BookingInfo> userData = roomCrud.getAllBookings(); // Получаем все бронирования
            for (BookingInfo booking : userData) {
                String[] roomDetails = new String[9]; // Размер массива соответствует количеству колонок
                roomDetails[0] = String.valueOf(booking.getUserID()); // ID бронирования
                roomDetails[1] = booking.getUserName();                 // Имя пользователя
                roomDetails[2] = booking.getUserSurname();              // Фамилия пользователя
                roomDetails[3] = booking.getCheckInDate().toString();   // Дата заезда
                roomDetails[4] = booking.getCheckOutDate().toString();  // Дата выезда
                roomDetails[5] = booking.getBookingDate().toString();    // Дата бронирования
                roomDetails[6] = String.valueOf(booking.getCountDay());  // Количество дней
                roomDetails[7] = String.valueOf(booking.getTotalPrice());
                roomDetails[8] = booking.getTitleRoom();// Итоговая цена

                data.add(roomDetails);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}