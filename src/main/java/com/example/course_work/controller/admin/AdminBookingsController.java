package com.example.course_work.controller.admin;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    private TableColumn<String[], String> comment;

    @FXML
    public TableColumn<String[], String> titleRoom;

//    @FXML
//    public TableColumn<String[], String> status;

    private ObservableList<String[]> data;

    @FXML
    public void initialize() {


    }



    public void handleDeleteButtonAction(ActionEvent actionEvent) {
    }

    public void handleEditButtonAction(ActionEvent actionEvent) {
    }

    public void handleAddButtonAction(ActionEvent actionEvent) {
    }
}
