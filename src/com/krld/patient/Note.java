package com.krld.patient;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;

public class Note extends Bullet
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
	Note(float x, float y, Game context)
	{
		super(x, y, context);
		speed = 26;
	}
	Note(float x, float y 
		 , Game context, Unit target)
	{
		super(x, y, context);
		speed = 26;
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
			context.animations.add(new BloodAnimation(x + 25, y, context));
			context.animations.add(new BloodAnimation(x, y + 25, context));
			context.animations.add(new BloodAnimation(x, y, context));
			context.player.damage(touchDmg);
			return true;	
		}
		return false;
	}

	public boolean achieveTarget()
	{
		if (Math.abs(x - moveX) < TOUCH_RANGE &&
			Math.abs(y - moveY) < TOUCH_RANGE)
		{
			return true;
		}
		return false;
	}

	public void draw(Canvas canvas, Paint paint)
	{   
		Utils.drawBitmapRotate(sprite, x, y, Utils.getAngle(deltaX, deltaY) - 90, canvas, paint);
	}

	public static void init(Resources resources)
	{
		int scale = 5;
		Options options = new
			BitmapFactory.Options();
		options.inScaled = false;
		sprite = BitmapFactory.decodeResource(resources, R.raw.note, options);
		sprite = Bitmap.createScaledBitmap(sprite,
										   sprite.getWidth() * scale, sprite.getHeight() * scale, false);
	}

	public void postAction()
	{
		if (touchPlayer()) return;
		//	Game.debugMessage = "ok";
		context.creeps.add(new NoteDummy(x,y, context));
		//	context.bullets.add(new Needle(x, y , context, context.player));
		/*	context.bullets.add(new Needle(x, y , context, x, y + 5));
		 context.bullets.add(new Needle(x, y , context, x - 5, y + 5));
		 context.bullets.add(new Needle(x, y , context, x - 5, y));
		 context.bullets.add(new Needle(x, y , context, x - 5, y - 5));
		 context.bullets.add(new Needle(x, y , context, x, y - 5));
		 context.bullets.add(new Needle(x, y , context, x + 5, y - 5));
		 context.bullets.add(new Needle(x, y , context, x + 5, y));
		 */
	}
	
	public void moveOut(Unit unit) {
		super.moveOut(unit);
		
	}
	
}
