package com.example.course_work.controller.admin;
import com.example.course_work.models.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;


import java.net.URL;
import java.util.ResourceBundle;

public class AdminRoomsController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roomTableView.setEditable(true);

        setupColumn(roomNumberColumn, "roomNumber");
        setupColumn(descriptionColumn, "description");
        setupIntegerColumn(capacityColumn, "capacity");
        setupDoubleColumn(priceColumn, "price");
        setupColumn(statusColumn, "status");
        setupColumn(photoColumn, "photo");

        roomTableView.refresh();
        roomTableView.setItems(rooms);

        addButton.setOnAction(event -> addRoom());
    }

    private void setupColumn(TableColumn<Room, String> column, String propertyName) {
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            switch (propertyName) {
                case "roomNumber":
                    room.setRoomNumber(event.getNewValue());
                    break;
                case "description":
                    room.setDescription(event.getNewValue());
                    break;

                case "photo":
                    room.setPhoto(event.getNewValue());
                    break;
            }
            System.out.println("Updated " + propertyName + ": " + event.getNewValue());
        });
    }

    private void setupIntegerColumn(TableColumn<Room, Integer> column, String propertyName) {
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        column.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setCapacity(event.getNewValue());
            System.out.println("Updated " + propertyName + ": " + event.getNewValue());
        });
    }

    private void setupDoubleColumn(TableColumn<Room, Double> column, String propertyName) {
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        column.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setPrice(event.getNewValue());
            System.out.println("Updated " + propertyName + ": " + event.getNewValue());
        });
    }

    private void addRoom() {
        Room newRoom = new Room("New Room", "Description", 10, 100.0,"photo.png");

        rooms.add(newRoom);

        // Установка фокуса на новую строку для ввода данных
        int newIndex = rooms.size() - 1; // Индекс новой комнаты
        roomTableView.scrollTo(newIndex); // Прокрутка к новой строке
        roomTableView.getSelectionModel().select(newIndex); // Выбор новой строки

        roomTableView.edit(newIndex, roomNumberColumn); // Редактирование номера комнаты
    }
}