package com.example.recyclingsystemreal;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField studentIdTextField;
    @FXML private TextField passwordTextField;
    @FXML private Hyperlink forgotPasswordHyperlink;
    @FXML private Hyperlink registerHyperlink;
    @FXML private Hyperlink adminLoginHyperlink;

    @FXML
    private void initialize(){

    }
    @FXML
    private void onSignInButtonClicked(){
        try {
            String studentId = studentIdTextField.getText();
            String password = passwordTextField.getText();
            if (studentId.isEmpty() || password.isEmpty()){
                Alerter.showAlert(Alert.AlertType.ERROR, "Missing Input field", "Please make an input on every fields!");
                return;
            }
            if(LoginValidator.validateLogin(studentId, password)){
                Stage stage = (Stage) studentIdTextField.getScene().getWindow();
                StudentUser studentUser = UserDataCreator.createStudentUserData(studentId);
                UserData.setStudentUser(studentUser);
                SceneSwitcher.switchScene(stage, "Dashboard");
            } else {
                Alerter.showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "Please input valid credentials.");
            }
        } catch (Exception e){
            e.printStackTrace();
            Alerter.showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occured: " + e.getMessage());
        }
    }

    @FXML
    private void onRegisterHyperlinkClicked() throws IOException {
        Stage stage = (Stage) studentIdTextField.getScene().getWindow();
        SceneSwitcher.switchScene(stage, "Register");
    }

    @FXML
    private void onAdminLoginHyperlinkClicked() throws IOException{
        Stage stage = (Stage) studentIdTextField.getScene().getWindow();
        SceneSwitcher.switchScene(stage, "AdminLogin");
    }
}
