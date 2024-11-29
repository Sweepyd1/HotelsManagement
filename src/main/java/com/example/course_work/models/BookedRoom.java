package com.example.course_work.models;

import java.time.LocalDate;

public class BookedRoom {
    private String roomNumber;
    private String description;
    private int capacity;
    private double price;
    private String photo;
    private LocalDate indate;
    private LocalDate outdate;
    private int bookingdate;

    // Конструктор для создания забронированной комнаты с указанием всех параметров
    public BookedRoom(String roomNumber, String description, int capacity, double price, String photo,
                      LocalDate indate, LocalDate outdate, int bookingdate) {
        this.roomNumber = roomNumber;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.photo = photo;
        this.indate = indate;
        this.outdate = outdate;
        this.bookingdate = bookingdate;
    }

    // Геттеры и сеттеры
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LocalDate getIndate() {
        return indate;
    }

    public void setIndate(LocalDate indate) {
        this.indate = indate;
    }

    public LocalDate getOutdate() {
        return outdate;
    }

    public void setOutdate(LocalDate outdate) {
        this.outdate = outdate;
    }

    public int getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(int bookingdate) {
        this.bookingdate = bookingdate;
    }
}