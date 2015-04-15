package com.krld.patient.game.model.effects;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Unit;

public abstract class Effect {
	long birthDateTick;   //TODO remove
	long birthDateTime;
	protected long durationTick;    //TODO remove
	protected long durationTime;
	protected Unit owner;

	protected static float yCorrection = 0;

	protected Effect(Unit owner) {
		this.owner = owner;
		birthDateTick = owner.context.getTick();
		birthDateTime = System.currentTimeMillis();
		durationTick = 25;   //TODO rework
	}

	public void postEffect() {
	}

	public void doEffect(float delta) {
	}

	public boolean checkEffectTime() {
		if (owner.context.getTick() - birthDateTick > durationTick) {    //TODO rework
			return false;
		} else return true;
	}

	public abstract void draw(Canvas canvas, Paint paint, GameCamera camera);
}
