package com.krld.patient;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.krld.patient.game.GameView;

public class GameActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        final GameView gameView = new GameView(this);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.addView(gameView);
    }
}
