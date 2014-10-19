package com.krld.patient;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.krld.patient.game.GameView;

public class GameActivity extends Activity {

	private LinearLayout mLayout;
	private MenuView mMenuView;
	private GameView mGameView;

	private View mActiveView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		mGameView = new GameView(this);
		mMenuView = new MenuView(this);
		mLayout = (LinearLayout) findViewById(R.id.layout);
		show(mMenuView);
	}

	private void show(View view) {
		if (mActiveView != null) {
			((ActiveView) mActiveView).onPause();
		}
		mLayout.removeAllViews();
		mLayout.addView(view);
		mActiveView = view;
		((ActiveView) mActiveView).onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mActiveView != null)
			((ActiveView) mActiveView).onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mGameView != null)
			((ActiveView) mActiveView).onResume();
	}

	public void showGame() {
		show(mGameView);
	}

	@Override
	public void onBackPressed() {
		if (mActiveView instanceof GameView) {
			mGameView.onPause();
			show(mMenuView);
		} else {
			super.onBackPressed();
		}
	}
}
