package com.krld.patient.game;

public class Decal extends Unit
{ 
	private long duration;
	private long birthDate;
	Decal(float x, float y, GameView context)
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
	
