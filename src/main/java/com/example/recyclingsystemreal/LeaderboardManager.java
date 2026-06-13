package com.example.recyclingsystemreal;

import javafx.collections.ObservableList;

public class LeaderboardManager {
    public static String checkRanking(String studentId){
        ObservableList<LeaderboardStudent> leaderboardStudents = LeaderboardRepo.getLeaderboardStudents();
        for (int i = 0; i < leaderboardStudents.size(); ++ i){
            if (leaderboardStudents.get(i).getStudentId().equals(studentId)){
                return Integer.toString(i + 1);
            }
        }
        return "Not Ranked";
    }
}
