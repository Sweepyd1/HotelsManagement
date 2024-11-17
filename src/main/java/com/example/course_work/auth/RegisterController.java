package com.example.course_work.auth;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.example.course_work.App;
import com.example.course_work.SessionManager;
import com.example.course_work.database.UserCrud;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class RegisterController {

    @FXML
    private TextField loginField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private Text authorization;

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
        App.setRoot("auth/login");
    }

    private void switchToMain() throws IOException {
        App.setRoot("mainWindow");
    }

    @FXML
    private void createUser() {
        String name = nameField.getText();
        String password = passwordField.getText();
        String login = loginField.getText();
        String surname = surnameField.getText();


        if (login.isEmpty() || password.isEmpty() ||name.isEmpty()||surname.isEmpty()) {
            System.out.println("Все поля должны быть заполнены.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hotel", "postgres", "sweepy2006")) {
            UserCrud userCrud = new UserCrud(connection);
            userCrud.createUser(name, password,login,surname,"user");
            try {

                SessionManager.getInstance().setId(userCrud.getUserId(login, password));
                switchToMain();
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
