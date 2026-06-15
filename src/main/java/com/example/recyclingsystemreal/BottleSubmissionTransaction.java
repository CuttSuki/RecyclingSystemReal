package com.example.recyclingsystemreal;

public class BottleSubmissionTransaction extends Transaction {
    private int bottlesSubmitted;
    private int totalBottles;
    private int pointsGained;
    private int totalPoints;

    // Default Constructor
    public BottleSubmissionTransaction() {
        super();
    }

    // Parameterized Constructor
    public BottleSubmissionTransaction(int transactionId, String studentId, String createdAt,
                                       int bottlesSubmitted, int totalBottles,
                                       int pointsGained, int totalPoints) {
        super(transactionId, studentId, createdAt);
        this.bottlesSubmitted = bottlesSubmitted;
        this.totalBottles = totalBottles;
        this.pointsGained = pointsGained;
        this.totalPoints = totalPoints;
    }

    // --- Getters and Setters ---

    public int getPointsGained() {
        return pointsGained;
    }

    public void setPointsGained(int pointsGained) {
        this.pointsGained = pointsGained;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getBottlesSubmitted(){
        return bottlesSubmitted;
    }

    public int getTotalBottles(){
        return totalBottles;
    }

}