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
    public static void registerUser(String firstName, String lastName, String studentId, int departmentId, int yearLevelId, String hashedPassword) throws SQLException {
        // Separate your queries
        String insertStudentSql = "INSERT INTO STUDENTS (student_id, first_name, last_name, password, department_id, year_level_id) VALUES (?, ?, ?, ?, ?, ?)";
        String insertStatsSql = "INSERT INTO student_stats (student_id, points_balance, rewards_redeemed) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection()) {
            // 1. Disable auto-commit to start a transaction
            conn.setAutoCommit(false);

            // 2. Prepare both statements
            try (PreparedStatement pstmt1 = conn.prepareStatement(insertStudentSql);
                 PreparedStatement pstmt2 = conn.prepareStatement(insertStatsSql)) {

                // Execute Query 1: Insert into STUDENTS
                pstmt1.setString(1, studentId);
                pstmt1.setString(2, firstName);
                pstmt1.setString(3, lastName);
                pstmt1.setString(4, hashedPassword);
                pstmt1.setInt(5, departmentId);
                pstmt1.setInt(6, yearLevelId);
                int rows1 = pstmt1.executeUpdate();

                // Execute Query 2: Insert into student_stats
                pstmt2.setString(1, studentId);
                pstmt2.setInt(2, 0); // Assuming initial points_balance is 0
                pstmt2.setInt(3, 0); // Assuming initial rewards_redeemed is 0
                int rows2 = pstmt2.executeUpdate();

                // 3. Commit the transaction (Saves to database)
                conn.commit();
                System.out.println("Successfully registered user. Added " + (rows1 + rows2) + " rows.");

            } catch (SQLException e) {
                // 4. Rollback if anything goes wrong
                conn.rollback();
                System.err.println("Transaction failed. Rolling back changes.");
                throw e; // Rethrow to notify the calling method

            } finally {
                // 5. Reset auto-commit to its default state
                conn.setAutoCommit(true);
            }
        }
    }
}
