package com.example.course_work.models;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Room {
    private String roomNumber;
    private String description;
    private int capacity;
    private double price;

    private String photo;

    public Room(String roomNumber, String description, int capacity, double price, String photo) {
        this.roomNumber = roomNumber;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.photo = photo;
    }

    // Геттер для номера комнаты
    public String getRoomNumber() {
        return roomNumber;
    }

    // Сеттер для номера комнаты
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    // Геттер для описания
    public String getDescription() {
        return description;
    }

    // Сеттер для описания
    public void setDescription(String description) {
        this.description = description;
    }

    // Геттер для вместимости
    public int getCapacity() {
        return capacity;
    }

    // Сеттер для вместимости
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // Геттер для цены
    public double getPrice() {
        return price;
    }

    // Сеттер для цены
    public void setPrice(double price) {
        this.price = price;
    }

    // Геттер для статуса


    // Геттер для фото
    public String getPhoto() {
        return photo;
    }

    // Сеттер для фото
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", description='" + description + '\'' +
                ", capacity=" + capacity +
                ", price=" + price +
                ", photo='" + photo + '\'' +
                '}';
    }
}