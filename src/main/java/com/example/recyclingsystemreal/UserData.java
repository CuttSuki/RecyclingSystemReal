package com.example.recyclingsystemreal;

public class UserData {
    private static StudentUser studentUser = null;
    private static UserRewards userRewards = null;
    public static void setStudentUser(StudentUser setStudentUser){
        studentUser = setStudentUser;
    }

    public static void setUserRewards(UserRewards setUserRewards){
        userRewards = setUserRewards;
    }
    public static StudentUser getStudentUser(){
        return studentUser;
    }
    public static UserRewards getUserRewards(){
        return userRewards;
    }

}
