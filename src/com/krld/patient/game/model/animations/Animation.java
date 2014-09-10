package com.krld.patient.game.model.animations;

import android.graphics.*;
import com.krld.patient.game.GameView;
import com.krld.patient.game.model.Unit;

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
