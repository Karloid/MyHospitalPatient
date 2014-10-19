package com.krld.patient;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MenuView extends FrameLayout implements ActiveView {

	private Button mPlay;
	private Button mRatings;
	private Button mSettings;

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

		mPlay = (Button) findViewById(R.id.generalMenu_play);
		mRatings = (Button) findViewById(R.id.generalMenu_ratings);
		mSettings = (Button) findViewById(R.id.generalMenu_settings);

		mPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showGameView();
			}


		});
	}

	private void showGameView() {
		GameActivity activity = (GameActivity) getContext();
		activity.showGame();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}
}
