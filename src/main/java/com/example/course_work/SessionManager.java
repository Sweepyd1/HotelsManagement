package com.example.course_work;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.time.LocalDate;
import com.example.course_work.ChangeWindowController;


public class SessionManager {

    public static SessionManager instance;

    public int userId;
    public String userRole;

    public LocalDate checkInDate;
    public LocalDate checkOutDate;

    // Новые параметры
    public double minPrice;
    public double maxPrice;

    public boolean wifi;
    public boolean breakfast;
    public boolean spa;
    public boolean petFriendly;

    public int capacity;

    @FXML
    public DatePicker checkInDatePicker;

    @FXML
    public DatePicker checkOutDatePicker;

    @FXML
    public Slider minPriceSlider;

    @FXML
    public Slider maxPriceSlider;

    @FXML
    public CheckBox wifiCheckBox;

    @FXML
    public CheckBox breakfastCheckBox;

    @FXML
    public CheckBox spaCheckBox;

    @FXML
    public CheckBox petFriendlyCheckBox;


    @FXML
    private Spinner<Integer> capacitySpinner;

    // Приватный конструктор для предотвращения создания экземпляров извне
    public SessionManager() {}

    // Метод для получения единственного экземпляра класса
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Методы для установки и получения идентификатора пользователя
    public void setId(int id) {
        this.userId = id;
    }

    public int getId() {
        return this.userId;
    }

    // Методы для установки и получения дат
    public void setInDate(LocalDate in) {
        this.checkInDate = in;
    }

    public LocalDate getInDate() {
        return this.checkInDate;
    }

    public void setOutDate(LocalDate out) {
        this.checkOutDate = out;
    }

    public LocalDate getOutDate() {
        return this.checkOutDate;
    }

    // Новые сеттеры и геттеры для дополнительных параметров

    public void setMinPrice(double min) {
        this.minPrice = min;
    }

    public double getMinPrice() {
        return this.minPrice;
    }

    public void setMaxPrice(double max) {
        this.maxPrice = max;
    }

    public double getMaxPrice() {
        return this.maxPrice;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isWifi() {
        return this.wifi;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isBreakfast() {
        return this.breakfast;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }

    public boolean isSpa() {
        return this.spa;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public boolean isPetFriendly() {
        return this.petFriendly;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setUserRole(String role){
        this.userRole = role;
    }
    public String getUserRole(){
        return this.userRole;
    }


    @FXML
    public void applyFilters() {
        // Получаем значения из элементов управления
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();
        LocalDate currentDate = LocalDate.now();

        if (checkInDate == null || checkOutDate == null) {
            showAlert("Ошибка", "Пожалуйста, выберите даты заезда и выезда.");
            return;
        }

        // Проверка, что даты не меньше текущего дня
        if (checkInDate.isBefore(currentDate) || checkOutDate.isBefore(currentDate)) {
            showAlert("Ошибка", "Выбранные даты не могут быть меньше текущего дня.");
            return;
        }

        // Проверка, что дата заезда не больше даты выезда
        if (checkInDate.isAfter(checkOutDate)) {
            showAlert("Ошибка", "Дата заезда не может быть позже даты выезда.");
            return;
        }



        double minPrice = minPriceSlider.getValue();
        double maxPrice = maxPriceSlider.getValue();
        boolean wifi = wifiCheckBox.isSelected();
        boolean breakfast = breakfastCheckBox.isSelected();
        boolean spa = spaCheckBox.isSelected();
        boolean petFriendly = petFriendlyCheckBox.isSelected();
        int selectedCapacity = capacitySpinner.getValue(); // Получаем выбранное количество мест

        // Устанавливаем значения в SessionManager
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.setInDate(checkInDate);
        sessionManager.setOutDate(checkOutDate);
        sessionManager.setMinPrice(minPrice);
        sessionManager.setMaxPrice(maxPrice);
        sessionManager.setWifi(wifi);
        sessionManager.setBreakfast(breakfast);
        sessionManager.setSpa(spa);
        sessionManager.setPetFriendly(petFriendly);
        sessionManager.setCapacity(selectedCapacity);






        // Логика применения фильтров...
        System.out.println("Выбрано количество мест: " + selectedCapacity);

        System.out.println("Filters applied: " +
                "Check-in: " + checkInDate +
                ", Check-out: " + checkOutDate +
                ", Min Price: " + minPrice +
                ", Max Price: " + maxPrice +
                ", WiFi: " + wifi +
                ", Breakfast: " + breakfast +
                ", Spa: " + spa +
                ", Pet Friendly: " + petFriendly);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

