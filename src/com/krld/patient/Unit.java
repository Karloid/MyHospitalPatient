package com.krld.patient;
import android.content.res.*;
import android.graphics.*;
import java.util.*;

public abstract class Unit
{

	float x;

	float y;

    Float moveX;
	Float moveY;

	float speed;

	int size;
	int hp;
	int maxHp;

	Game context;

	private static final float PICK_RANGE = 50;

	private static final int DECAY_DMG = 1;
	Unit(float x, float y, Game context)
	{
		this.x = x;
		this.y = y;
		this.context = context;
		size = 30;
		speed = 0;
		maxHp = 100;
		hp = maxHp;
	}
	public void draw(Canvas canvas, Paint paint)
	{
		paint.setColor(Color.RED);
		canvas.drawRect(x - size / 2, y - size / 2 , x + size / 2, y + size / 2, paint);

	}	
	public void moveTo(float x, float y)
	{
		moveX = x;
		moveY = y;
	}

	public void move()
	{
		if (moveX == null || moveY == null || speed == 0) 
			return;
		float tX = Math.abs(x - moveX) / speed;
		float tY = Math.abs(y - moveY) / speed;
		if (tX > tY)
		{
			x += (moveX - x) / tX;
			y += (moveY - y) / tX;
		}
		else
		{
			x += (moveX - x) / tY;
			y += (moveY - y) / tY;
		}
		if (Math.abs(x - moveX) < speed &&
			Math.abs(y - moveY) < speed	){
			moveX = null;
			moveY = null;
    	}

	}

	public void collect()
	{
		List<Medkit> medkitsToRemove = new ArrayList<Medkit>();
		for (Medkit medkit:context.medkits)
		{
			if (Math.abs(medkit.x - x) < PICK_RANGE && 
				Math.abs(medkit.y - y) < PICK_RANGE)
			{
				this.heal(medkit.value);
				medkitsToRemove.add(medkit);
			}
		}
		context.medkits.removeAll(medkitsToRemove);
	}

	private void heal(float value)
	{
		hp += value;
		if (hp > maxHp) 
			hp = maxHp;
	}

	public void damage(float dmg)
	{
		hp -= dmg;
		if (hp < 0) 
			hp = 0;
	}

	public void attackPlayer()
	{

	}
	public static void init(Resources resources ){
		
	}
}
