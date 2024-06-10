module com.example.guiclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.guiclient to javafx.fxml;
    exports com.example.guiclient;
}