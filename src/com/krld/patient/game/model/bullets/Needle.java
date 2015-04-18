package com.krld.patient.game.model.bullets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Unit;
import com.krld.patient.game.model.animations.BloodAnimation;

public class Needle extends Bullet {

	public static final int SCALE_FACTOR = 2;
	public static final int SPEED = 240;

	public long getBirthDate() {
		return birthDate;
	}


	public static Bitmap sprite;

	long birthDate;
	public static long lifeTime = 10000;

	private static final float TOUCH_RANGE = 30;

	private static float touchDmg = 10;

	Needle(float x, float y, GameView context) {
		super(x, y, context);
		speed = SPEED;
	}

	public Needle(float x, float y
			, GameView context, Unit target) {
		this(x, y, context, target.x, target.y);
	}

	Needle(float x, float y
			, GameView context, float targetX, float targetY) {
		super(x, y, context);
		speed = SPEED;
		size = 10;
		moveX = targetX;
		moveY = targetY;
		birthDate = System.currentTimeMillis();
	}

	public boolean touchPlayer() {
		if (Math.abs(x - context.player.x) < TOUCH_RANGE &&
				Math.abs(y - context.player.y) < TOUCH_RANGE) {
			context.player.damage(touchDmg);
			context.animations.add(new BloodAnimation(x, y, context));
			return true;
		}
		return false;
	}


	public void draw(Canvas canvas, Paint paint, GameCamera camera) {
		Utils.drawBitmapRotate(sprite, x - camera.getX(), y - camera.getY(), Utils.getAngle(deltaX, deltaY) - 90, canvas, paint);
	}

	@Override
	public Bitmap getBitmap() {
		return sprite;
	}

	public static void init(Resources resources) {
		sprite = Utils.loadSprite(R.raw.needle, resources, SCALE_FACTOR);
	}
}
