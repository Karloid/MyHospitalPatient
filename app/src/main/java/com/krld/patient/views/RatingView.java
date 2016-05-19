package com.krld.patient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.krld.patient.GameActivity;
import com.krld.patient.R;

public class RatingView extends FrameLayout implements ActiveView {

    private BackgroundView mBackground;

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
}
