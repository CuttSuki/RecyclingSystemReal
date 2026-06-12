package com.example.recyclingsystemreal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class YearLevelRepository {
    static ArrayList <YearLevel> yearLevelRepo = new ArrayList<>();
    static void createYearLevelRepo() throws SQLException {
        String sql = """
                SELECT year_level_id, year_level_name FROM YEAR_LEVELS;
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                yearLevelRepo.add(new YearLevel(rs.getInt("year_level_id"), rs.getString("year_level_name")));
            }
        }
    }
    static int getYearLevelId(String yearLevelName){
        for (YearLevel yearLevel : yearLevelRepo){
            if (yearLevel.getYearLevel().equals(yearLevelName)){
                return yearLevel.getYearLevelId();
            }
        }
        return -1;
    }
}
