package com.example.recyclingsystemreal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionRepo {
    static ObservableList<RewardsTransaction> rewardsTransactionRepo = FXCollections.observableArrayList();
    static ObservableList<BottleSubmissionTransaction> bottleSubmissionTransactionRepo = FXCollections.observableArrayList();
    public static void createTransactionRepo(String studentId) throws SQLException {
        rewardsTransactionRepo.clear();
        String sql = """
                SELECT transaction_id, 
                student_id, reward_name, point_cost, 
                points_left, created_at FROM transaction_vw WHERE student_id =?
                """;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    LocalDateTime timestamp = rs.getObject("created_at", LocalDateTime.class);
                    if(timestamp == null){
                        continue;
                    }
                    String formattedTimestamp = timestamp.format(formatter);
                    rewardsTransactionRepo.add(
                            new RewardsTransaction(
                                    rs.getInt("transaction_id"),
                                    rs.getString("student_id"),
                                    rs.getString("reward_name"),
                                    rs.getInt("point_cost"),
                                    rs.getInt("points_left"),
                                    formattedTimestamp
                            )
                    );
                }
            }
        }
    }

    public static ObservableList<RewardsTransaction> getTransactionRepo(){
        return rewardsTransactionRepo;
    }

    public static void createBottleSubmissionTransactionRepo(String studentId) throws SQLException{
        String sql = """
                SELECT transaction_id, student_id, bottles_submitted, total_bottles, 
                points_gained, 
                total_points, created_at FROM bottle_transactions_vw WHERE student_id = ?
                """;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    LocalDateTime timestamp = rs.getObject("created_at", LocalDateTime.class);
                    if(timestamp == null){
                        continue;
                    }
                    String formattedTimestamp = timestamp.format(formatter);
                    bottleSubmissionTransactionRepo.add(
                            new BottleSubmissionTransaction(
                                    rs.getInt("transaction_id"),
                                    rs.getString("student_id"),
                                    formattedTimestamp,
                                    rs.getInt("bottles_submitted"),
                                    rs.getInt("total_bottles"),
                                    rs.getInt("points_gained"),
                                    rs.getInt("total_points")
                            )
                    );
                }
            }
        }
    }

    public static ObservableList<BottleSubmissionTransaction> getBottleSubmissionTransactionRepo(){
        return bottleSubmissionTransactionRepo;
    }

    public static void createRewardTransaction(String studentId, int rewardId, int pointsLeft) throws SQLException{
        String sql = """
                INSERT INTO transactions (student_id, reward_id, points_left) VALUES (?, ?, ?)
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, studentId);
            pstmt.setInt(2, rewardId);
            pstmt.setInt(3, pointsLeft);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0){
                System.out.println("Transaction entry inserted successfully!");
            }
        }
    }

    public static void createBottleSubmissionTransaction(String studentId, int bottlesSubmitted,
                                                         int totalBottles, int pointsGained,
                                                         int totalPoints){
        String sql = """
                INSERT INTO bottle_transactions 
                (student_id, bottles_submitted, total_bottles, points_gained, total_points) VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, studentId);
            pstmt.setInt(2, bottlesSubmitted);
            pstmt.setInt(3, totalBottles);
            pstmt.setInt(4, pointsGained);
            pstmt.setInt(5, totalPoints);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0){
                System.out.println("Bottle Submission Transaction inserted successfully!");
            }
        } catch (Exception e){
            System.out.println("Error inserting bottle submission: " + e.getMessage());
        }

    }

}
