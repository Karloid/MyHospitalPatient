package com.krld.patient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.krld.patient.game.GameView;
import com.krld.patient.views.ActiveView;
import com.krld.patient.views.MenuView;
import com.krld.patient.views.RatingView;

public class GameActivity extends Activity {

    private FrameLayout mLayout;
    private MenuView mMenuView;
    private GameView mGameView;

    private ActiveView mActiveView;
    private ActiveView mRatingsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        mMenuView = new MenuView(this);
        mLayout = (FrameLayout) findViewById(R.id.layout);
        show(mMenuView);
    }

    private void show(ActiveView view) {
        if (mActiveView != null) {
            ActiveView previousView = mActiveView;
            previousView.onPause();

            Application.handler.postDelayed(() -> mLayout.removeView(previousView.getView()), 400);
        }
        mLayout.addView(view.getView());
        mActiveView = view;
        mActiveView.onShow();
        mActiveView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mActiveView != null)
            (mActiveView).onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mActiveView != null)
            mActiveView.onResume();
    }

    public void showGame() {
        show(getGameView());
    }

    private GameView getGameView() {
        if (mGameView == null) {
            mGameView = new GameView(this);
        }
        return mGameView;
    }

    @Override
    public void onBackPressed() {
        if (!(mActiveView instanceof MenuView)) {
            show(mMenuView);
        } else {
            super.onBackPressed();
        }
    }

    public void showRatings() {
        show(getRatingsView());
    }

    private ActiveView getRatingsView() {
        if (mRatingsView == null) {
            mRatingsView = new RatingView(this);
        }
        return mRatingsView;
    }
}
