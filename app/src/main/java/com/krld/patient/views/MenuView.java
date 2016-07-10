package com.krld.patient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.krld.patient.GameActivity;
import com.krld.patient.R;

public class MenuView extends FrameLayout implements ActiveView {

    private Button mPlay;
    private Button mRatings;
    private Button mSettings;
    private BackgroundView mBackground;

    public MenuView(Context gameActivity) {
        super(gameActivity);
        init();
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.general_menu, this);

        mBackground = (BackgroundView) findViewById(R.id.generalMenu_background);
        mPlay = (Button) findViewById(R.id.generalMenu_play);
        mRatings = (Button) findViewById(R.id.generalMenu_ratings);
        mSettings = (Button) findViewById(R.id.generalMenu_settings);

        mPlay.setOnClickListener(v -> showGameView());
        mRatings.setOnClickListener(v -> showRatings());

    }

    private void showRatings() {
        GameActivity activity = (GameActivity) getContext();
        activity.showRatings();
    }

    private void showGameView() {
        GameActivity activity = (GameActivity) getContext();
        activity.showGame();
    }

    @Override
    public void onPause() {
       mBackground.onPause();
    }

    @Override
    public void onResume() {
        mBackground.onResume();
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onShow() {

    }
}
