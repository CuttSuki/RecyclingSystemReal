package com.example.recyclingsystemreal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class DashboardController {
    @FXML private Label totalBottlesLabel;
    @FXML private Label pointsBalanceLabel;
    @FXML private Label rewardsRedeemedLabel;
    @FXML private Label studentNameLabel;
    @FXML private Label studentIdLabel;
    @FXML private TextField bottleCountTextField;
    @FXML private Label pointsPreviewLabel;
    @FXML private void initialize(){
        totalBottlesLabel.setText(Integer.toString(UserData.getUserRewards().getTotalBottles()));
        pointsBalanceLabel.setText(Integer.toString(UserData.getUserRewards().getPointsBalance()));
        rewardsRedeemedLabel.setText(Integer.toString(UserData.getUserRewards().getRewardsRedeemed()));
        studentNameLabel.setText(UserData.getStudentUser().getFirstName() + " " + UserData.getStudentUser().getLastName());
        studentIdLabel.setText(UserData.getStudentUser().getStudentId());
    }

    @FXML private void onSubmitBottlesButtonClicked(){
        if (!bottleCountTextField.getText().matches("\\d++")){
            return;
        }
        int currentBottles = UserData.getUserRewards().getTotalBottles();
        int currentPointsBalance = UserData.getUserRewards().getPointsBalance();
        int newBottleCount = Integer.parseInt(bottleCountTextField.getText());
        int newPoints = Integer.parseInt(pointsPreviewLabel.getText());
        UserData.getUserRewards().setTotalBottles(currentBottles + newBottleCount);
        UserData.getUserRewards().setPointsBalance(currentPointsBalance + newPoints);
        Alerter.showAlert(Alert.AlertType.CONFIRMATION, "Update success", "Bottle count and points preview has been updated!");
        bottleCountTextField.setText("");
    }

    @FXML private void onBottleCountTextFieldKeyTyped(){
        String pointsPreviewStr = bottleCountTextField.getText();
        System.out.println(pointsPreviewStr);
        if (pointsPreviewStr.matches("\\d++")){
            int pointsPreview = Integer.parseInt(pointsPreviewStr);
            pointsPreview = (pointsPreview >= 5) ? (pointsPreview + (pointsPreview/5)) : pointsPreview;
            pointsPreviewLabel.setText(Integer.toString(pointsPreview));
        } else {
            pointsPreviewLabel.setText("");
        }
    }
}
