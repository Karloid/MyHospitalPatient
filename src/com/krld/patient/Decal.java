package com.krld.patient;

public class Decal extends Unit
{ 
	private long duration;
	private long birthDate;
	Decal(float x, float y, Game context)
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
	
