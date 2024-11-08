package com.example.course_work;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RoomCartController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label countLabel;

    @FXML
    private Button addToCartButton;

    private String roomTitle;


    public void setRoomData(String title, String description, String count) {
        this.roomTitle = title;
        titleLabel.setText(title);
        descriptionLabel.setText(description);
        countLabel.setText(count);

        addToCartButton.setOnAction(event -> handleAddToCart());
    }



    private void handleAddToCart() {
        // Здесь вы можете реализовать логику добавления комнаты в корзину
        System.out.println("Добавлено в корзину: " + roomTitle);

    }

}