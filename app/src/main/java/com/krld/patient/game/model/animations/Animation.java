package com.krld.patient.game.model.animations;

import android.graphics.Bitmap;
import com.krld.patient.game.GameView;
import com.krld.patient.game.model.Unit;

import java.util.List;

public abstract class Animation extends Unit {
	private final float lifeTimeMax;
	private float lifeTime;

	long birthDateMs;
	private byte frameIndex;
	private boolean stop;

	Animation(float x, float y, GameView context) {
		super(x, y, context);
		birthDateMs = System.currentTimeMillis();
		lifeTimeMax = 0.5f;
		frameIndex = 0;
		stop = false;
	}

	@Override
	public void move(float delta) {
		lifeTime += delta;
		if (lifeTime >= lifeTimeMax) {
			stop = true;
		} else {
			frameIndex = (byte) ((lifeTime / lifeTimeMax) * getSprites().size());
		}
		super.move(delta);
	}

	public Bitmap getFrame() {
		if (stop) throw new RuntimeException("Are u mad?");
		return getSprites().get(frameIndex);
	}

	public boolean checkAlive() {
		return !stop;
	}

	@Override    //TODO extract
	public boolean needRemove() {
		return stop;
	}

	@Override
	public Bitmap getBitmap() {
		return getSprites().get(frameIndex);
	}

	protected abstract List<Bitmap> getSprites();
}
