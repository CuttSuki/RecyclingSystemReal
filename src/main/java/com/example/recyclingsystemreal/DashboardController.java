package com.example.recyclingsystemreal;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;


public class DashboardController {
    @FXML private Label totalBottlesLabel;
    @FXML private Label pointsBalanceLabel;
    @FXML private Label rewardsRedeemedLabel;
    @FXML private Label studentNameLabel;
    @FXML private Label studentIdLabel;
    @FXML private TextField bottleCountTextField;
    @FXML private Label pointsPreviewLabel;
    @FXML private Label dashboardRankLabel;
    @FXML private Label yourRankLabel;
    @FXML private Button leaderboardButton;
    @FXML private VBox leaderboardView;
    @FXML private VBox dashboardView;


    @FXML private void initialize() throws SQLException {
        LeaderboardRepo.createLeaderboardRepo();
        dashboardView.setVisible(true);
        leaderboardView.setVisible(false);
        dashboardRankLabel.setText(LeaderboardManager.checkRanking(UserData.getStudentUser().getStudentId()));
        yourRankLabel.setText("Your Rank: #" + LeaderboardManager.checkRanking(UserData.getStudentUser().getStudentId()));
        reloadLabels();
    }

    private void animateViewEntrance(Node view) {
        // 1. Set the initial state (hidden and shifted down)
        view.setOpacity(0.0);
        view.setTranslateY(20.0);

        // 2. Create the Fade In Transition
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), view);
        fadeIn.setToValue(1.0);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

        // 3. Create the Slide Up Transition
        TranslateTransition slideUp = new TranslateTransition(Duration.millis(600), view);
        slideUp.setToY(0.0);
        slideUp.setInterpolator(Interpolator.EASE_OUT);

        // 4. Combine and play them together
        ParallelTransition entranceAnimation = new ParallelTransition(fadeIn, slideUp);
        entranceAnimation.play();
    }

    private void reloadLabels() {
        totalBottlesLabel.setText(Integer.toString(UserData.getUserStats().getTotalBottles()));
        pointsBalanceLabel.setText(Integer.toString(UserData.getUserStats().getPointsBalance()));
        rewardsRedeemedLabel.setText(Integer.toString(UserData.getUserStats().getRewardsRedeemed()));
        studentNameLabel.setText(UserData.getStudentUser().getFirstName() + " " + UserData.getStudentUser().getLastName());
        studentIdLabel.setText(UserData.getStudentUser().getStudentId());
    }

    @FXML private void onSubmitBottlesButtonClicked(){
        if (!bottleCountTextField.getText().matches("\\d++")){
            return;
        }
        int currentBottles = UserData.getUserStats().getTotalBottles();
        int currentPointsBalance = UserData.getUserStats().getPointsBalance();
        int newBottleCount = Integer.parseInt(bottleCountTextField.getText());
        int newPoints = Integer.parseInt(pointsPreviewLabel.getText());
        UserData.getUserStats().setTotalBottles(currentBottles + newBottleCount);
        UserData.getUserStats().setPointsBalance(currentPointsBalance + newPoints);
        reloadLabels();
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

    @FXML private void onSignOutButtonClicked() throws IOException, SQLException {
        Stage stage = (Stage) totalBottlesLabel.getScene().getWindow();
        DataSaver.saveStudentStats();
        SceneSwitcher.switchScene(stage, "Login");
    }

    @FXML private void onLeaderboardButtonClicked(){
        leaderboardView.setVisible(true);
        dashboardView.setVisible(false);
        animateViewEntrance(leaderboardView);
    }

    @FXML private void onDashboardButtonClicked(){
        dashboardView.setVisible(true);
        leaderboardView.setVisible(false);
        animateViewEntrance(dashboardView);
    }

}
