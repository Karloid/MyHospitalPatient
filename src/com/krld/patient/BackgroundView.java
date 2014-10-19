package com.krld.patient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.krld.patient.game.model.animations.Animation;
import com.krld.patient.game.model.animations.CloudAnimation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BackgroundView extends View implements View.OnTouchListener {
	public static final float CLOUD_RECREATE_RATIO = 0.4f;
	private int mEndColor;
	private List<Level> mLevels;
	private Collection<Animation> animations;

	public BackgroundView(Context context) {
		super(context);
		init();
	}

	public BackgroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BackgroundView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mEndColor = getResources().getColor(R.color.background_end);
		animations = new ArrayList<Animation>();
		CloudAnimation.init(getResources());
		setOnTouchListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		if (mLevels == null) {
			initLevels();
		}
		drawEnd(canvas, paint);
		drawLevels(canvas, paint);
		drawAnimation(canvas, paint);
		removeAnim();
	}

	private void removeAnim() {
		List<Animation> deadAnimations = new ArrayList<Animation>();
		for (Animation animation : animations) {
			if (!animation.checkAlive()) {
				deadAnimations.add(animation);
			}
		}
		for (Animation deadAnim : deadAnimations) {
			if (Math.random() < CLOUD_RECREATE_RATIO) {
				animations.add(new CloudAnimation(deadAnim.x, deadAnim.y, null)
				);
			}
		}
		animations.removeAll(deadAnimations);

	}

	private void drawAnimation(Canvas canvas, Paint paint) {
		for (Animation anim : animations) {
			anim.draw(canvas, paint);
		}
	}

	private void initLevels() {
		mLevels = new ArrayList<Level>();
		mLevels.add(new Level(getResources().getColor(R.color.level_1), 20, 20));
		mLevels.add(new Level(getResources().getColor(R.color.level_2), 5, 50));
		mLevels.add(new Level(getResources().getColor(R.color.level_3), 2, 100));
	}

	private void drawLevels(Canvas canvas, Paint paint) {
		for (Level level : mLevels) {
			level.draw(canvas, paint);
		}
	}

	private void drawEnd(Canvas canvas, Paint paint) {
		paint.setColor(mEndColor);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
	}

	public void update() {
		if (mLevels != null)
			for (Level level : mLevels) {
				level.update();
			}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int disp = 100;
		int dispDiv2 = disp / 2;
		for (int i = 0; i < 3; i++) {
			animations.add(new CloudAnimation(event.getX() + (float) Math.random() * disp - dispDiv2,
					event.getY() + (float) Math.random() * disp - dispDiv2, null));
		}
		return true;
	}

	private class Level {
		private final int mColor;
		private final int mCount;
		private final int mHeight;
		private List<Rectangle> mRectangles;

		public Level(int color, int count, int width) {
			mColor = color;
			mCount = count;
			mHeight = width;

			createRectangles();
		}

		private void createRectangles() {
			mRectangles = new ArrayList<Rectangle>();
			int space = (int) (getHeight() / ((mCount + 1) * 1f));
			for (int i = 0; i <= mCount; i++) {
				mRectangles.add(new Rectangle(i * space - mHeight));
			}
		}

		public void draw(Canvas canvas, Paint paint) {
			paint.setColor(Color.BLACK);
			paint.setAlpha(100);
			for (Rectangle rect : mRectangles) {
				canvas.drawRect(0, rect.mY + mHeight / 4, canvas.getWidth(), rect.mY + mHeight + mHeight / 4, paint);
			}
			paint.setAlpha(255);
			paint.setColor(mColor);
			for (Rectangle rect : mRectangles) {
				canvas.drawRect(0, rect.mY, canvas.getWidth(), rect.mY + mHeight, paint);
			}
		}

		public void update() {
			for (Rectangle rect : mRectangles) {
				rect.mY += getSpeed();
				if (rect.mY > getHeight()) {
					rect.mY = -mHeight;
				}
			}
		}

		private int getSpeed() {
			return mHeight / 20;
		}
	}

	private class Rectangle {
		public int mY;

		public Rectangle(int y) {
			mY = y;
		}
	}
}
