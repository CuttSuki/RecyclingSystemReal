module com.example.recyclingsystemreal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires password4j;
    requires io.github.cdimascio.dotenv.java;

    opens com.example.recyclingsystemreal to javafx.fxml;
    exports com.example.recyclingsystemreal;
}