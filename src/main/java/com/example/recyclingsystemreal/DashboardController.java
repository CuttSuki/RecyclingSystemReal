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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    @FXML private Button leaderboardButton;
    @FXML private HBox topLeaderboardHBox;
    @FXML private VBox leaderboardView;
    @FXML private VBox dashboardView;
    @FXML private VBox rewardsView;
    @FXML private TableColumn<LeaderboardStudent, String> rankColumn;
    @FXML private TableColumn<LeaderboardStudent, String> firstNameColumn;
    @FXML private TableColumn<LeaderboardStudent, String> lastNameColumn;
    @FXML private TableColumn<LeaderboardStudent, String> departmentNameColumn;
    @FXML private TableColumn<LeaderboardStudent, String> yearLevelNameColumn;
    @FXML private TableColumn<LeaderboardStudent, Integer> totalBottlesColumn;
    @FXML private TableView<LeaderboardStudent> leaderboardStudentTableView;

    @FXML private void initialize() throws SQLException {
        LeaderboardRepo.createLeaderboardRepo();
        dashboardView.setVisible(true);
        leaderboardView.setVisible(false);
        rewardsView.setVisible(false);
        dashboardRankLabel.setText(LeaderboardManager.checkRanking(UserData.getStudentUser().getStudentId()));
        yourRankLabel.setText("Your Rank: #" + LeaderboardManager.checkRanking(UserData.getStudentUser().getStudentId()));
        reloadLabels();
        initCells();
        setupLeaderboardPagination();
        processLeaderboardLabels();
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
            VBox childVBox = getVBoxFromId("Top" + Integer.toString(i + 1));
            if (childVBox == null){
                System.out.println("This VBOX doesnt exist.");
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

    private VBox getVBoxFromId(String vboxId){
        for (Node node : topLeaderboardHBox.getChildren()){
            VBox currentVBox = (VBox) node;
            if(vboxId.equals(currentVBox.getId())){
                return currentVBox;
            }
        }
        return null;
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