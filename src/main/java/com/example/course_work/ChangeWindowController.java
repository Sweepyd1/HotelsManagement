package com.example.course_work;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ChangeWindowController {

    @FXML
    private AnchorPane mainContent;
    @FXML
    private Button mainButton; // Кнопка "Главная"
    @FXML
    private Button busketButton; // Кнопка "Корзина"
    @FXML
    private Button adminButton; // Кнопка "Админ Панель"
    @FXML
    private Text PageTitle;


    @FXML
    public void showHomePage() {
        loadPage("mainPage.fxml", "Главная");
        highlightButton(mainButton);
    }

    @FXML
    public void showBusketPage() {
        loadPage("busket.fxml", "Корзина");
        highlightButton(busketButton);
    }

    @FXML
    public void showAdminPanelPage() {
        loadPage("admin.fxml", "Админ");
        highlightButton(adminButton);
    }

    private void loadPage(String fxmlFile, String title) {
        try {
            resetButtonStyles();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent page = loader.load();
            mainContent.getChildren().clear();
            mainContent.getChildren().add(page);
            PageTitle.setText(title);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void highlightButton(Button button) {
        button.setStyle("-fx-background-color:  #489abe;"); // Цвет выделенной кнопки
    }
    private void resetButtonStyles() {
        mainButton.setStyle("-fx-background-color:#1e222b; -fx-border-color:#489abe;-fx-border-radius:5;"); // Сбрасываем стиль кнопки "Главная"
        busketButton.setStyle("-fx-background-color:#1e222b; -fx-border-color:#489abe;-fx-border-radius:5;"); // Сбрасываем стиль кнопки "Корзина"
        adminButton.setStyle("-fx-background-color:#1e222b; -fx-border-color:#489abe;-fx-border-radius:5;"); // Сбрасываем стиль кнопки "Админ Панель"
    }
}
