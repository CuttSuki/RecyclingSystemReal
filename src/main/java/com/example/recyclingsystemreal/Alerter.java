package com.example.recyclingsystemreal;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerter {

    // Private constructor prevents instantiation
    private Alerter() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Displays a JavaFX Alert dialog.
     *
     * @param type    The type of alert (e.g., INFORMATION, ERROR, WARNING, CONFIRMATION)
     * @param header  The header text (set to null if you don't want a header)
     * @param message The main content text of the alert
     */
    public static void showAlert(AlertType type, String header, String message) {
        Alert alert = new Alert(type);

        // Capitalize the first letter of the enum for a clean window title
        String title = type.name().charAt(0) + type.name().substring(1).toLowerCase();
        alert.setTitle(title);

        alert.setHeaderText(header);
        alert.setContentText(message);

        // showAndWait() ensures the code pauses until the user closes the alert
        alert.showAndWait();
    }
}