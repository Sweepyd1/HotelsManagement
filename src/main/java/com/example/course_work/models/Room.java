package com.example.course_work.models;
import javafx.beans.property.*;

public class Room {
    private String roomNumber;
    private String description;
    private int capacity;
    private double price;
    private String status;
    private String photo;

    public Room(String roomNumber, String description, int capacity, double price, String status, String photo) {
        this.roomNumber = roomNumber;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.status = status;
        this.photo = photo;
    }

    // Геттеры
    public String getRoomNumber() { return roomNumber; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public String getPhoto() { return photo; }

    // Сеттеры
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public void setDescription(String description) { this.description = description; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setPrice(double price) { this.price = price; }
    public void setStatus(String status) { this.status = status; }
    public void setPhoto(String photo) { this.photo = photo; }
}