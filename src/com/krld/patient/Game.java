package com.krld.patient;

import android.content.*;
import android.graphics.*;
import android.view.*;
import java.util.*;
import android.media.*;

class Game extends View
{
	public Player player;

	public List<Medkit> medkits;
	public List<Nurse> nurses;
	public List<Needle> needles;
	public Game(Context context)
	{
		super(context);
		initSprites();
		player = new Player(200, 400, this);
		medkits = new ArrayList<Medkit>();
	    nurses = new ArrayList<Nurse>();
		nurses.add(new Nurse(200, 200, this));
		needles = new ArrayList<Needle>();
		//	final MediaPlayer mp = MediaPlayer.create(context, R.raw.s1);
		final Game game =	this;
		Thread runner = new Thread(new Runnable(){
				public void run()
				{ while (true)
					{
						game.update();
						game.postInvalidate();
						try
						{	
							Thread.sleep(100);
						}
						catch (Exception e)
						{}
					}
				}
			}
		);
		runner.start();
	}

	private void initSprites()
	{
		Player.init(getResources());
		Nurse.init(getResources());
	}

	@Override
	public void onDraw(Canvas canvas)
	{    
		//update();
		canvas.drawColor(Color.BLUE);
		Paint paint = new Paint();
		paint.setColor(Color.GREEN); 
		paint.setStrokeWidth(100);
		for (int y = 0, alpha = 190; alpha > 2; alpha -= 10, y
		+= 100)
		{
			paint.setAlpha(alpha);
			canvas.drawLine(0, y, 550, y, paint);
		}
		drawEntitys(canvas, paint);
		drawPlayer(canvas, paint);
		drawUI(canvas, paint);
	}

	private void drawEntitys(Canvas canvas, Paint paint)
	{
		for (Medkit medkit: medkits)
		{
			medkit.draw(canvas, paint);
		}
		for (Nurse nurse:nurses)
		{
			nurse.draw(canvas, paint);
		}
		for (Needle needle:needles)
		{
			needle.draw(canvas, paint);
		}
	}

	private void update()
	{   
	    spawnMedkits();
		moveUnits();
		player.move();
		player.collect();
	}

	private void moveUnits()
	{
		for (Nurse nurse:nurses)
		{
			nurse.move();
		}
		List<Needle> needleToRemove = new ArrayList<Needle>();
		long currentTimeMillis = System.currentTimeMillis();
		for (Needle needle:needles)
		{
			needle.move();	
			if (needle.touchPlayer() || currentTimeMillis - needle.birthDate > Needle.lifeTime)
				needleToRemove.add(needle);
		}
		needles.removeAll(needleToRemove);
	}

	private void spawnMedkits()
	{
		if (Math.random() < 0.0512f && medkits.size() < 10)
			medkits.add(new Medkit((float)(20 + Math.random() * 500), 
								   (float)(20 + Math.random() * 690), this));	
	}

	private void drawUI(Canvas canvas, Paint paint)
	{
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, 700, 540, 960, paint);
		paint.setColor(Color.rgb(44, 13, 13));
		canvas.drawRect(0, 710, 170, 950, paint);
		paint.setColor(Color.DKGRAY);
		canvas.drawRect(180, 710, 410, 800, paint);
		canvas.drawRect(180, 810, 410, 950, paint);
		canvas.drawRect(420, 710, 540, 950, paint);

		drawHp(canvas, paint);
	}

	private void drawHp(Canvas canvas, Paint paint)
	{
		paint.setColor(Color.RED);
		float multiplayer = player.hp / (player.maxHp * 1f);
		//	canvas.drawText(multiplayer + " " + player.hp, 100, 100, paint);
		canvas.drawRect(0, 950 - (950 - 710) * multiplayer, 170, 950, paint);
	}

	private void drawPlayer(Canvas canvas, Paint paint)
	{
		player.draw(canvas, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();
		player.moveTo(x, y);

		return super.onTouchEvent(event);
	}}
