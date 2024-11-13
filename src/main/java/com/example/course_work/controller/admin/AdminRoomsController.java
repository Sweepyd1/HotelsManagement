package com.example.course_work.controller.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;




import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

    private ObservableList<String[]> data;

    @FXML
    public void initialize() {
        data = FXCollections.observableArrayList();
        tableView.setItems(data);

        roomNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        capacityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        costColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        photoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));

        // Add a listener for mouse clicks on the table view
        tableView.setOnMouseClicked(this::handleRowSelection);
    }

    @FXML
    private void handleAddButtonAction() {
        String[] roomDetails = showInputDialog("Добавить комнату", "Введите данные комнаты:");
        if (roomDetails != null) {
            data.add(roomDetails);
        }
    }

    @FXML
    private void handleEditButtonAction() {
        String[] selectedRoom = tableView.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            String[] updatedDetails = showInputDialog("Редактировать комнату", "Введите новые данные комнаты:", selectedRoom);
            if (updatedDetails != null) {
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
            data.remove(selectedRoom);
        } else {
            showAlert("Ошибка", "Пожалуйста, выберите комнату для удаления.");
        }
    }

    // Method to handle row selection and print data to console
    private void handleRowSelection(MouseEvent event) {
        if (event.getClickCount() == 2) { // Check for double-click
            String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                System.out.println("Selected Room Data:");
                System.out.println("Номер комнаты: " + selectedRow[0]);
                System.out.println("Описание: " + selectedRow[1]);
                System.out.println("Вместимость: " + selectedRow[2]);
                System.out.println("Стоимость: " + selectedRow[3]);
                System.out.println("Фото: " + selectedRow[4]);
            }
        }
    }

    // Method to show an input dialog for room details
    private String[] showInputDialog(String title, String headerText) {
        return showInputDialog(title, headerText, null);
    }

    private String[] showInputDialog(String title, String headerText, String[] defaultValues) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        // Create input fields
        TextField roomNumberField = new TextField(defaultValues != null ? defaultValues[0] : "");
        TextField descriptionField = new TextField(defaultValues != null ? defaultValues[1] : "");
        TextField capacityField = new TextField(defaultValues != null ? defaultValues[2] : "");
        TextField costField = new TextField(defaultValues != null ? defaultValues[3] : "");
        TextField photoField = new TextField(defaultValues != null ? defaultValues[4] : "");

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
        grid.add(new Label("Фото (URL):"), 0, 4);
        grid.add(photoField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Add buttons for confirmation and cancellation
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Convert the result to an array of strings when the OK button is pressed
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new String[]{
                        roomNumberField.getText(),
                        descriptionField.getText(),
                        capacityField.getText(),
                        costField.getText(),
                        photoField.getText()
                };
            }
            return null;
        });

        return dialog.showAndWait().orElse(null); // Show dialog and wait for result
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