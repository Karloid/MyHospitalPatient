package com.krld.patient;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;
import java.util.*;

public class Player extends Unit
{
	public static Bitmap sprite;

	private float decayDmg;

	public int lives;

	public List<Effect> effects;

	public static int scale;

	public static final float SPEED = 10;

	Player(float x, float y, Game context)
	{
		super(x, y, context);
		speed = SPEED;
		hp = 55;
		decayDmg = 1;
		lives = 3;

		effects = new ArrayList<Effect>();
		effects.add(new ShieldEffect(this));
	}
	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawBitmap(sprite, x - sprite.getWidth(), y - sprite.getHeight() / 1.5f, paint);
		//	canvas.drawText(sprite.getWidth() +" " +(0 - (sprite.getWidth()) / 2), 100,100, paint);
		for (Effect effect:effects)
		{
			effect.draw(canvas, paint);
		}		
	}

	public void move()
	{
		super.move();
		decay();
		processEffects();
		checkHp();

	}

	private void processEffects()
	{
		List<Effect> effectsToRemove = new ArrayList<Effect>();
		for (Effect effect:effects)
		{
			effect.effect();
			if (!effect.checkEffectTime()) {
				effect.postEffect();
				effectsToRemove.add(effect);
				}
		}
		effects.removeAll(effectsToRemove);
	}

	private void checkHp()
	{
		if (hp == 0)
		{
			lives--;
			hp = maxHp;
			effects.add(new ShieldEffect(this));
			x = 250;
			y = 300;
		}
	}

	private void decay()
	{
		if (Math.random() < 0.3f)
	    	damage(decayDmg);
	}

	public static void init(Resources resources)
	{ scale = 3;
		Options options = new
			BitmapFactory.Options();
		options.inScaled = false;
		sprite = BitmapFactory.decodeResource(resources, R.raw.player, options);
		sprite = Bitmap.createScaledBitmap(sprite,
										   sprite.getWidth() * scale, sprite.getHeight() * scale, false);
	}
	@Override
	public void damage(float dmg)
	{ 
		for (Effect effect: effects)
		{
			if (effect instanceof ShieldEffect)
				return;
		}
		if (dmg >6)
	    	context.decals.add(new BloodSpot(x + (float) Math.random() * 80 - 40, y + (float) Math.random() * 80 - 40, context));
		super.damage(dmg);
	}
}
