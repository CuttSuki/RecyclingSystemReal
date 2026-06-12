package com.example.recyclingsystemreal;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
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

    @FXML
    private void onCreateAccountButtonClicked() {
        try {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String studentId = studentIdTextField.getText();
            String departmentName = departmentComboBox.getValue();
            String yearLevel = yearLevelComboBox.getValue();
            String password = passwordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();

            // Note: If nothing is selected in the ComboBox, getValue() might return null,
            // which would throw a NullPointerException here when calling .isEmpty().
            // The try-catch will handle the crash, but you may want to check for null as well.
            if (firstName.isEmpty() || lastName.isEmpty() || studentId.isEmpty() ||
                    departmentName == null || departmentName.isEmpty() ||
                    yearLevel == null || yearLevel.isEmpty() ||
                    password.isEmpty() || confirmPassword.isEmpty()) {

                Alerter.showAlert(Alert.AlertType.ERROR, "Missing input field", "You must input all fields!");
                return;
            }

            int departmentId = DepartmentRepository.getDepartmentId(departmentName);
            if (departmentId == -1) {
                Alerter.showAlert(Alert.AlertType.ERROR, "Unknown Department ID", "Unknown Department ID.");
                return;
            }

            int yearLevelId = YearLevelRepository.getYearLevelId(yearLevel);
            if (yearLevelId == -1) {
                Alerter.showAlert(Alert.AlertType.ERROR, "Unknown Year Level ID", "Unknown Year Level ID.");
                return;
            }

            String hashedPassword = PasswordHasher.hashPassword(password);
            if (!PasswordHasher.checkPassword(confirmPassword, hashedPassword)) {
                Alerter.showAlert(Alert.AlertType.ERROR, "Wrong password", "You failed to confirm your password!");
                return;
            }

            // Assuming database insertion logic goes here

            Alerter.showAlert(Alert.AlertType.CONFIRMATION, "Register Success", "Your account has been registered successfully! Please head into the login page.");
            Database.registerUser(firstName, lastName, studentId, departmentId, yearLevelId, hashedPassword);
        } catch (Exception e) {
            // Prints the error to the console for debugging
            e.printStackTrace();

            // Shows a user-friendly error dialog
            Alerter.showAlert(Alert.AlertType.ERROR, "Registration Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML private void onSignInHyperlinkClicked() throws IOException {
        Stage stage = (Stage) firstNameTextField.getScene().getWindow();
        SceneSwitcher.switchScene(stage, "Login");
    }
}
