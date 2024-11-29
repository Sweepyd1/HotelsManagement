package com.example.course_work.controller.admin;

import com.example.course_work.database.DBCONN;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.example.course_work.database.Room;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminRoomsController {


    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> roomNumberColumn;

    @FXML
    private TableColumn<String[], String> descriptionColumn;

    @FXML
    private TableColumn<String[], String> capacityColumn;

    @FXML
    private TableColumn<String[], String> costColumn;

    @FXML
    private TableColumn<String[], String> photoColumn;
    @FXML
    public TableColumn<String[], String> serviceColumn;

    private ObservableList<String[]> data;

    @FXML
    public void initialize() {

        data = FXCollections.observableArrayList();
        tableView.setItems(data);
        loadAlldata();

        roomNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        capacityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        costColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        photoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        serviceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));

        tableView.setOnMouseClicked(this::handleRowSelection);
    }

    @FXML
    private void handleAddButtonAction() {

        try (Connection connection = DBCONN.getConnection()) {
            Room room = new Room(connection);

            String[] roomDetails = showInputDialog("Добавить комнату", "Введите данные комнаты:");
            if (roomDetails != null) {

                int index2 = Integer.parseInt(roomDetails[2]); // Convert index 2 to int
                int index3 = Integer.parseInt(roomDetails[3]); // Convert index 3 to int
                int index5 = Integer.parseInt(roomDetails[5]); // Convert index 5 to int

                room.addNewRoom(roomDetails[0], roomDetails[1], index2, index3, roomDetails[4], index5);

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

            // Проверяем, забронирована ли комната
            try (Connection connection = DBCONN.getConnection()) {
                Room room = new Room(connection);
                boolean isBooked = room.isRoomBooked(selectedRoom[0]); // Проверяем по ID комнаты

                if (isBooked) {
                    showAlert("Ошибка", "Нельзя редактировать комнату, так как она уже забронирована."); // Уведомляем пользователя
                    return; // Выходим из метода, если комната забронирована
                }
            }

            String[] updatedDetails = showInputDialog("Редактировать комнату", "Введите новые данные комнаты:", selectedRoom);
            if (updatedDetails != null) {
                try (Connection connection = DBCONN.getConnection()) {
                    Room room = new Room(connection);

                    int index2 = Integer.parseInt(updatedDetails[2]); // Convert index 2 to int
                    int index3 = Integer.parseInt(updatedDetails[3]); // Convert index 3 to int
                    int index5 = Integer.parseInt(updatedDetails[5]); // Convert index 5 to int
                    room.updateRoomData(updatedDetails[0], updatedDetails[1], index2, index3, updatedDetails[4], index5, selectedRoom[0]);
                } catch (SQLException e) {
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
        String[] selectedRoom = tableView.getSelectionModel().getSelectedItem(); // Получаем выбранную комнату
        if (selectedRoom != null) {
            try (Connection connection = DBCONN.getConnection()) {
                Room room = new Room(connection);
                room.deleteRoom(selectedRoom[0]); // Удаляем комнату по ID
                data.remove(selectedRoom); // Удаляем комнату из списка данных
            } catch (SQLException e) {
                // Если возникает ошибка при удалении, показываем предупреждение
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null); // Убираем заголовок
                alert.setContentText("Завтра туда заедет клиент, удалять уже поздно"); // Текст сообщения

                // Отображение окна
                alert.showAndWait();
                System.out.println(e.getMessage()); // Выводим сообщение об ошибке в консоль
            }
        } else {
            // Если комната не выбрана, показываем предупреждение
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null); // Убираем заголовок
            alert.setContentText("Пожалуйста, выберите комнату для удаления."); // Текст сообщения

            // Отображение окна
            alert.showAndWait();
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
        TextField roomNumberField = new TextField(defaultValues != null ? defaultValues[0] : "");
        TextArea descriptionField = new TextArea(defaultValues != null ? defaultValues[1] : "");
        descriptionField.setWrapText(true);

        TextField capacityField = new TextField(defaultValues != null ? defaultValues[2] : "");
        TextField costField = new TextField(defaultValues != null ? defaultValues[3] : "");
        TextField photoField = new TextField(defaultValues != null ? defaultValues[4] : "");
        TextField serviceField = new TextField(defaultValues != null ? defaultValues[5] : "");

        // Create a button to open the FileChooser
        Button fileButton = new Button("Выбрать фото");
        FileChooser fileChooser = new FileChooser();

        // Set up the button action
        fileButton.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                String fileName = selectedFile.getName(); // Получаем только имя файла
                photoField.setText(fileName); // Устанавливаем имя файла в текстовое поле

                // Сохраняем файл в папку resources/images
                try {
                    Path sourcePath = selectedFile.toPath();
                    Path destinationPath = Paths.get("src/main/resources/images/" + fileName); // Путь к папке ресурсов
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING); // Копируем файл
                } catch (IOException e) {
                    e.printStackTrace(); // Обработка ошибок при копировании файла
                }
            }
        });

        // Create a grid for layout
        GridPane grid = new GridPane();

        grid.add(new Label("Номер комнаты:"), 0, 0);
        grid.add(roomNumberField, 1, 0);

        grid.add(new Label("Описание:"), 0, 1);
        grid.add(descriptionField, 1, 1);

        grid.add(new Label("Вместимость:"), 0, 2);
        grid.add(capacityField, 1, 2);

        grid.add(new Label("Стоимость:"), 0, 3);
        grid.add(costField, 1, 3);

        grid.add(new Label("Фото (имя файла):"), 0, 4);
        grid.add(photoField, 1, 4);

        // Add the button to the grid instead of the FileChooser
        grid.add(fileButton, 1, 5);

        grid.add(new Label("Сервис комнаты"), 0, 6);
        grid.add(serviceField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new String[]{
                        roomNumberField.getText(),
                        descriptionField.getText(),
                        capacityField.getText(),
                        costField.getText(),
                        photoField.getText(),
                        serviceField.getText()
                };
            }
            return null;
        });

        return dialog.showAndWait().orElse(null); // Show dialog and wait for result
    }


    public void loadAlldata() {
        try (Connection connection = DBCONN.getConnection()) {
            Room roomCrud = new Room(connection);

            List<com.example.course_work.models.Room> roomsData = roomCrud.getAllRoomData(); // Fetch all rooms

            for (com.example.course_work.models.Room room : roomsData) {
                String[] roomDetails = new String[6]; // Adjust size based on your columns
                roomDetails[0] = room.getRoomNumber();
                roomDetails[1] = room.getDescription();
                roomDetails[2] = String.valueOf(room.getCapacity());
                roomDetails[3] = String.valueOf(room.getPrice());
                roomDetails[4] = room.getPhoto();
                roomDetails[5] = String.valueOf(room.getServiceId());

                data.add(roomDetails); // Add the String array to the ObservableList
            }




        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }








    // Method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}