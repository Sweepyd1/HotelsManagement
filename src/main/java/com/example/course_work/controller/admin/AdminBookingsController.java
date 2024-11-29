package com.example.course_work.controller.admin;

import com.example.course_work.database.DBCONN;
import com.example.course_work.database.Room;
import com.example.course_work.models.BookingInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private void handleAddButtonAction() throws SQLException {




            String[] inputData = showInputDialog("Создание бронирования", "Введите данные бронирования", null);
            if (inputData != null) {
                String userId = inputData[0];
                String roomIdsString = inputData[1]; // Комнаты как строка
                String additionalUserNamesString = inputData[2]; // Дополнительные пользователи как строка
                String checkInDate = inputData[3];
                String checkOutDate = inputData[4];

                try (Connection connection = DBCONN.getConnection()) {
                    // 1. Вставка записи в таблицу Bookings
                    String insertBookingQuery = "INSERT INTO Bookings (UserID, CheckInDate, CheckOutDate, peoples) VALUES (?, ?, ?, ?) RETURNING BookingID";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookingQuery)) {
                        preparedStatement.setInt(1, Integer.parseInt(userId));
                        preparedStatement.setDate(2, Date.valueOf(checkInDate));
                        preparedStatement.setDate(3, Date.valueOf(checkOutDate));
                        preparedStatement.setArray(4, connection.createArrayOf("text", additionalUserNamesString.split(","))); // Преобразуем строку в массив текстов

                        // Выполняем вставку и получаем BookingID
                        int bookingId;
                        try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                            if (generatedKeys.next()) {
                                bookingId = generatedKeys.getInt(1); // Получаем сгенерированный BookingID
                            } else {
                                throw new SQLException("Не удалось получить ID нового бронирования.");
                            }
                        }

                        System.out.println("Бронирование успешно добавлено с ID: " + bookingId);


                        // 2. Вставка записей в таблицу BookingRooms
                        String[] roomIds = roomIdsString.split(","); // Получаем массив ID комнат
                        String insertBookingRoomQuery = "INSERT INTO BookingRooms (BookingID, RoomID) VALUES (?, ?)";

                        try (PreparedStatement roomPreparedStatement = connection.prepareStatement(insertBookingRoomQuery)) {
                            for (String roomId : roomIds) {
                                roomPreparedStatement.setInt(1, bookingId); // Устанавливаем BookingID
                                roomPreparedStatement.setInt(2, Integer.parseInt(roomId.trim())); // Устанавливаем RoomID
                                roomPreparedStatement.executeUpdate(); // Выполняем вставку
                            }
                        }
                        data.clear();
                        loadAlldata();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Ошибка при добавлении бронирования: " + e.getMessage());
                    }
                }
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
                    Room room = new Room(connection);

                    int index2 = Integer.parseInt(updatedDetails[2]); // Convert index 2 to int
                    int index3 = Integer.parseInt(updatedDetails[3]); // Convert index 3 to int
                    int index5 = Integer.parseInt(updatedDetails[5]); // Convert index 5 to int
                    room.updateRoomData(updatedDetails[0], updatedDetails[1], index2, index3, updatedDetails[4], index5, selectedRoom[0]);
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null); // Убираем заголовок
                    alert.setContentText("Завтра туда заедет клиент, удалять уже поздно");
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
                Room room = new Room(connection);
                room.deletedBooked(Integer.parseInt(selectedRoom[0]), LocalDate.parse(selectedRoom[3]),LocalDate.parse(selectedRoom[4]));
            }
            catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null); // Убираем заголовок
                alert.setContentText("Завтра туда заедет клиент, удалять уже поздно");


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
        TextField userIdField = new TextField(defaultValues != null ? defaultValues[0] : ""); // ID главного пользователя
        TextField roomIdField = new TextField(defaultValues != null ? defaultValues[1] : ""); // ID комнаты
        DatePicker checkInDatePicker = new DatePicker(defaultValues != null ? LocalDate.parse(defaultValues[2]) : LocalDate.now()); // Дата заезда
        DatePicker checkOutDatePicker = new DatePicker(defaultValues != null ? LocalDate.parse(defaultValues[3]) : LocalDate.now().plusDays(1)); // Дата выезда

        // Для хранения текстовых полей для комнат
        VBox roomFieldsContainer = new VBox();
        roomFieldsContainer.getChildren().add(roomIdField); // Добавляем первое поле для ID комнаты

        // Для хранения текстовых полей для дополнительных пользователей
        VBox userFieldsContainer = new VBox();

        Button addRoomButton = new Button("Добавить комнату");
        addRoomButton.setOnAction(e -> {
            TextField newRoomIdField = new TextField(); // Создаем новое поле для ID комнаты
            roomFieldsContainer.getChildren().add(newRoomIdField); // Добавляем его в контейнер
        });

        Button addUserButton = new Button("Добавить пользователя");
        addUserButton.setOnAction(e -> {
            TextField newUserNameField = new TextField(); // Создаем новое поле для ФИО дополнительного пользователя
            userFieldsContainer.getChildren().add(newUserNameField); // Добавляем его в контейнер
        });

        GridPane grid = new GridPane();
        grid.add(new Label("ID главного пользователя:"), 0, 0);
        grid.add(userIdField, 1, 0);

        grid.add(new Label("Комнаты:"), 0, 1);
        grid.add(roomFieldsContainer, 1, 1);
        grid.add(addRoomButton, 1, 2); // Кнопка для добавления комнаты

        grid.add(new Label("Дополнительные пользователи:"), 0, 3);
        grid.add(userFieldsContainer, 1, 3);
        grid.add(addUserButton, 1, 4); // Кнопка для добавления пользователя

        grid.add(new Label("Дата заезда:"), 0, 5);
        grid.add(checkInDatePicker, 1, 5);

        grid.add(new Label("Дата выезда:"), 0, 6);
        grid.add(checkOutDatePicker, 1, 6);

        // Создаем ScrollPane и добавляем в него GridPane
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true); // Устанавливаем ширину ScrollPane равной ширине содержимого

        dialog.getDialogPane().setContent(scrollPane); // Устанавливаем ScrollPane как содержимое диалога

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                // Собираем данные из полей
                List<String> roomIds = new ArrayList<>();
                for (Node node : roomFieldsContainer.getChildren()) {
                    if (node instanceof TextField) {
                        roomIds.add(((TextField) node).getText());
                    }
                }

                List<String> additionalUserNames = new ArrayList<>();
                for (Node node : userFieldsContainer.getChildren()) {
                    if (node instanceof TextField) {
                        additionalUserNames.add(((TextField) node).getText());
                    }
                }

                return new String[]{
                        userIdField.getText(),                  // ID главного пользователя
                        String.join(",", roomIds),              // Комнаты как строка
                        String.join(",", additionalUserNames),   // Дополнительные пользователи как строка
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
            Room room = new Room(connection);
            List<BookingInfo> userData = room.getAllBookings(); // Получаем все бронирования
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