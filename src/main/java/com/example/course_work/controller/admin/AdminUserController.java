package com.example.course_work.controller.admin;


import com.example.course_work.Session;
import com.example.course_work.database.DBCONN;
import com.example.course_work.database.User;
import com.example.course_work.models.UserForAdmin;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;

import java.sql.SQLException;

import java.util.List;


public class AdminUserController {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> user_id;

    @FXML
    private TableColumn<String[], String> username;

    @FXML
    private TableColumn<String[], String> usersurname;

    @FXML
    private TableColumn<String[], String> userlogin;

    @FXML
    private TableColumn<String[], String> userpassword;
    @FXML
    public TableColumn<String[], String> user_role;





    private ObservableList<String[]> data;

    @FXML
    public void initialize() {



        data = FXCollections.observableArrayList();
        tableView.setItems(data);
        loadAlldata();

        user_id.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        username.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        usersurname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        userlogin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        userpassword.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        user_role.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));

        // Add a listener for mouse clicks on the table view
        tableView.setOnMouseClicked(this::handleRowSelection);
    }

    @FXML
    private void handleAddButtonAction() {

        try (Connection connection = DBCONN.getConnection()) {
            User user = new User(connection);

            String[] userDetails = showInputDialog("Добавить комнату", "Введите данные комнаты:");
            if (userDetails != null) {
                System.out.println(userDetails[0]);

                user.createUser(userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4]);
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
            System.out.println(selectedRoom[0]);

            String[] updatedDetails = showInputDialog("Редактировать комнату", "Введите новые данные комнаты:", selectedRoom);
            if (updatedDetails != null) {

                try (Connection connection = DBCONN.getConnection()) {
                    User user = new User(connection);


                    user.updateUser(Integer.parseInt(selectedRoom[0]),updatedDetails[0], updatedDetails[1], updatedDetails[2], updatedDetails[3],updatedDetails[4]);
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                data.clear();
                loadAlldata();
            }
        } else {
            showAlert("Ошибка", "Пожалуйста, выберите комнату для редактирования.");
        }
    }

    @FXML
    private void handleDeleteButtonAction() {
        String[] selectedRoom = tableView.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            if(Session.getInstance().getId() == Integer.parseInt(selectedRoom[0])){
                showAlert("ошибка","удалять себя нельзя");
                return;
            }
            try (Connection connection = DBCONN.getConnection()) {
                User roomCrud = new User(connection);
                roomCrud.deleteUser(Integer.parseInt(selectedRoom[0]));
            }
            catch (SQLException e) {
                showAlert("Ошибка", "Пожалуйста, выберите комнату для удаления.");
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null); // Убираем заголовок
                alert.setContentText("Завтра туда заедет клиент, удалять уже поздно"); // Текст сообщения

                // Отображение окна
                alert.showAndWait();
            }
            data.remove(selectedRoom);
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
//        TextField user_id = new TextField(defaultValues != null ? defaultValues[0] : "");
        TextArea descriptionField = new TextArea(defaultValues != null ? defaultValues[1] : "");
        descriptionField.setWrapText(true);

        TextField capacityField = new TextField(defaultValues != null ? defaultValues[2] : "");
        TextField costField = new TextField(defaultValues != null ? defaultValues[3] : "");
        TextField photoField = new TextField(defaultValues != null ? defaultValues[4] : "");
        TextField serviceField = new TextField(defaultValues != null ? defaultValues[5] : "");

        GridPane grid = new GridPane();

        grid.add(new Label("Роль пользователя"), 0, 0);
        grid.add(serviceField, 1, 0);

        grid.add(new Label("имя:"), 0, 1);
        grid.add(descriptionField, 1, 1);

        grid.add(new Label("пароль:"), 0, 2);
        grid.add(capacityField, 1, 2);

        grid.add(new Label("логин"), 0, 3);
        grid.add(costField, 1, 3);

        grid.add(new Label("фамилия"), 0, 4);
        grid.add(photoField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new String[]{
//                        roomNumberField.getText(),
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
            User userCrud = new User(connection);
            List<UserForAdmin> userData = userCrud.getAllUserData(); // Fetch all rooms
            for (UserForAdmin user : userData) {
                String[] roomDetails = new String[6]; // Adjust size based on your columns
                roomDetails[0] = String.valueOf(user.getId());
                roomDetails[1] = user.getUsername();
                roomDetails[2] = user.getSurname();
                roomDetails[3] = user.getLogin();
                roomDetails[4] = user.getPassword();
                roomDetails[5] = user.getRole();
                data.add(roomDetails); // Add the String array to the ObservableList
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

