package com.example.course_work.models;


public class User {
    private int id;
    private String name;
    private String username;
    private String login;
    private String password;
    private String role;

    public User(int id, String name, String username,String login, String password, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}