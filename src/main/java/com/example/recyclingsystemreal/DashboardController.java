package com.example.recyclingsystemreal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class DashboardController {
    @FXML private Label totalBottlesLabel;
    @FXML private Label pointsBalanceLabel;
    @FXML private Label rewardsRedeemedLabel;

    @FXML private void initialize(){
        totalBottlesLabel.setText(Integer.toString(UserData.getUserRewards().getTotalBottles()));
        pointsBalanceLabel.setText(Integer.toString(UserData.getUserRewards().getPointsBalance()));
        rewardsRedeemedLabel.setText(Integer.toString(UserData.getUserRewards().getRewardsRedeemed()));
    }

    @FXML private void onSubmitBottlesButtonClicked(){

    }
}
