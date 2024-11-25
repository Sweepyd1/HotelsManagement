package com.example.course_work.models;

import java.sql.Date;
import java.sql.Timestamp;

public class BookingInfo {
    private int bookingID;        // Идентификатор бронирования
    private int userID;           // Идентификатор пользователя
    private String userName;      // Имя пользователя
    private String userSurname;   // Фамилия пользователя
    private Date checkInDate;     // Дата заезда
    private Date checkOutDate;    // Дата выезда
    private Timestamp bookingDate; // Дата бронирования
    private int countDay;         // Количество дней
    private double totalPrice;     // Итоговая цена
    private String titleRoom;      // Название комнаты

    // Конструктор класса
    public BookingInfo(int bookingID, int userID, String userName, String userSurname,
                       Date checkInDate, Date checkOutDate, Timestamp bookingDate,
                       int countDay, double totalPrice, String titleRoom) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.userName = userName;
        this.userSurname = userSurname;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingDate = bookingDate;
        this.countDay = countDay;
        this.totalPrice = totalPrice;
        this.titleRoom = titleRoom;
    }

    // Геттеры и сеттеры
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getCountDay() {
        return countDay;
    }

    public void setCountDay(int countDay) {
        this.countDay = countDay;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTitleRoom() {
        return titleRoom;
    }

    public void setTitleRoom(String titleRoom) {
        this.titleRoom = titleRoom;
    }
}