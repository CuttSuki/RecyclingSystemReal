package com.example.recyclingsystemreal;

public class Rewards {

    // Private fields mapping to the database columns
    private int rewardId;
    private String rewardName;
    private int pointCost;
    private String rewardType;
    private String description = "Description"; // Default value from schema

    // Default No-Arg Constructor
    public Rewards() {
    }

    // Constructor without description (uses the default 'Description')
    public Rewards(int rewardId, String rewardName, int pointCost, String rewardType) {
        this.rewardId = rewardId;
        this.rewardName = rewardName;
        this.pointCost = pointCost;
        this.rewardType = rewardType;
    }

    // Full Constructor
    public Rewards(int rewardId, String rewardName, int pointCost, String rewardType, String description) {
        this.rewardId = rewardId;
        this.rewardName = rewardName;
        this.pointCost = pointCost;
        this.rewardType = rewardType;
        this.description = description;
    }

    // ==========================================
    // Getters and Setters
    // ==========================================

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public int getPointCost() {
        return pointCost;
    }

    public void setPointCost(int pointCost) {
        this.pointCost = pointCost;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}