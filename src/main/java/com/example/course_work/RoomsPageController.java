package com.example.course_work;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
public class RoomsPageController {


    @FXML
    private VBox cardsContainer;

    @FXML
    public void initialize() {
        try {
            addCards();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void addCards() throws IOException {
        for (int i = 1; i <= 10; i++) {
            FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("hotel.fxml"));
            VBox card = cardLoader.load();
            SportCartController cardController = cardLoader.getController();
            cardController.setData("Title " + i, "Description for card " + i, "count"+i);
            cardsContainer.getChildren().add(card);
        }
    }
}
