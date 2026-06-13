package com.example.recyclingsystemreal;

public class UserData {
    private static StudentUser studentUser = null;
    private static UserStats userStats = null;
    public static void setStudentUser(StudentUser setStudentUser){
        studentUser = setStudentUser;
    }

    public static void setUserStats(UserStats setUserStats){
        userStats = setUserStats;
    }
    public static StudentUser getStudentUser(){
        return studentUser;
    }
    public static UserStats getUserStats(){
        return userStats;
    }

}
