package com.example.recyclingsystemreal;

public class UserData {
    private static StudentUser studentUser = null;
    private static UserRewards userRewards = null;
    public static void setStudentUser(StudentUser setStudentUser){
        studentUser = setStudentUser;
    }
    public static StudentUser getStudentUser(){
        return studentUser;
    }
}
