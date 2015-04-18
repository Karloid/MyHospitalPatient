package com.krld.patient.game.model.effects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Unit;

public class ShieldEffect extends Effect {


	public ShieldEffect(Unit owner) {
		super(owner);
		durationTime = 3f;
	}

	public void draw(Canvas canvas, Paint paint, GameCamera camera) {
		paint.setColor(Color.rgb(0, 255, 255));
		paint.setAlpha(60);
		canvas.drawCircle(owner.x - camera.getX(), owner.y + yCorrection - camera.getY(), 55, paint);
		paint.setAlpha(255);
	}
}
