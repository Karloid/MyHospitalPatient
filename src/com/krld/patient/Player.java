package com.krld.patient;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.Options;

public class Player extends Unit
{
	public static Bitmap sprite;

	private float decayDmg;
	Player(float x, float y, Game context)
	{
		super(x, y, context);
		speed = 10;
		hp = 55;
		decayDmg = 1;
	}
	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawBitmap(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);		
	}

	public void move()
	{
		super.move();
		decay();

	}

	private void decay()
	{
		if (Math.random() < 0.3f)
	    	damage(decayDmg);
	}

	public static void init(Resources resources)
	{
		int scale = 4;
		Options options = new
			BitmapFactory.Options();
		options.inScaled = false;
		sprite = BitmapFactory.decodeResource(resources, R.raw.player, options);
		sprite = Bitmap.createScaledBitmap(sprite,
										   sprite.getWidth() * scale, sprite.getHeight() * scale, false);

	}
}
