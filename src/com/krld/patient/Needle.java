package com.krld.patient;

import android.graphics.*;

public class Needle extends Unit
{
	Float deltaX, deltaY;

	long birthDate;
	static long lifeTime = 10000;

	private static final float TOUCH_RANGE = 30;

	private static float touchDmg = 10; 
	Needle(float x, float y, Game context)
	{
		super(x, y, context);
		speed = 8;
	}
	Needle(float x, float y 
		   , Game context, Unit target)
	{
		super(x, y, context);
		speed = 12;
		size = 10;
		moveX = target.x;
		moveY = target.y;
		birthDate = System.currentTimeMillis();
	}

	public boolean touchPlayer()
	{
		if (Math.abs(x - context.player.x) < TOUCH_RANGE &&
			Math.abs(y - context.player.y) < TOUCH_RANGE)
		{
			context.player.damage(touchDmg);
			return true;	
		}
		return false;
	}

	public void move()
	{
		if (moveX == null || moveY == null || speed == 0) 
			return;
		if (deltaX == null || deltaY == null)
		{
			float tX = Math.abs(x - moveX) / speed;
			float tY = Math.abs(y - moveY) / speed;
			if (tX > tY)
			{
				deltaX = (moveX - x) / tX;
				deltaY = (moveY - y) / tX;				
			}
			else
			{
				deltaX = (moveX - x) / tY;
				deltaY = (moveY - y) / tY;
			}
			float multiply = speed / (Math.abs(deltaX) + Math.abs(deltaY));
			deltaX *= multiply;
			deltaY *= multiply;	
		}
		x += deltaX;
		y += deltaY;
	}

	public void draw(Canvas canvas, Paint paint)
	{
		if (deltaX > 0)
		{
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(8);
			canvas.drawLine(x - 25, y , x + 10, y , paint);
			paint.setStrokeWidth(16);
			canvas.drawLine(x - 16, y, x - 18, y, paint);
			canvas.drawLine(x - 25, y, x - 23, y, paint);
			paint.setStrokeWidth(6);
			paint.setColor(Color.YELLOW);
			canvas.drawLine(x - 10, y , x + 8, y , paint);
			paint.setStrokeWidth(1);
			paint.setColor(Color.BLACK);
			canvas.drawLine(x + 10, y , x + 35, y , paint);
		}
		else
		{
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(8);
			canvas.drawLine(x + 25, y , x - 10, y , paint);
			paint.setStrokeWidth(16);
			canvas.drawLine(x + 16, y, x + 18, y, paint);
			canvas.drawLine(x + 25, y, x + 23, y, paint);
			paint.setStrokeWidth(6);
			paint.setColor(Color.YELLOW);
			canvas.drawLine(x + 10, y , x - 8, y , paint);
			paint.setStrokeWidth(1);
			paint.setColor(Color.BLACK);
			canvas.drawLine(x - 10, y , x - 35, y , paint);
		}
	}
}
