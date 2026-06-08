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
    public static void registerUser(String firstName, String lastName, String studentId, int departmentId, String yearLevel, String hashedPassword){

    }
}
