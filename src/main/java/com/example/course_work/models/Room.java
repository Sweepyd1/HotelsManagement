package com.example.course_work.models;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;



public class Room {
    private String roomNumber;
    private String description;
    private int capacity;
    private int price;

    private String photo;
    private int serviceId;

    public Room(String roomNumber, String description, int capacity, int price, String photo, int serviceId) {
        this.roomNumber = roomNumber;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.photo = photo;
        this.serviceId = serviceId;
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
    public int getPrice() {
        return price;
    }

    // Сеттер для цены
    public void setPrice(int price) {
        this.price = price;
    }




    // Геттер для статуса


    // Геттер для фото
    public String getPhoto() {
        return photo;
    }


    public void setServiceId(int service) {
        this.serviceId = service;
    }

    public int getServiceId() {
        return serviceId;
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
                ", serviceId'" + serviceId + '\''+
                '}';
    }
}