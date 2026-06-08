package com.example.recyclingsystemreal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DepartmentRepository {
    static ArrayList<Department> departmentRepo = new ArrayList<Department>();
    public static void createDepartmentRepo() throws SQLException {
        if (!departmentRepo.isEmpty()){
            return;
        }
        String sql = """
                SELECT department_id, department_name FROM DEPARTMENTS;
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                //System.out.println("Data exists!");
                departmentRepo.add(new Department(rs.getInt("department_id"), rs.getString("department_name")));
            }
        }
    }

    static int getDepartmentId(String departmentName){
        for (Department department : departmentRepo){
            if (department.getDepartmentName().equals(departmentName)){
                return department.getDepartmentId();
            }
        }
        return -1;
    }
}
