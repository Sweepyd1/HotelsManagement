package com.example.course_work.models;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Room {
    private final SimpleStringProperty roomNumber;
    private final SimpleStringProperty description;
    private final SimpleIntegerProperty capacity;
    private final SimpleDoubleProperty price;
    private final SimpleStringProperty status;
    private final SimpleStringProperty photo;

    public Room(String roomNumber, String description, int capacity, double price, String status, String photo) {
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.description = new SimpleStringProperty(description);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.price = new SimpleDoubleProperty(price);
        this.status = new SimpleStringProperty(status);
        this.photo = new SimpleStringProperty(photo);
    }

    public String getRoomNumber() { return roomNumber.get(); }
    public void setRoomNumber(String value) { roomNumber.set(value); }

    public String getDescription() { return description.get(); }
    public void setDescription(String value) { description.set(value); }

    public int getCapacity() { return capacity.get(); }
    public void setCapacity(int value) { capacity.set(value); }

    public double getPrice() { return price.get(); }

    public void setPrice(Double value) {
        price.set(value);
    }

    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }

    public String getPhoto() { return photo.get(); }
    public void setPhoto(String value) { photo.set(value); }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber.get() + '\'' +
                ", description='" + description.get() + '\'' +
                ", capacity=" + capacity.get() +
                ", price=" + price.get() +
                ", status='" + status.get() + '\'' +
                ", photo='" + photo.get() + '\'' +
                '}';
    }
}