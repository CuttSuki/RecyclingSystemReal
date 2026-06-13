package com.example.recyclingsystemreal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataSaver {
    public static void saveStudentStats() throws SQLException {
        String sql = """
                 UPDATE student_stats SET points_balance = ?, rewards_redeemed = ?, total_bottles = ?
                 WHERE student_id = ?
                """;
        if (UserData.getUserStats() == null || UserData.getStudentUser() == null){
            return;
        }
        int pointsBalance = UserData.getUserStats().getPointsBalance();
        int rewardsRedeemed = UserData.getUserStats().getRewardsRedeemed();
        int totalBottles = UserData.getUserStats().getTotalBottles();
        String studentId = UserData.getStudentUser().getStudentId();
        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, pointsBalance);
            pstmt.setInt(2, rewardsRedeemed);
            pstmt.setInt(3, totalBottles);
            pstmt.setString(4, studentId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Student stats saved, Rows Affected: " + rowsAffected);
        }
    }
}
