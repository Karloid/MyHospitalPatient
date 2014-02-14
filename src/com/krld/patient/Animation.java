package com.krld.patient;

import android.graphics.*;

public abstract class Animation extends Unit
{
    long birthDate;
	Animation(float x, float y, Game context)
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
