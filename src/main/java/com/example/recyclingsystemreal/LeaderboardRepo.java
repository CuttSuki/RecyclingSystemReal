package com.example.recyclingsystemreal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaderboardRepo {
    private static ObservableList<LeaderboardStudent> leaderboardStudents = FXCollections.observableArrayList();

    public static void createLeaderboardRepo() throws SQLException {
        leaderboardStudents.clear();
        String sql = """
                 SELECT student_id, first_name, last_name, department_name, 
                        year_level_name, total_bottles, points_balance, 
                        rewards_redeemed 
                 FROM vw_students_stats ORDER BY total_bottles DESC LIMIT 100;
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                LeaderboardStudent leaderboardStudent = new LeaderboardStudent(
                        rs.getString("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("department_name"),
                        rs.getString("year_level_name"),
                        rs.getInt("total_bottles"),
                        rs.getInt("points_balance"),
                        rs.getInt("rewards_redeemed")
                );
                leaderboardStudents.add(leaderboardStudent);
            }
            //Set their ranks
            for (LeaderboardStudent leaderboardStudent : leaderboardStudents){
                leaderboardStudent.setRank(LeaderboardManager.checkRanking(leaderboardStudent.getStudentId()));
            }
        }
    }

    public static ObservableList<LeaderboardStudent> getLeaderboardStudents(){ return leaderboardStudents; }
}
