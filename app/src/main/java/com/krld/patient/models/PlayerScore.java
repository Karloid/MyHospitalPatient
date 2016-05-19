package com.krld.patient.models;

public class PlayerScore {
    public String playerName;
    public int score;

    public PlayerScore() {
    }
    public PlayerScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    @Override
    public String toString() {
        return "PlayerScore{" +
                "playerName='" + playerName + '\'' +
                ", score=" + score +
                '}';
    }
}
