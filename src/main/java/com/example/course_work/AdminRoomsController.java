package com.example.course_work;
import com.example.course_work.models.Room;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class AdminRoomsController {

    @FXML
    private TableView<Room> roomTableView;

    @FXML
    private TableColumn<Room, String> roomNumberColumn;

    @FXML
    private TableColumn<Room, String> descriptionColumn;

    @FXML
    private TableColumn<Room, Integer> capacityColumn;

    @FXML
    private TableColumn<Room, Double> priceColumn;

    @FXML
    private TableColumn<Room, String> statusColumn;

    @FXML
    private TableColumn<Room, String> photoColumn;

    @FXML
    private Button addButton;

    // Список комнат
    private ObservableList<Room> rooms = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Настройка колонок таблицы для редактирования с использованием PropertyValueFactory
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        roomNumberColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setRoomNumber(event.getNewValue()); // Обновление значения в модели
        });

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setDescription(event.getNewValue()); // Обновление значения в модели
        });

        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        capacityColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setCapacity(event.getNewValue()); // Обновление значения в модели
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setPrice(event.getNewValue()); // Обновление значения в модели
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statusColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setStatus(event.getNewValue()); // Обновление значения в модели
        });

        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        photoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        photoColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setPhoto(event.getNewValue()); // Обновление значения в модели
        });

        // Установка данных в таблицу (пример)
        rooms.add(new Room("101", "Комната с видом на море", 2, 1500.0, "Свободна", "photo1.jpg"));

        // Установка списка в TableView
        roomTableView.setItems(rooms);

        // Разрешение редактирования таблицы
        roomTableView.setEditable(true);

        // Обработчик нажатия на кнопку "Добавить"
        addButton.setOnAction(event -> addRoom());
    }

    // Метод для добавления новой комнаты
    private void addRoom() {
        // Пример добавления новой записи с фиксированными данными
        Room newRoom = new Room("102", "Комната с балконом", 3, 2000.0, "Свободна", "photo2.jpg");
        rooms.add(newRoom);

        // Если нужно обновить таблицу (обычно это не требуется с ObservableList)
        roomTableView.refresh();
    }
}