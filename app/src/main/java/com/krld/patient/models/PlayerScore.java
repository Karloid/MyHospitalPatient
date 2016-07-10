package com.krld.patient.models;

public class PlayerScore {
    public String playerName;
    public int score;
    public String appVersion;

    public PlayerScore() {
    }

    public PlayerScore(String playerName, int score, String appVersion) {
        this.playerName = playerName;
        this.score = score;
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        return "PlayerScore{" +
                "playerName='" + playerName + '\'' +
                ", score=" + score +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }
}
