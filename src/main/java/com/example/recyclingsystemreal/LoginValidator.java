package com.example.recyclingsystemreal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginValidator {
    public static boolean validateLogin(String studentId, String password) throws SQLException {
        String sql = """
                 SELECT password FROM vw_students_admin WHERE student_id = ?;
                """;
        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, studentId);
            try(ResultSet rs = pstmt.executeQuery()){
                if(!rs.next()){
                    return false;
                }
                String hashedPassword = rs.getString("password");
                if(!PasswordHasher.checkPassword(password, hashedPassword)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates administrator credentials against the admin_table.
     */
    public static boolean validateAdminLogin(String username, String password) throws SQLException {
        String sql = """
                 SELECT admin_password FROM admin_table WHERE admin_user = ?;
                """;
        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, username);
            try(ResultSet rs = pstmt.executeQuery()){
                if(!rs.next()){
                    return false;
                }
                String hashedPassword = rs.getString("admin_password");
                if(!PasswordHasher.checkPassword(password, hashedPassword)){
                    return false;
                }
            }
        }
        return true;
    }
}
