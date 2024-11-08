package com.example.course_work;
import com.example.course_work.models.Room;
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
            room.setRoomNumber(event.getNewValue());
        });

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setDescription(event.getNewValue());
        });

        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        capacityColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setCapacity(event.getNewValue());
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setPrice(event.getNewValue());
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statusColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setStatus(event.getNewValue());
        });

        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        photoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        photoColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setPhoto(event.getNewValue());
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

    // Метод для добавления новой комнаты с пустыми значениями
    private void addRoom() {
        // Создание новой пустой комнаты для ввода данных
        Room newRoom = new Room("", "", 0, 0.0, "", "");

        // Добавление новой комнаты в список
        rooms.add(newRoom);

        // Установка фокуса на новую строку для ввода данных
        int newIndex = rooms.size() - 1; // Индекс новой комнаты
        roomTableView.scrollTo(newIndex); // Прокрутка к новой строке
        roomTableView.getSelectionModel().select(newIndex); // Выбор новой строки

        // Включение режима редактирования для первой колонки новой строки
        roomTableView.edit(newIndex, roomNumberColumn);
    }
}