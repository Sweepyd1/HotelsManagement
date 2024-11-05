module com.example.course_work {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.course_work to javafx.fxml;
    exports com.example.course_work;
}