package com.krld.patient.game.model.decals;

import android.graphics.Bitmap;
import com.krld.patient.game.GameView;
import com.krld.patient.game.model.Unit;

public abstract class Decal extends Unit
{ 
	private long duration;
	private long birthDate;
	protected Decal(float x, float y, GameView context)
	{
		super(x, y, context);
		birthDate = System.currentTimeMillis();
		duration = 10000;
	}


	public boolean checkAlive()
	{
		if (System.currentTimeMillis() - birthDate > duration)
		{
			return false;
		}
		else return true;
	}
}
	
