module com.example.course_work {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.course_work to javafx.fxml;
    exports com.example.course_work;
    exports com.example.course_work.auth;
    opens com.example.course_work.auth to javafx.fxml;
    exports com.example.course_work.controller.admin;
    opens com.example.course_work.controller.admin to javafx.fxml;
}