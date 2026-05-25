module com.example.recyclingsystemreal {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.recyclingsystemreal to javafx.fxml;
    exports com.example.recyclingsystemreal;
}