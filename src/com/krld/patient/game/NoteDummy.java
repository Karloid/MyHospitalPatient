package com.krld.patient.game;

import android.graphics.*;

public class NoteDummy extends Creep
{

	private boolean activated = false;
	NoteDummy(float x, float y, GameView context)
	{
		super(x, y, context);
		speed = 0;
	}

	public void move()
	{
		if (activated) return;
		launchNeedles();
		activated = true;
	}
    
	public void birth(){
		for ( int i = 0; i< 5; i++)
			context.animations.add(new CloudAnimation(x +(float) Math.random() * 60 - 30,y +(float) Math.random() * 60 - 30,context));
	}
	private void launchNeedles()
	{
		context.bullets.add(new Needle(x, y , context, x + 500, y + 500));
		context.bullets.add(new Needle(x, y , context, x, y + 500));
		context.bullets.add(new Needle(x, y , context, x - 500, y + 500));
		context.bullets.add(new Needle(x, y , context, x - 500, y));
		context.bullets.add(new Needle(x, y , context, x - 500, y - 500));
		context.bullets.add(new Needle(x, y , context, x, y - 500));
		context.bullets.add(new Needle(x, y , context, x + 500, y - 500));
		context.bullets.add(new Needle(x, y , context, x + 500, y));
	}

	public void draw(Canvas canvas, Paint paint)
	{
	}

	public boolean needRemove()
	{
		return activated;
	}
}
