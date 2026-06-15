package com.example.recyclingsystemreal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RewardsRepo {
    private static ArrayList<Rewards> rewardsRepo = new ArrayList<>();

    public static void createRewardsRepo() throws SQLException {
        String sql = """
            SELECT reward_id, reward_name, point_cost, reward_type, description FROM rewards;
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                rewardsRepo.add(
                        new Rewards(
                                rs.getInt("reward_id"),
                                rs.getString("reward_name"),
                                rs.getInt("point_cost"),
                                rs.getString("reward_type"),
                                rs.getString("description")
                        )
                );
            }
        }
    }
    static ArrayList<Rewards> getRewardsRepo(){
        return rewardsRepo;
    }

    static int getRewardId(String rewardName){
        for(Rewards reward : getRewardsRepo()){
            if(reward.getRewardName().equals(rewardName)){
                return reward.getRewardId();
            }
        }
        return -1;
    }
}
