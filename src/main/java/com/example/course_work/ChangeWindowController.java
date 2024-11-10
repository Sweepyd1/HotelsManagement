package com.example.course_work;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.awt.desktop.SystemEventListener;
import java.time.LocalDate;

public class ChangeWindowController {

    public Button myBookingRooms;
    @FXML
    private AnchorPane mainContent;
    @FXML
    public Button mainButton; // Кнопка "Главная"
    @FXML
    public Button busketButton; // Кнопка "Корзина"
    @FXML
    public Button adminButton; // Кнопка "Админ Панель"
    @FXML
    private Text PageTitle;


    @FXML
    public void showHomePage() {
        loadPage("mainPage.fxml", "Главная");
//        highlightButton(mainButton);
    }


    @FXML
    public void showBookedPage() {
        loadPage("mybooked.fxml", "Главная");
//        highlightButton(mainButton);
    }

    @FXML
    public void showBusketPage() {
        LocalDate in =  SessionManager.getInstance().getInDate();
        LocalDate out =  SessionManager.getInstance().getOutDate();
        Boolean wifi = SessionManager.getInstance().isWifi();

        System.out.println(in);
        System.out.println(out);
        System.out.println(wifi);



        if (in == null || out == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null); // Убираем заголовок
            alert.setContentText("Пожалуйста, выберите даты заезда и выезда."); // Текст сообщения

            // Отображение окна
            alert.showAndWait();

        } else {
            loadPage("rooms.fxml", "Корзина");
        }
    }

    @FXML
    public void showAdminRoomsPage() {
        loadPage("admin_rooms.fxml", "комнаты");
//        highlightButton(adminButton);
    }

    @FXML
    public void showAdminBookingsPage() {
        loadPage("admin_bookings.fxml", "брони");
//        highlightButton(adminButton);
    }

    private void loadPage(String fxmlFile, String title) {
        try {
//            resetButtonStyles();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent page = loader.load();
            mainContent.getChildren().clear();
            mainContent.getChildren().add(page);
            PageTitle.setText(title);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void highlightButton(Button button) {
//        button.setStyle("-fx-background-color:  #489abe;"); // Цвет выделенной кнопки
//    }
//    private void resetButtonStyles() {
//        mainButton.setStyle("-fx-background-color:#282959; ");
//        busketButton.setStyle("-fx-background-color:#282959;");
//        adminButton.setStyle("-fx-background-color:#282959; "); // Сбрасываем стиль кнопки "Админ Панель"
//    }
}
