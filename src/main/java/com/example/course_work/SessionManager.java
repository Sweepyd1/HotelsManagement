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
    private CheckBox breakfastCheckBox;

    private int userId;

    @FXML
    private CheckBox spaCheckBox;

    @FXML
    private CheckBox petFriendlyCheckBox;

    public void setId(int id){
        this.userId = id;
    }
    public int getId(){
        return this.userId;
    }

    // Метод для получения значений из всех элементов управления
    @FXML
    public void applyFilters() {
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();
        double minPrice = minPriceSlider.getValue();
        double maxPrice = maxPriceSlider.getValue();

        boolean wifi = wifiCheckBox.isSelected();


        boolean breakfast = breakfastCheckBox.isSelected();

        boolean spa = spaCheckBox.isSelected();
        boolean petFriendly = petFriendlyCheckBox.isSelected();

        // Здесь вы можете использовать полученные значения, например, для фильтрации данных
        System.out.println("Дата заезда: " + checkInDate);
        System.out.println("Дата выезда: " + checkOutDate);
        System.out.println("Минимальная цена: " + minPrice);
        System.out.println("Максимальная цена: " + maxPrice);
        System.out.println("Wi-Fi: " + wifi);


        System.out.println("Завтрак включен: " + breakfast);

        System.out.println("Спа-процедуры: " + spa);
        System.out.println("Допускаются домашние животные: " + petFriendly);
    }
}