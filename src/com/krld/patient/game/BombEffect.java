package com.krld.patient.game;
import android.graphics.*;
import java.util.*;

public class BombEffect extends Effect
{
	float effectRadius;
	BombEffect(Unit owner)
	{
		super(owner);
		effectRadius = 20;
		duration = 550;
		owner.context.decals.add(new BombSpot(owner.x, owner.y, owner.context));
	}
	public void draw(Canvas canvas, Paint paint)
	{
		paint.setColor(Color.RED);
		paint.setAlpha(200);
		canvas.drawCircle(owner.x , owner.y + yCorrection, effectRadius, paint);
    	paint.setColor(Color.YELLOW);
		paint.setAlpha(88);
    	canvas.drawCircle(owner.x , owner.y + yCorrection, effectRadius / 5, paint);
		paint.setAlpha(255);
		effectRadius *= 1.5;
	}

	public void effect()
	{ 

		List<Unit> unitsToRemove = null;
		for (Creep creep:owner.context.creeps)
		{
			if (Utils.getDistance(owner, creep) < effectRadius)
			{
				if (unitsToRemove == null)
					unitsToRemove = new ArrayList<Unit>();
				creep.die();		
				unitsToRemove.add(creep);
			}

		}
		if (unitsToRemove != null)
		{
			owner.context.creeps.removeAll(unitsToRemove);
		}
		
			for (Bullet needle:owner.context.bullets)
			{
				if (Utils.getDistance(owner, needle) < effectRadius)
				{

					needle.moveOut(owner);	
				}
			}
	}
}
