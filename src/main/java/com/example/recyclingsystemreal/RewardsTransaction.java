package com.example.recyclingsystemreal;

public class RewardsTransaction extends Transaction {

    // Fields specific to RewardsTransaction
    private String rewardName;
    private int pointsCost;
    private int pointsLeft;

    // Default Constructor
    public RewardsTransaction() {
        super();
    }

    // Parameterized Constructor
    public RewardsTransaction(int transactionId, String studentId, String rewardName, int pointsCost, int pointsLeft, String createdAt) {
        super(transactionId, studentId, createdAt);
        this.rewardName = rewardName;
        this.pointsCost = pointsCost;
        this.pointsLeft = pointsLeft;
    }

    // --- Getters and Setters for subclass-specific fields ---

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public int getPointsCost() {
        return pointsCost;
    }

    public void setPointsCost(int pointsCost) {
        this.pointsCost = pointsCost;
    }

    public int getPointsLeft() {
        return pointsLeft;
    }

    public void setPointsLeft(int pointsLeft) {
        this.pointsLeft = pointsLeft;
    }


}