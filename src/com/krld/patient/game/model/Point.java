package com.krld.patient.game.model;

public class Point {
	private float x;
	private float y;

	public Point(float x, float y) {
		setX(x);
		setY(y);
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}
}
