package com.krld.patient;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.krld.patient.game.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class MenuView extends FrameLayout implements ActiveView {

	private static final long BACKGROUND_DRAW_DELAY = 10;
	private Button mPlay;
	private Button mRatings;
	private Button mSettings;
	private BackgroundView mBackground;
	private Thread mDrawer;
	private SurfaceHolder mHolder;

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

		mPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showGameView();
			}


		});

		setupBackground();
	}

	private void setupBackground() {

	}

	private void createDrawerThread() {
		mDrawer = new Thread(new Runnable() {
			@Override
			public void run() {
				drawerLoop();
			}
		});
		mDrawer.start();
	}

	private void drawerLoop() {
		long currentTime;
		long lastTime = System.currentTimeMillis();
		float delta;
		long delay;
		try {
			while (true) {
				currentTime = System.currentTimeMillis();
				delta = (currentTime - lastTime) / 1000f;
				lastTime = currentTime;

				mBackground.update(delta);
				mBackground.postInvalidate();
				Thread.sleep(BACKGROUND_DRAW_DELAY);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void showGameView() {
		GameActivity activity = (GameActivity) getContext();
		activity.showGame();
	}

	@Override
	public void onPause() {
		mDrawer.interrupt();
		try {
			mDrawer.join();
			Log.i("", "mDrawer has terminated");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		createDrawerThread();
	}
}
