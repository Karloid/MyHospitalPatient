package com.krld.patient.game.model.bullets;

import com.krld.patient.game.GameView;
import com.krld.patient.game.model.Unit;
import com.krld.patient.game.model.animations.CloudAnimation;

public abstract class Bullet extends Unit {

	Float deltaX, deltaY;
	private boolean touchObstacle;

	Bullet(float x, float y, GameView context) {
		super(x, y, context);
	}

	public void postAction() {
	}

	public abstract long getBirthDate();

	public boolean achieveTarget() {
		return false;
	}

	void calculateDeltas() {
		float tX = Math.abs(x - moveX) / speed;
		float tY = Math.abs(y - moveY) / speed;
		if (tX > tY) {
			deltaX = (moveX - x) / tX;
			deltaY = (moveY - y) / tX;
		} else {
			deltaX = (moveX - x) / tY;
			deltaY = (moveY - y) / tY;
		}
		float multiply = speed / (Math.abs(deltaX) + Math.abs(deltaY));
		deltaX *= multiply;
		deltaY *= multiply;
	}


	public void move(float delta) {
		if (moveX == null || moveY == null || speed == 0)
			return;
		if (deltaX == null || deltaY == null) {
			calculateDeltas();
		}
		x += deltaX * delta;
		y += deltaY * delta;
		if (!context.checkLegalPosition(x, y, this)) {
			context.animations.add(new CloudAnimation(x, y, context));
			touchObstacle = true;
		}
	}

	public void reverseDirection(Unit unit) {
		moveX = (unit.x - x) * -1 + x;
		moveY = (unit.y - y) * -1 + y;

		calculateDeltas();
		context.animations.add(new CloudAnimation(x, y, context));
	}

	public abstract boolean touchPlayer();

	public boolean touchObstacle() {
		return touchObstacle;
	}
}
