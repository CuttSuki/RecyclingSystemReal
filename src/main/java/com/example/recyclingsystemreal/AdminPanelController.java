package com.example.recyclingsystemreal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AdminPanelController {

    private static final int ROWS_PER_PAGE = 8; // Adjust pagination limit here

    // --- FXML UI Elements ---

    // Stats Labels
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalBottlesAdminLabel;
    @FXML private Label totalPointsLabel;
    @FXML private Label rewardRedeemedLabel;

    // Student Table (Overview)
    @FXML private TextField searchField;
    @FXML private TableView<StudentAdminModel> studentsTable;
    @FXML private TableColumn<StudentAdminModel, String> adminIdCol;
    @FXML private TableColumn<StudentAdminModel, String> adminNameCol;
    @FXML private TableColumn<StudentAdminModel, String> adminDeptCol;
    @FXML private TableColumn<StudentAdminModel, Integer> adminBottlesCol;
    @FXML private TableColumn<StudentAdminModel, Integer> adminPtsCol;
    @FXML private TableColumn<StudentAdminModel, Void> adminActionsCol;

    // Quick Actions (Rewards)
    @FXML private TextField rewardNameField;
    @FXML private TextField rewardPtsField;

    // Views
    @FXML private VBox overviewView;
    @FXML private VBox transactionsView;
    @FXML private VBox manageRewardsView;

    // Transaction Table
    @FXML private TextField transactionsSearchField;
    @FXML private Pagination transactionsPagination;
    @FXML private TableView<TransactionAdminModel> transactionsTable;
    @FXML private TableColumn<TransactionAdminModel, Integer> transIdCol;
    @FXML private TableColumn<TransactionAdminModel, String> transStudentIdCol;
    @FXML private TableColumn<TransactionAdminModel, String> transTypeCol;
    @FXML private TableColumn<TransactionAdminModel, String> transDateCol;

    // Manage Rewards Table
    @FXML private TextField rewardsSearchField;
    @FXML private Pagination rewardsPagination;
    @FXML private TableView<RewardAdminModel> rewardsTable;
    @FXML private TableColumn<RewardAdminModel, Integer> rewardIdCol;
    @FXML private TableColumn<RewardAdminModel, String> rewardNameCol;
    @FXML private TableColumn<RewardAdminModel, Integer> rewardCostCol;
    @FXML private TableColumn<RewardAdminModel, String> rewardTypeCol;

    // Data Lists
    private ObservableList<StudentAdminModel> studentData = FXCollections.observableArrayList();

    private ObservableList<TransactionAdminModel> transactionData = FXCollections.observableArrayList();
    private FilteredList<TransactionAdminModel> filteredTransactions;

    private ObservableList<RewardAdminModel> rewardData = FXCollections.observableArrayList();
    private FilteredList<RewardAdminModel> filteredRewards;

    @FXML
    private void initialize() {
        try {
            loadSystemOverview();
            setupStudentsTable();
            setupTransactionsTable();
            setupRewardsTable();

            switchView(overviewView);
        } catch (SQLException e) {
            e.printStackTrace();
            Alerter.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load admin data.");
        }
    }

    // ==========================================
    // 1. SYSTEM OVERVIEW & STUDENTS SECTION
    // ==========================================

    private void loadSystemOverview() throws SQLException {
        try (Connection conn = Database.getConnection()) {
            String countSql = "SELECT COUNT(*) AS total FROM students";
            try (PreparedStatement stmt = conn.prepareStatement(countSql); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) totalStudentsLabel.setText(String.valueOf(rs.getInt("total")));
            }

            String statsSql = "SELECT SUM(total_bottles) as bottles, SUM(points_balance) as points, SUM(rewards_redeemed) as rewards FROM student_stats";
            try (PreparedStatement stmt = conn.prepareStatement(statsSql); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalBottlesAdminLabel.setText(String.valueOf(rs.getInt("bottles")));
                    totalPointsLabel.setText(String.valueOf(rs.getInt("points")));
                    rewardRedeemedLabel.setText(String.valueOf(rs.getInt("rewards")));
                }
            }
        }
    }

    private void setupStudentsTable() throws SQLException {
        adminIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        adminNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        adminDeptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        adminBottlesCol.setCellValueFactory(new PropertyValueFactory<>("bottles"));
        adminPtsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        // Make View Button Functional
        adminActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button actionBtn = new Button("View");
            {
                actionBtn.setStyle("-fx-background-color: #1D9E75; -fx-text-fill: white; -fx-cursor: hand;");
                actionBtn.setOnAction(event -> {
                    StudentAdminModel student = getTableView().getItems().get(getIndex());
                    String details = String.format(
                            "Student ID:\t%s\nName:\t\t%s\nDepartment:\t%s\n\nTotal Bottles Submitted: %d\nCurrent Points Balance: %d",
                            student.getStudentId(), student.getName(), student.getDepartment(),
                            student.getBottles(), student.getPoints()
                    );
                    Alerter.showAlert(Alert.AlertType.INFORMATION, student.getName() + " - Overview", details);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBtn);
            }
        });

        fetchStudentData();

        FilteredList<StudentAdminModel> filteredData = new FilteredList<>(studentData, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getName().toLowerCase().contains(lowerCaseFilter) ||
                        student.getStudentId().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<StudentAdminModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(studentsTable.comparatorProperty());
        studentsTable.setItems(sortedData);
    }

    private void fetchStudentData() throws SQLException {
        studentData.clear();
        String sql = "SELECT student_id, first_name, last_name, department_name, total_bottles, points_balance FROM vw_students_stats";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                studentData.add(new StudentAdminModel(
                        rs.getString("student_id"),
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getString("department_name"),
                        rs.getInt("total_bottles"),
                        rs.getInt("points_balance")
                ));
            }
        }
    }

    // ==========================================
    // 2. TRANSACTIONS SECTION
    // ==========================================

    private void setupTransactionsTable() throws SQLException {
        if (transactionsTable == null) return;

        transIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        transStudentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        transTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        transDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        fetchTransactionData();

        // 1. Setup Filtered List for Search
        filteredTransactions = new FilteredList<>(transactionData, p -> true);
        transactionsSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTransactions.setPredicate(trans -> {
                if (newValue == null || newValue.isEmpty()) return true;
                return trans.getStudentId().toLowerCase().contains(newValue.toLowerCase());
            });
            updateTransactionsPagination(); // Re-paginate when filtered
        });

        // 2. Setup Pagination
        transactionsPagination.setPageFactory(this::createTransactionPage);
        updateTransactionsPagination();
    }

    private void updateTransactionsPagination() {
        int pageCount = (int) Math.ceil((double) filteredTransactions.size() / ROWS_PER_PAGE);
        transactionsPagination.setPageCount(pageCount == 0 ? 1 : pageCount);
        transactionsPagination.setCurrentPageIndex(0);
        createTransactionPage(0); // Load first page manually on update
    }

    private VBox createTransactionPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredTransactions.size());

        if (fromIndex < filteredTransactions.size()) {
            transactionsTable.setItems(FXCollections.observableArrayList(filteredTransactions.subList(fromIndex, toIndex)));
        } else {
            transactionsTable.setItems(FXCollections.observableArrayList());
        }
        return new VBox(); // Pagination requires returning a Node, but we just want to update the TableView directly
    }

    private void fetchTransactionData() throws SQLException {
        transactionData.clear();
        try (Connection conn = Database.getConnection()) {
            String rewardSql = "SELECT transaction_id, student_id, created_at FROM transactions ORDER BY created_at DESC";
            try (PreparedStatement stmt = conn.prepareStatement(rewardSql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactionData.add(new TransactionAdminModel(rs.getInt("transaction_id"), rs.getString("student_id"), "Reward Claimed", rs.getTimestamp("created_at").toString()));
                }
            }
            String bottleSql = "SELECT transaction_id, student_id, created_at FROM bottle_transactions ORDER BY created_at DESC";
            try (PreparedStatement stmt = conn.prepareStatement(bottleSql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactionData.add(new TransactionAdminModel(rs.getInt("transaction_id"), rs.getString("student_id"), "Bottle Submission", rs.getTimestamp("created_at").toString()));
                }
            }
        }
    }

    // ==========================================
    // 3. MANAGE REWARDS SECTION
    // ==========================================

    private void setupRewardsTable() throws SQLException {
        if (rewardsTable == null) return;

        rewardIdCol.setCellValueFactory(new PropertyValueFactory<>("rewardId"));
        rewardNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rewardCostCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        rewardTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        fetchRewardData();

        // 1. Setup Filtered List for Search (By Name or ID)
        filteredRewards = new FilteredList<>(rewardData, p -> true);
        rewardsSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRewards.setPredicate(reward -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerFilter = newValue.toLowerCase();
                return reward.getName().toLowerCase().contains(lowerFilter) ||
                        String.valueOf(reward.getRewardId()).contains(lowerFilter);
            });
            updateRewardsPagination();
        });

        // 2. Setup Pagination
        rewardsPagination.setPageFactory(this::createRewardsPage);
        updateRewardsPagination();
    }

    private void updateRewardsPagination() {
        int pageCount = (int) Math.ceil((double) filteredRewards.size() / ROWS_PER_PAGE);
        rewardsPagination.setPageCount(pageCount == 0 ? 1 : pageCount);
        rewardsPagination.setCurrentPageIndex(0);
        createRewardsPage(0);
    }

    private VBox createRewardsPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredRewards.size());

        if (fromIndex < filteredRewards.size()) {
            rewardsTable.setItems(FXCollections.observableArrayList(filteredRewards.subList(fromIndex, toIndex)));
        } else {
            rewardsTable.setItems(FXCollections.observableArrayList());
        }
        return new VBox();
    }

    private void fetchRewardData() throws SQLException {
        rewardData.clear();
        String sql = "SELECT reward_id, reward_name, point_cost, reward_type FROM rewards";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rewardData.add(new RewardAdminModel(rs.getInt("reward_id"), rs.getString("reward_name"), rs.getInt("point_cost"), rs.getString("reward_type")));
            }
        }
    }

    @FXML
    private void onAddRewardClicked() {
        String name = rewardNameField.getText();
        String ptsStr = rewardPtsField.getText();

        if (name.isEmpty() || ptsStr.isEmpty() || !ptsStr.matches("\\d+")) {
            Alerter.showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid reward name and numeric point value.");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            int newId = 1;
            String maxIdSql = "SELECT MAX(reward_id) as max_id FROM rewards";
            try (PreparedStatement maxStmt = conn.prepareStatement(maxIdSql); ResultSet rs = maxStmt.executeQuery()) {
                if (rs.next()) newId = rs.getInt("max_id") + 1;
            }

            String insertSql = "INSERT INTO rewards (reward_id, reward_name, point_cost, reward_type) VALUES (?, ?, ?, 'General')";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setInt(1, newId);
                stmt.setString(2, name);
                stmt.setInt(3, Integer.parseInt(ptsStr));
                stmt.executeUpdate();
            }

            Alerter.showAlert(Alert.AlertType.CONFIRMATION, "Success", "Reward added successfully!");
            rewardNameField.clear();
            rewardPtsField.clear();

            if (rewardsTable != null) {
                fetchRewardData();
                updateRewardsPagination(); // Refresh table data and adjust pages
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alerter.showAlert(Alert.AlertType.ERROR, "Database Error", "Could not add reward.");
        }
    }

    // ==========================================
    // SIDEBAR NAVIGATION
    // ==========================================

    @FXML private void onOverviewNavClicked() { switchView(overviewView); }
    @FXML private void onTransactionsNavClicked() { switchView(transactionsView); }
    @FXML private void onManageRewardsNavClicked() { switchView(manageRewardsView); }

    private void switchView(VBox targetView) {
        if (targetView == null) return;

        if (overviewView != null) { overviewView.setVisible(false); overviewView.setManaged(false); }
        if (transactionsView != null) { transactionsView.setVisible(false); transactionsView.setManaged(false); }
        if (manageRewardsView != null) { manageRewardsView.setVisible(false); manageRewardsView.setManaged(false); }

        targetView.setVisible(true);
        targetView.setManaged(true);
    }

    @FXML
    private void onSignOutClicked() throws IOException {
        Stage stage = (Stage) totalStudentsLabel.getScene().getWindow();
        SceneSwitcher.switchScene(stage, "AdminLogin");
    }

    // ==========================================
    // DATA MODELS
    // ==========================================

    public static class StudentAdminModel {
        private final String studentId;
        private final String name;
        private final String department;
        private final int bottles;
        private final int points;

        public StudentAdminModel(String studentId, String name, String department, int bottles, int points) {
            this.studentId = studentId;
            this.name = name;
            this.department = department;
            this.bottles = bottles;
            this.points = points;
        }

        public String getStudentId() { return studentId; }
        public String getName() { return name; }
        public String getDepartment() { return department; }
        public int getBottles() { return bottles; }
        public int getPoints() { return points; }
    }

    public static class TransactionAdminModel {
        private final int transactionId;
        private final String studentId;
        private final String type;
        private final String date;

        public TransactionAdminModel(int transactionId, String studentId, String type, String date) {
            this.transactionId = transactionId;
            this.studentId = studentId;
            this.type = type;
            this.date = date;
        }

        public int getTransactionId() { return transactionId; }
        public String getStudentId() { return studentId; }
        public String getType() { return type; }
        public String getDate() { return date; }
    }

    public static class RewardAdminModel {
        private final int rewardId;
        private final String name;
        private final int cost;
        private final String type;

        public RewardAdminModel(int rewardId, String name, int cost, String type) {
            this.rewardId = rewardId;
            this.name = name;
            this.cost = cost;
            this.type = type;
        }

        public int getRewardId() { return rewardId; }
        public String getName() { return name; }
        public int getCost() { return cost; }
        public String getType() { return type; }
    }
}