package com.krld.patient.game.model.creeps;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.bullets.Note;

public class Doctor extends Nurse {
	public static Bitmap sprite;

	private static final int REWARD = 500;

	private double launchNeedleChance = 0.05d;

	public Doctor(float x, float y, GameView context) {
		super(x, y, context);
	}

	void launchNeedle() {
		context.bullets.add(new Note(x, y, context, context.player));
	}

	public static void init(Resources resources) {
		sprite = Utils.loadSprite(R.raw.doctor, resources);
	}

	public void draw(Canvas canvas, Paint paint, GameCamera camera) {
		canvas.drawBitmap(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);
	}

	public int getReward() {
		return REWARD;
	}

	public double getLaunchChance() {
		return launchNeedleChance;
	}

	@Override
	public Bitmap getBitmap() {
		return sprite;
	}
}
