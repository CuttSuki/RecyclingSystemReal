package com.example.recyclingsystemreal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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
    @Override
    public void stop() throws SQLException {
        System.out.println("Saving Data..");
        DataSaver.saveStudentStats();
    }
}
