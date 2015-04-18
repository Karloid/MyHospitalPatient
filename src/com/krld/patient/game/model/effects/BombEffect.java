package com.krld.patient.game.model.effects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.krld.patient.game.Utils;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Unit;
import com.krld.patient.game.model.bullets.Bullet;
import com.krld.patient.game.model.creeps.Creep;
import com.krld.patient.game.model.decals.BombSpot;

import java.util.ArrayList;
import java.util.List;

public class BombEffect extends Effect {
	private final float effectStartRadius;
	float effectCurrentRadius;
	float effectMaxRadius;

	public BombEffect(Unit owner) {
		super(owner);
		effectStartRadius = 20;
		effectCurrentRadius = effectStartRadius;
		durationTime = 0.3f;
		effectMaxRadius = 200;
		owner.context.decals.add(new BombSpot(owner.x, owner.y, owner.context));
	}

	public void draw(Canvas canvas, Paint paint, GameCamera camera) {
		paint.setColor(Color.RED);
		paint.setAlpha(200);
		float cx = owner.x - camera.getX();
		float cy = owner.y + yCorrection - camera.getY();
		canvas.drawCircle(cx, cy, effectCurrentRadius, paint);
		paint.setColor(Color.YELLOW);
		paint.setAlpha(88);
		canvas.drawCircle(cx, cy, effectCurrentRadius / 5, paint);
		paint.setAlpha(255);
	}

	public void doEffect(float delta) {
		super.doEffect(delta);

		List<Creep> unitsToRemove = null;
		for (Creep creep : owner.context.creeps) {
			if (Utils.getDistance(owner, creep) < effectCurrentRadius) {
				if (unitsToRemove == null)
					unitsToRemove = new ArrayList<Creep>();
				creep.die();
				unitsToRemove.add(creep);
			}

		}
		if (unitsToRemove != null) {
			owner.context.creeps.removeAll(unitsToRemove);
		}

		for (Bullet needle : owner.context.bullets) {
			if (Utils.getDistance(owner, needle) < effectCurrentRadius) {

				needle.reverseDirection(owner);
			}
		}
		float deltaRadius = effectMaxRadius - effectStartRadius;
		effectCurrentRadius = Math.min(deltaRadius * (currentLifeTime / durationTime), effectMaxRadius);
	}
}
