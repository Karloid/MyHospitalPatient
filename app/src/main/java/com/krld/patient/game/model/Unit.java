package com.krld.patient.game.model;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.krld.patient.game.Drawable;
import com.krld.patient.game.GameView;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.animations.BloodAnimation;
import com.krld.patient.game.model.bonuses.Bonus;
import com.krld.patient.game.model.decals.BloodSpot;

import java.util.ArrayList;
import java.util.List;

public abstract class Unit implements Drawable {

	protected boolean visible;
	public float x;

	public float y;

	protected Float moveX;
	protected Float moveY;

	public float speed;

	public int size;
	public float hp;
	public int maxHp;

	public GameView context;

	private static final float PICK_RANGE = 50;

	private static final int DECAY_DMG = 1;

	public Unit(float x, float y, GameView context) {
		this.x = x;
		this.y = y;
		this.context = context;
		size = 30;
		speed = 0;
		maxHp = 100;
		hp = maxHp;
		visible = true;
	}

	public void draw(Canvas canvas, Paint paint, GameCamera camera) {
		paint.setColor(Color.RED);
		canvas.drawRect(x - size / 2 - camera.getX(), y - size / 2 - camera.getY(), x + size / 2, y + size / 2, paint);
	}

	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(Color.RED);
		canvas.drawRect(x - size / 2, y - size / 2, x + size / 2, y + size / 2, paint);
	}

	public void moveTo(float x, float y) {
		moveX = x;
		moveY = y;
	}

	public void move(float delta) {
		positionUpdate(delta);
	}

	protected void positionUpdate(float delta) {
		if (moveX == null || moveY == null || speed == 0)
			return;
		float realSpeed = speed * delta;
		float tX = Math.abs(x - moveX) / realSpeed;
		float tY = Math.abs(y - moveY) / realSpeed;
		float newX;
		float newY;
		if (tX > tY) {
			newX = x + (moveX - x) / tX;
			newY = y + (moveY - y) / tX;
		} else {
			newX = x + (moveX - x) / tY;
			newY = y + (moveY - y) / tY;
		}
		if (!context.checkLegalPosition(newX, newY, this) || Math.abs(x - moveX) < realSpeed &&
				Math.abs(y - moveY) < realSpeed) {
			moveX = null;
			moveY = null;
		} else {
			x = newX;
			y = newY;
		}
	}

	public void collect(float delta) {
		List<Bonus> bonusToRemove = new ArrayList<Bonus>();
		for (Bonus bonus : context.bonuses) {
			if (Math.abs(bonus.x - x) < PICK_RANGE &&
					Math.abs(bonus.y - y) < PICK_RANGE) {
				bonus.activate((Player) this);
				bonusToRemove.add(bonus);
			}
		}
		context.bonuses.removeAll(bonusToRemove);
	}

	public void heal(float value) {
		hp += value;
		if (hp > maxHp)
			hp = maxHp;
	}

	public void damage(float dmg) {
		hp -= dmg;    //TODO DEBUG MODE
		if (hp < 0)
			hp = 0;
	}

	public void attackPlayer() {

	}

	public static void init(Resources resources) {

	}

	public void createDeadDecalsAmdAnimations() {
		for (int i = 0; i < 5; i++)
			context.decals.add(new BloodSpot(x + (float) Math.random() * 80 - 40, y + (float) Math.random() * 80 - 40, context));
		for (int i = 0; i < 14; i++)
			context.animations.add(new BloodAnimation(x + (float) Math.random() * 80 - 40, y + (float) Math.random() * 80 - 40, context));
	}

	public boolean needRemove() {
		return false;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
