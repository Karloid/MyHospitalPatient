package com.krld.patient.game;

import android.graphics.*;

public abstract class Animation extends Unit
{
    long birthDate;
	Animation(float x, float y, GameView context)
	{
		super(x, y, context);
		birthDate = System.currentTimeMillis();
	}


	public boolean checkAlive()
	{
	 return true;
	}
	
	public abstract byte getFrameIndex();
	public abstract Bitmap getFrame();
}
