package com.example.recyclingsystemreal;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;


public class DashboardController {

    private static final int LEADERBOARD_ROWS_PER_PAGE = 8;

    @FXML private Label totalBottlesLabel;
    @FXML private Label pointsBalanceLabel;
    @FXML private Label rewardsRedeemedLabel;
    @FXML private Label studentNameLabel;
    @FXML private Label studentIdLabel;
    @FXML private TextField bottleCountTextField;
    @FXML private Label pointsPreviewLabel;
    @FXML private Label dashboardRankLabel;
    @FXML private Label yourRankLabel;
    @FXML private Label rewardsPointsBalanceLabel;
    @FXML private Button leaderboardButton;
    @FXML private HBox topLeaderboardHBox;
    @FXML private VBox leaderboardView;
    @FXML private VBox dashboardView;
    @FXML private VBox rewardsView;
    @FXML private FlowPane rewardsFlowPane;
    @FXML private TableColumn<LeaderboardStudent, String> rankColumn;
    @FXML private TableColumn<LeaderboardStudent, String> firstNameColumn;
    @FXML private TableColumn<LeaderboardStudent, String> lastNameColumn;
    @FXML private TableColumn<LeaderboardStudent, String> departmentNameColumn;
    @FXML private TableColumn<LeaderboardStudent, String> yearLevelNameColumn;
    @FXML private TableColumn<LeaderboardStudent, Integer> totalBottlesColumn;
    @FXML private TableView<LeaderboardStudent> leaderboardStudentTableView;

    @FXML private void initialize() throws SQLException {
        LeaderboardRepo.createLeaderboardRepo();
        RewardsRepo.createRewardsRepo();
        dashboardView.setVisible(true);
        leaderboardView.setVisible(false);
        rewardsView.setVisible(false);
        dashboardRankLabel.setText(LeaderboardManager.checkRanking(UserData.getStudentUser().getStudentId()));
        yourRankLabel.setText("Your Rank: #" + LeaderboardManager.checkRanking(UserData.getStudentUser().getStudentId()));
        reloadLabels();
        initCells();
        setupLeaderboardPagination();
        processLeaderboardLabels();
        setupRewardCards();
    }

    private void initCells(){
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        departmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        yearLevelNameColumn.setCellValueFactory(new PropertyValueFactory<>("yearLevelName"));
        totalBottlesColumn.setCellValueFactory(new PropertyValueFactory<>("totalBottles"));
    }

    /**
     * Sets up pagination for the leaderboard table view.
     * A Pagination control is created in code and appended below the
     * TableView inside its parent VBox. The page factory does not return
     * the TableView itself (it remains in its original FXML position);
     * instead it simply updates the TableView's items for the selected
     * page and returns an empty placeholder node.
     */
    private void setupLeaderboardPagination() {
        ObservableList<LeaderboardStudent> allStudents = LeaderboardRepo.getLeaderboardStudents();

        int pageCount = (int) Math.ceil((double) allStudents.size() / LEADERBOARD_ROWS_PER_PAGE);
        pageCount = Math.max(pageCount, 1);

        Pagination leaderboardPagination = new Pagination(pageCount, 0);
        leaderboardPagination.setMaxPageIndicatorCount(5);
        leaderboardPagination.setStyle("-fx-page-information-visible: false;");

        leaderboardPagination.setPageFactory(pageIndex -> {
            updateLeaderboardTablePage(allStudents, pageIndex);
            return new VBox(); // empty placeholder; TableView stays where it is in the FXML layout
        });

        // Append the pagination control below the leaderboard TableView
        Node tableViewParent = leaderboardStudentTableView.getParent();
        if (tableViewParent instanceof VBox) {
            ((VBox) tableViewParent).getChildren().add(leaderboardPagination);
        }

        // Load the first page immediately
        updateLeaderboardTablePage(allStudents, 0);
    }

    /**
     * Updates the leaderboard TableView's items to show only the rows
     * belonging to the given page index.
     */
    private void updateLeaderboardTablePage(ObservableList<LeaderboardStudent> allStudents, int pageIndex) {
        int fromIndex = pageIndex * LEADERBOARD_ROWS_PER_PAGE;

        if (fromIndex >= allStudents.size()) {
            leaderboardStudentTableView.setItems(FXCollections.observableArrayList());
            return;
        }

        int toIndex = Math.min(fromIndex + LEADERBOARD_ROWS_PER_PAGE, allStudents.size());
        ObservableList<LeaderboardStudent> pageItems =
                FXCollections.observableArrayList(allStudents.subList(fromIndex, toIndex));
        leaderboardStudentTableView.setItems(pageItems);
    }

    private void processLeaderboardLabels() {
        //Get the top 3 students
        for(int i = 0; i < 3; ++i) {
            LeaderboardStudent leaderboardStudent = LeaderboardRepo.getLeaderboardStudents().get(i);
            VBox childVBox = (VBox) topLeaderboardHBox.lookup("#Top" + Integer.toString(i + 1));
            if (childVBox == null){
                System.out.println("This VBOX ID doesnt exist: " + "Top" + Integer.toString(i + 1));
                break;
            }
            Label nameLabel = (Label) childVBox.getChildren().get(1);
            Label departmentLabel = (Label) childVBox.getChildren().get(2);
            Label totalBottlesLabel = (Label) childVBox.getChildren().get(3);
            String studentName = leaderboardStudent.getFirstName() + " " + leaderboardStudent.getLastName();
            String department = leaderboardStudent.getDepartmentName() + " " + leaderboardStudent.getYearLevelName();
            String totalBottles = Integer.toString(leaderboardStudent.getTotalBottles()) + " Bottles";
            nameLabel.setText(studentName);
            departmentLabel.setText(department);
            totalBottlesLabel.setText(totalBottles);
        }
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
        rewardsPointsBalanceLabel.setText(Integer.toString(UserData.getUserStats().getPointsBalance()) + " points");
        rewardsRedeemedLabel.setText(Integer.toString(UserData.getUserStats().getRewardsRedeemed()));
        studentNameLabel.setText(UserData.getStudentUser().getFirstName() + " " + UserData.getStudentUser().getLastName());
        studentIdLabel.setText(UserData.getStudentUser().getStudentId());
    }

    private void setupRewardCards(){
        for (Rewards reward : RewardsRepo.getRewardsRepo()){
            rewardsFlowPane.getChildren().add(createRewardVBox(reward.getRewardName(), reward.getRewardName(),
                    reward.getDescription(), Integer.toString(reward.getPointCost()) + " points"));
        }
    }
    public VBox createRewardVBox(String emojiIcon, String title, String description, String points) {
        // 1. Root VBox
        VBox rootVBox = new VBox(8);
        rootVBox.setId("rewardVBoxTemplate");
        rootVBox.setPrefSize(200, 230);
        rootVBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #D8EBE0; -fx-border-radius: 12; -fx-padding: 20;");

        // 2. Icon Container (VBox)
        VBox iconContainer = new VBox();
        iconContainer.setStyle("-fx-background-color: #E1F5EE; -fx-background-radius: 8; -fx-alignment: center; -fx-min-height: 80; -fx-pref-height: 80;");
        Label iconLabel = new Label(emojiIcon);
        iconLabel.setStyle("-fx-font-size: 36px;");
        iconContainer.getChildren().add(iconLabel);

        // 3. Title Label
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1A3A2A;");

        // 4. Description Label
        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B8C7A; -fx-wrap-text: true;");

        // 5. Spacer Region
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // 6. Bottom HBox (Points & Redeem Button)
        HBox bottomHBox = new HBox();
        bottomHBox.setStyle("-fx-alignment: center-left;");

        Label pointsLabel = new Label(points);
        pointsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1D9E75;");
        pointsLabel.setMaxWidth(Double.MAX_VALUE); // Necessary for HBox.hgrow to work properly
        HBox.setHgrow(pointsLabel, Priority.ALWAYS);

        Button redeemButton = new Button("Redeem");
        redeemButton.setStyle("-fx-background-color: #1D9E75; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 6; -fx-border-radius: 6; -fx-padding: 6 12; -fx-cursor: hand;");

        // Optional: Add action event to the button
        // redeemButton.setOnAction(e -> System.out.println("Redeemed: " + title));

        bottomHBox.getChildren().addAll(pointsLabel, redeemButton);

        // 7. Assemble the Root VBox
        rootVBox.getChildren().addAll(iconContainer, titleLabel, descLabel, spacer, bottomHBox);

        return rootVBox;
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
        rewardsView.setVisible(false);
        animateViewEntrance(leaderboardView);
    }

    @FXML private void onDashboardButtonClicked(){
        dashboardView.setVisible(true);
        leaderboardView.setVisible(false);
        rewardsView.setVisible(false);
        animateViewEntrance(dashboardView);
    }

    @FXML private void onRewardsButtonClicked(){
        leaderboardView.setVisible(false);
        dashboardView.setVisible(false);
        rewardsView.setVisible(true);
        animateViewEntrance(rewardsView);
    }

}