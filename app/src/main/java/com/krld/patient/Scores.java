package com.krld.patient;

import android.appwidget.AppWidgetProvider;
import android.util.Log;

import java.util.Collections;
import java.util.List;

public class Scores {
    private static final int MAX_SIZE = 5;
    List<PlayerScore> allScores;

    public void addScore(PlayerScore playerScore) {
        allScores.add(playerScore);
        Log.d(Application.TAG, "add score " + playerScore);
        Collections.sort(allScores, (lhs, rhs) -> rhs.score - lhs.score);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean isNewRecord(int score) {
        if (score == 0) return false;
        if (allScores.size() < MAX_SIZE) return true;

        return allScores.get(MAX_SIZE - 1).score <= score;
    }

    public void saveScore(String playerName, int score) {
        PlayerScore playerScore = new PlayerScore(playerName, score);
        addScore(playerScore);
        Application.saveLastPlayerName(playerName);
        Application.saveScores(this);

    }
}
