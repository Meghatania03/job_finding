module com.example.job_finding {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.google.gson;


    opens com.example.job_finding to javafx.fxml;
    exports com.example.job_finding;
}