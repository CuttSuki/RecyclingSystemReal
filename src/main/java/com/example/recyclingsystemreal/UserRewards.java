package com.example.recyclingsystemreal;

public class UserRewards {
    private int totalBottles;
    private int pointsBalance;
    private int rewardsRedeemed;

    UserRewards(int totalBottles, int pointsBalance, int rewardsRedeemed){
        this.totalBottles = totalBottles;
        this.pointsBalance = pointsBalance;
        this.rewardsRedeemed = rewardsRedeemed;
    }

    public void setTotalBottles(int bottleCount){
        totalBottles = bottleCount;
    }
    public void setPointsBalance(int pointCount){
        pointsBalance = pointCount;
    }

    public void setRewardsRedeemed(int rewardCount){
        rewardsRedeemed = rewardCount;
    }

    public int getTotalBottles(){
        return totalBottles;
    }

    public int getPointsBalance(){
        return pointsBalance;
    }

    public int getRewardsRedeemed(){
        return rewardsRedeemed;
    }
}
