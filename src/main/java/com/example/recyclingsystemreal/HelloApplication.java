package com.example.recyclingsystemreal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        SceneSwitcher.switchScene(stage, "Login");
        Database.testDatabaseConnection();
        DepartmentRepository.createDepartmentRepo();
        YearLevelRepository.createYearLevelRepo();
    }
}
