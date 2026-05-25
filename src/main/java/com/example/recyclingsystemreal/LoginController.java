package com.example.recyclingsystemreal;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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

    }

    @FXML
    private void onRegisterHyperlinkClicked() throws IOException {
        Stage stage = (Stage) studentIdTextField.getScene().getWindow();
        SceneSwitcher.switchScene(stage, "Register");
    }

    @FXML
    private void onAdminLoginHyperlinkClicked(){

    }
}
