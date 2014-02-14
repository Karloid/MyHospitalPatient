package com.krld.patient;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;

public class Needle extends Bullet
{

	public long getBirthDate()
	{
		return birthDate;
	}
	
	

	public static Bitmap sprite;

	long birthDate;
	static long lifeTime = 10000;

	private static final float TOUCH_RANGE = 30;

	private static float touchDmg = 10; 
	Needle(float x, float y, Game context)
	{
		super(x, y, context);
		speed = 26;
	}
	Needle(float x, float y 
		   , Game context, Unit target)
	{
		this(x, y, context, target.x,target.y);
	}
	
	Needle(float x, float y 
	, Game context, float targetX, float targetY)
	{
		super(x, y, context);
		speed = 16;
		size = 10;
		moveX = targetX;
		moveY = targetY;
		birthDate = System.currentTimeMillis();
	}

	public boolean touchPlayer()
	{
		if (Math.abs(x - context.player.x) < TOUCH_RANGE &&
			Math.abs(y - context.player.y) < TOUCH_RANGE)
		{
			context.player.damage(touchDmg);
			context.animations.add(new BloodAnimation(x,y, context));
			return true;	
		}
		return false;
	}


	public void draw(Canvas canvas, Paint paint)
	{   
		Utils.drawBitmapRotate(sprite, x, y, Utils.getAngle(deltaX, deltaY) -90, canvas, paint);
	}
	
	public static void init(Resources resources)
	{
		int scale = 2;
		Options options = new
			BitmapFactory.Options();
		options.inScaled = false;
		sprite = BitmapFactory.decodeResource(resources, R.raw.needle, options);
		sprite = Bitmap.createScaledBitmap(sprite,
										   sprite.getWidth() * scale, sprite.getHeight() * scale, false);
	}
}
