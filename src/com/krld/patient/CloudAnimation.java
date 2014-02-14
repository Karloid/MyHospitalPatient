package com.krld.patient;


import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;
import com.krld.patient.*;
import java.util.*;

public class CloudAnimation extends Animation
{

	private byte frameIndex;

	private boolean stop;

	public byte getFrameIndex()
	{
		return frameIndex;
	}

	public Bitmap getFrame()
	{
		return sprites.get(getFrameIndex());
	}

    public static List<Bitmap> sprites;

	CloudAnimation(float x, float y, Game context)
	{
		super(x, y, context);
		birthDate = System.currentTimeMillis();
		frameIndex = 0;
		stop = false;
	}

	public static void init(Resources resources)
	{
		sprites = new ArrayList<Bitmap>();
		int scale = 2;
		
		sprites.add(Utils.loadSprite(R.raw.cloud0, resources, scale));

		sprites.add(Utils.loadSprite(R.raw.cloud1, resources, scale));

		sprites.add(Utils.loadSprite(R.raw.cloud2, resources, scale));

		sprites.add(Utils.loadSprite(R.raw.cloud3, resources, scale));

		sprites.add(Utils.loadSprite(R.raw.cloud4, resources, scale));

		sprites.add(Utils.loadSprite(R.raw.cloud5, resources, scale));
	
		sprites.add(Utils.loadSprite(R.raw.cloud6, resources, scale));
	}

	public void draw(Canvas canvas, Paint paint)
	{
		if (stop) return;
		canvas.drawBitmap(getFrame(), x - getFrame().getWidth() / 2, y - getFrame().getHeight() / 2, paint);	
		frameIndex++;
		if (frameIndex > sprites.size() - 1)
		{
			stop = true;
		}
	}

	public boolean checkAlive(){
		return !stop;
	}
}
