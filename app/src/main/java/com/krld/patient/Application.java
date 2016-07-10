package com.krld.patient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.krld.patient.models.Scores;

import java.util.ArrayList;

public class Application extends android.app.Application {
    public static final String SP_NAME = "SP_NAME";
    public static final String TAG = "PLOG";
    private static Application instance;
    private static String KEY_LAST_PLAYER_NAME = "key last player name";
    private static String KEY_ALL_SCORES = "key all scores";
    public static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        handler = new Handler(Looper.getMainLooper());
    }

    public static String getLastPlayerName() {
        SharedPreferences sp = getSharedPrefs();
        String name = sp.getString(KEY_LAST_PLAYER_NAME, "");
        if (TextUtils.isEmpty(name)) {
            name = "Player";
        }
        return name;
    }

    private static SharedPreferences getSharedPrefs() {
        return instance.getSharedPreferences(SP_NAME, MODE_PRIVATE);
    }

    public static Scores getAllScores() {
        SharedPreferences sharedPrefs = getSharedPrefs();
        String allScores = sharedPrefs.getString(KEY_ALL_SCORES, "");
        Scores score;
        if (TextUtils.isEmpty(allScores)) {
            score = new Scores();
            score.allScores = new ArrayList<>();
        } else {
            score = new Gson().fromJson(allScores, Scores.class);
        }
        return score;
    }

    public static void saveScores(Scores scores) {
        SharedPreferences.Editor edit = getSharedPrefs().edit();
        edit.putString(KEY_ALL_SCORES, new Gson().toJson(scores));
        edit.apply();
    }

    public static void saveLastPlayerName(String playerName) {
        SharedPreferences.Editor edit = getSharedPrefs().edit();
        edit.putString(KEY_LAST_PLAYER_NAME, playerName);
        edit.apply();
    }

    public static Context getInstance() {
        return instance;
    }
}
