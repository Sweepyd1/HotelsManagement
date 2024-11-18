package com.example.course_work;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.desktop.SystemEventListener;
import java.time.LocalDate;

public class ChangeWindowController {

    public Button myBookingRooms;


    @FXML
    public Button bookings;

    @FXML
    public Button users;
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
    public void initialize() {
        // Проверяем роль пользователя и отображаем кнопку в зависимости от роли
        if ("admin".equals(SessionManager.getInstance().getUserRole())) {
            adminButton.setVisible(true); // Показываем кнопку для админа
            bookings.setVisible(true);

        } else {
            adminButton.setVisible(false); // Скрываем кнопку для других ролей
            bookings.setVisible(false);

        }
    }

    @FXML
    public void showHomePage() {
        loadPage("mainPage.fxml", "Главная");
//        highlightButton(mainButton);
    }


    @FXML
    public void showBookedPage() {
        loadPage("mybooked_page.fxml", "Главная");
//        highlightButton(mainButton);
    }

    @FXML
    public void showBusketPage() {
        // Open the filter window and wait for it to close
        boolean filtersClosed = openFilterWindow();

        // Proceed only if the filters window was closed without errors
        if (filtersClosed) {
            LocalDate in = SessionManager.getInstance().getInDate();
            LocalDate out = SessionManager.getInstance().getOutDate();
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
    }

    @FXML
    public void showAdminRoomsPage() {
        loadPage("admin/admin_rooms.fxml", "комнаты");
//        highlightButton(adminButton);
    }

    @FXML
    public void showAdminBookingsPage() {
        loadPage("admin/admin_bookings.fxml", "брони");
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

    public boolean openFilterWindow() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
            Parent root = loader.load();

            // Create a new Stage (window)
            Stage filterStage = new Stage();

            filterStage.setTitle("Фильтры");
            filterStage.initModality(Modality.APPLICATION_MODAL);

            // Set the scene with an appropriate size
            Scene scene = new Scene(root);
            filterStage.setScene(scene);

            filterStage.setMinWidth(300);
//            filterStage.setMinHeight(400);
            filterStage.setMaxHeight(480);

            filterStage.showAndWait();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
            return false; // Indicate that there was an error
        }
    }

}
