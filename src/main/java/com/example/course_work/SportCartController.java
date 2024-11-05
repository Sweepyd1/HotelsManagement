package com.example.course_work;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SportCartController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label countLabel;


    public void setData(String title, String description, String count) {
        titleLabel.setText(title);
        descriptionLabel.setText(description);
        countLabel.setText(count);
    }

}