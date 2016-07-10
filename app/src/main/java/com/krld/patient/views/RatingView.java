package com.krld.patient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krld.patient.Application;
import com.krld.patient.GameActivity;
import com.krld.patient.R;
import com.krld.patient.models.PlayerScore;
import com.krld.patient.models.Scores;

public class RatingView extends FrameLayout implements ActiveView {

    private BackgroundView mBackground;
    private LinearLayout mContainer;

    public RatingView(Context gameActivity) {
        super(gameActivity);
        init();
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.rating_v, this);

        mBackground = (BackgroundView) findViewById(R.id.generalMenu_background);
        mBackground.colorFade = 0xffE6A46E;
        mBackground.colorEnd = 0xff225378;
        mBackground.colorLevel2 = 0xff1695A3;
        mBackground.colorLevel3 = 0xffACF0F2;

        mContainer = (LinearLayout) findViewById(R.id.rating_container);
    }

    private void refreshData() {
        mContainer.removeAllViews();
        Scores scores = Application.getAllScores();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (scores.allScores != null && !scores.allScores.isEmpty()) {
            for (PlayerScore score : scores.allScores) {
                View row = inflater.inflate(R.layout.rating_li, mContainer, false);
                ((TextView) row.findViewById(R.id.rating_username)).setText(score.playerName);
                ((TextView) row.findViewById(R.id.rating_score)).setText(score.score + "");
                mContainer.addView(row);
            }
        } else {
            View empty = inflater.inflate(R.layout.rating_li_empty, mContainer, true);
        }
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
        refreshData();
    }
}
