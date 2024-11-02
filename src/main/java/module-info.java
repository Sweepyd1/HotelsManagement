module com.course_work {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.course_work to javafx.fxml;
    exports com.course_work;
}
