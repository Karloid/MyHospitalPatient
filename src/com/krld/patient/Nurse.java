package com.krld.patient;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.Options;

public class Nurse extends Unit
{
	public static Bitmap sprite;
	
	private long lastMove;

	private static final long MOVE_DELAY = 10000;
	Nurse(float x, float y, Game context)
	{
		super(x, y, context);
		speed = 5;
	}

	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawBitmap(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);
		paint.setStrokeWidth(8);
		paint.setColor(Color.WHITE);
		canvas.drawLine(x, y - 20, x, y + 20, paint);
		paint.setColor(Color.LTGRAY);
		canvas.drawCircle(x, y - 50, 30, paint);
		paint.setColor(Color.WHITE);
		canvas.drawRect(x - 30, y - 50, x + 30, y - 100, paint);
		paint.setStrokeWidth(4);
		canvas.drawLine(x + 30, y + 50, x, y + 20, paint);
		canvas.drawLine(x - 30, y + 50, x, y + 20, paint);
		canvas.drawLine(x + 30, y - 10, x, y - 15, paint);
		canvas.drawLine(x - 30, y - 10, x, y - 15, paint);
		paint.setColor(Color.RED);
		canvas.drawRect(x - size / 3, y - size / 15 - 80,
						x + size / 3, y + size / 15 - 80, paint);
		canvas.drawRect(x - size / 15, y - size / 3 - 80,
						x + size / 15, y + size / 3 - 80, paint);	

		//	paint.setColor(Color.MAGENTA);
		//	canvas.drawText("NURSE", x - 21, y -50, paint);		
	}
	public void move()
	{
		super.move();
		attackPlayer();
		if (moveX == null && moveY == null && 
		System.currentTimeMillis() - lastMove > MOVE_DELAY)
		{
			moveX = (float)(20 + Math.random() * 500);
			moveY = (float)(20 + Math.random() * 690);
			lastMove = System.currentTimeMillis();
		}
	}
	public void attackPlayer()
	{
		if (Math.random() > 0.9f)
		{
			launchNeedle();
		}
	}

	private void launchNeedle()
	{
		context.needles.add(new Needle(x, y, context, 									   context.player));
	}
	
	public static void init(Resources resources)
	{
		int scale = 5;
		Options options = new
			BitmapFactory.Options();
		options.inScaled = false;
		sprite = BitmapFactory.decodeResource(resources, R.raw.nurse, options);
		sprite = Bitmap.createScaledBitmap(sprite,
										   sprite.getWidth() * scale, sprite.getHeight() * scale, false)
	}
}
