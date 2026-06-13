package com.example.recyclingsystemreal;

public class LeaderboardStudent {
    private String studentId;
    private String firstName;
    private String lastName;
    private String departmentName;
    private String yearLevelName;
    private int totalBottles;
    private int pointsBalance;
    private int rewardsRedeemed;

    LeaderboardStudent(
            String studentId, String firstName, String lastName,
            String departmentName, String yearLevelName, int totalBottles,
            int pointsBalance, int rewardsRedeemed
    ) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentName = departmentName;
        this.yearLevelName = yearLevelName;
        this.totalBottles = totalBottles;
        this.pointsBalance = pointsBalance;
        this.rewardsRedeemed = rewardsRedeemed;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDepartmentName(){
        return departmentName;
    }

    public String getYearLevelName(){
        return yearLevelName;
    }

    public int getTotalBottles(){
        return totalBottles;
    }

    public int getPointsBalance(){
        return pointsBalance;
    }

    public int getRewardsRedeemed() {
        return rewardsRedeemed;
    }
}
