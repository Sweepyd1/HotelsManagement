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

public class LoginController {

    @FXML
    TextField loginField;

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
        App.setRoot("auth/register");
    }


    @FXML
    private void checkUsernameAndPassword(){
        String login = loginField.getText();
        String password = passwordField.getText();


        if (login.isEmpty() || password.isEmpty()) {
            System.out.println("Все поля должны быть заполнены.");
            return;
        }


        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hotel", "postgres", "sweepy2006")) {
            UserCrud userCrud = new UserCrud(connection);
            boolean isExist = userCrud.userExists(login, password);

            if (isExist){
                try {
                    SessionManager.getInstance().setId(userCrud.getUserId(login, password));
                    SessionManager.getInstance().setUserRole(userCrud.getUserRole(login, password));
                    String role =  SessionManager.getInstance().getUserRole();
                    System.out.println(role);


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
