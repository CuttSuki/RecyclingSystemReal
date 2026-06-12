package com.example.recyclingsystemreal;

import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataCreator {
    public static StudentUser createStudentUserData(String studentId) throws SQLException {
        String sql = """
                    SELECT student_id, first_name, last_name, department, year_level FROM vw_students WHERE student_id = ?
                """;
        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, studentId);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return new StudentUser(rs.getString("student_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("department"),
                            rs.getString("year_level")
                    );
                }
            }
        }
        return null;
    }

    public static UserRewards createUserRewards(String studentId) throws SQLException{
        String sql = """
                SELECT total_bottles, points_balance, rewards_redeemed FROM vw_students_stats WHERE student_id = ? LIMIT 1
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, studentId);
            try(ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    return new UserRewards(
                            rs.getInt("total_bottles"),
                            rs.getInt("points_balance"),
                            rs.getInt("rewards_redeemed")
                    );
                }
            }
        }
        return null;
    }
}
