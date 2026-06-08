package com.example.recyclingsystemreal;

public class YearLevel {
    private int yearLevelId;
    private String yearLevel;

    public YearLevel(int yearLevelId, String yearLevel){
        this.yearLevelId = yearLevelId;
        this.yearLevel = yearLevel;
    }

    public int getYearLevelId(){
        return yearLevelId;
    }
    public String getYearLevel(){
        return yearLevel;
    }
}
