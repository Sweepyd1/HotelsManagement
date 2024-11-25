package com.example.course_work.models;

public class UserForAdmin {

    private int id;

    private String username;
    private String login;
    private String password;
    private String role;
    private String usersurname;


    public UserForAdmin(int id,String username, String usersurname,String login, String password, String role) {
        this.id = id;
        this.username = username;
        this.usersurname = usersurname;
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


    public String getLogin() { return login; }



    public String getSurname() { return usersurname; }

    public void setSurname(String usersurname) { this.usersurname = usersurname; }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

