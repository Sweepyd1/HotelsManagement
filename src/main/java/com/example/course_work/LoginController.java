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

public class LoginController {

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    private Text register; // Ссылка на текст, который будет кликабельным

    @FXML
    public void initialize() {

        register.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                switchToRegister();
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    private void switchToMain() throws IOException {
        App.setRoot("mainWindow");
    }


    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("register");
    }


    @FXML
    private void checkUsernameAndPassword(){
        String username = usernameField.getText();
        String password = passwordField.getText();


        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Все поля должны быть заполнены.");
            return;
        }


        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sport_shop", "postgres", "sweepy2006")) {
            UserCrud userCrud = new UserCrud(connection);
            boolean isExist = userCrud.userExists(username, password);
            if (isExist){
                try {
                    switchToMain();
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
