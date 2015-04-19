package com.krld.patient.game.model.effects;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Point;
import com.krld.patient.game.model.Unit;

public abstract class Effect {
	protected long birthDateTimeMs;
	protected float durationTime;
	protected float currentLifeTime;
	protected Unit owner;

	protected Point position;

	protected Effect(Unit owner) {
		this.owner = owner;
		birthDateTimeMs = System.currentTimeMillis();
	}

	public void postEffect() {
	}

	public void doEffect(float delta) {
		currentLifeTime += delta;
	}

	public boolean isEndEffectTime() {
		return currentLifeTime <= durationTime;
	}

	public abstract void draw(Canvas canvas, Paint paint, GameCamera camera);
}
