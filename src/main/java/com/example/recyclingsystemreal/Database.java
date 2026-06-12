package com.example.recyclingsystemreal;

import java.sql.*;

public class Database {
    private static final String URL ="jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password123";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void testDatabaseConnection() throws SQLException{
        String query = "SELECT VERSION() as version";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()){
            if (rs.next()){
                System.out.println(rs.getString("version"));
            }
        }
    }
    public static void registerUser(String firstName, String lastName, String studentId, int departmentId, int yearLevelId, String hashedPassword) throws SQLException{
        String sql = """
                INSERT INTO STUDENTS (student_id, first_name, last_name, password, department_id, year_level_id) VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, studentId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, hashedPassword);
            pstmt.setInt(5, departmentId);
            pstmt.setInt(6, yearLevelId);
            int rows = pstmt.executeUpdate();
            System.out.println("Added " + rows + " rows.");
        }
    }

}
