package com.example.recyclingsystemreal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField studentIdTextField;
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private ComboBox<String> yearLevelComboBox;
    @FXML private TextField passwordTextField;
    @FXML private TextField confirmPasswordTextField;
    @FXML private Button createAccountButton;

    @FXML private void initialize(){
        for (Department departmentChoice : DepartmentRepository.departmentRepo){
            departmentComboBox.getItems().add(departmentChoice.getDepartmentName());
        }

        for (YearLevel yearLevel : YearLevelRepository.yearLevelRepo){
            yearLevelComboBox.getItems().add(yearLevel.getYearLevel());
        }
    }

    @FXML private void onCreateAccountButtonClicked(){
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String studentId = studentIdTextField.getText();
        String departmentName = departmentComboBox.getValue();
        String yearLevel = yearLevelComboBox.getValue();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        if (firstName.isEmpty() || lastName.isEmpty() || studentId.isEmpty() || departmentName.isEmpty() || yearLevel.isEmpty() ||  password.isEmpty() || confirmPassword.isEmpty()){
            return;
        }
        int departmentId = DepartmentRepository.getDepartmentId(departmentName);
        if (departmentId == -1){
            return;
        }
        String hashedPassword = PasswordHasher.hashPassword(password);
    }

    @FXML private void onSignInHyperlinkClicked() throws IOException {
        Stage stage = (Stage) firstNameTextField.getScene().getWindow();
        SceneSwitcher.switchScene(stage, "Login");
    }
}
