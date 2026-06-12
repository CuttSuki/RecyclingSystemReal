package com.example.recyclingsystemreal;

public class StudentUser {
    private String studentId;
    private String firstName;
    private String lastName;
    private String department;
    private String yearLevel;

    StudentUser(String studentId, String firstName, String lastName, String department, String yearLevel){
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.yearLevel = yearLevel;
    }
    public String getStudentId(){
        return studentId;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getDepartment(){
        return department;
    }
    public String getYearLevel(){
        return yearLevel;
    }
}
