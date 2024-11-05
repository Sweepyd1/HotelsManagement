package com.example.course_work;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.example.course_work.database.UserCrud;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private Text authorization; // Ссылка на текст, который будет кликабельным

    @FXML
    public void initialize() {

        authorization.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                switchToLogin();
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }

    private void switchToMain() throws IOException {
        App.setRoot("mainWindow");
    }



    @FXML
    private void createUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();


        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Все поля должны быть заполнены.");
            return;
        }


        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sport_shop", "postgres", "sweepy2006")) {
            UserCrud userCrud = new UserCrud(connection);
            userCrud.createUser(username, password, "client");
            try {
                switchToMain();
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
