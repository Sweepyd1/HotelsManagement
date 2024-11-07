package com.example.course_work;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;

import java.time.LocalDate;

public class SessionManager {

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    private Slider minPriceSlider;

    @FXML
    private Slider maxPriceSlider;

    @FXML
    private CheckBox wifiCheckBox;

    @FXML
    private CheckBox parkingCheckBox;

    @FXML
    private CheckBox poolCheckBox;

    @FXML
    private CheckBox breakfastCheckBox;

    @FXML
    private CheckBox gymCheckBox;

    @FXML
    private CheckBox spaCheckBox;

    @FXML
    private CheckBox petFriendlyCheckBox;

    // Метод для получения значений из всех элементов управления
    @FXML
    public void applyFilters() {
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();
        double minPrice = minPriceSlider.getValue();
        double maxPrice = maxPriceSlider.getValue();

        boolean wifi = wifiCheckBox.isSelected();
        boolean parking = parkingCheckBox.isSelected();
        boolean pool = poolCheckBox.isSelected();
        boolean breakfast = breakfastCheckBox.isSelected();
        boolean gym = gymCheckBox.isSelected();
        boolean spa = spaCheckBox.isSelected();
        boolean petFriendly = petFriendlyCheckBox.isSelected();

        // Здесь вы можете использовать полученные значения, например, для фильтрации данных
        System.out.println("Дата заезда: " + checkInDate);
        System.out.println("Дата выезда: " + checkOutDate);
        System.out.println("Минимальная цена: " + minPrice);
        System.out.println("Максимальная цена: " + maxPrice);
        System.out.println("Wi-Fi: " + wifi);
        System.out.println("Парковка: " + parking);
        System.out.println("Бассейн: " + pool);
        System.out.println("Завтрак включен: " + breakfast);
        System.out.println("Фитнес-центр: " + gym);
        System.out.println("Спа-процедуры: " + spa);
        System.out.println("Допускаются домашние животные: " + petFriendly);
    }
}