package com.example.recyclingsystemreal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AdminLoginController {
    @FXML private TextField adminUsernameTextField;
    @FXML private TextField adminPasswordTextField;


    @FXML private void initialize(){
    }

    @FXML private void onBackToStudentLoginHyperlinkClicked() throws IOException {
        Stage stage = (Stage) adminPasswordTextField.getScene().getWindow();
        SceneSwitcher.switchScene(stage, "Login");
    }

    @FXML private void onAdminLoginButtonClicked() throws SQLException, IOException {
        String adminUsername = adminUsernameTextField.getText();
        String adminPassword = adminPasswordTextField.getText();
        if (adminUsername.isEmpty() || adminPassword.isEmpty()){
            return;
        }

        if (LoginValidator.validateAdminLogin(adminUsername, adminPassword)){
            Stage stage = (Stage) adminPasswordTextField.getScene().getWindow();
            SceneSwitcher.switchScene(stage, "AdminPanel");
        }
    }
}
