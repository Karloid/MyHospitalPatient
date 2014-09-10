package com.krld.patient;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.krld.patient.game.GameView;

public class GameActivity extends Activity {
    private GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        gameView = new GameView(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.addView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null)
            gameView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null)
            gameView.onResume();
    }
}
