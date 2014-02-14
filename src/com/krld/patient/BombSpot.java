package com.krld.patient;
import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;

public class BombSpot extends Decal
{
	public static Bitmap sprite;
	private byte type;

	private static Bitmap sprite1;

	private static Bitmap sprite2;

	private static Bitmap sprite3;

	private static Bitmap sprite4;

	private float rotare;

	BombSpot(float x, float y, Game context)
	{
		super(x, y, context);
		type = (byte)(Math.random() * 5);
		rotare =Math.round( 360 * Math.random());
	}

	public void draw(Canvas canvas, Paint paint)
	{  
	    Bitmap bitmap = sprite;
		if (type == 1) 
			bitmap = sprite1;
		else if (type == 2) 
			bitmap = sprite2;
		else if (type == 3) 
			bitmap = sprite3;
bitmap =sprite;
		paint.setAlpha(200);
		//	canvas.drawBitmap(bitmap, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);
		Utils.drawBitmapRotate(bitmap, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2,rotare ,canvas, paint);
		paint.setAlpha(255);
	}

	public static void init(Resources resources)
	{
		int scale = 4;
		Options options = new
			BitmapFactory.Options();
		options.inScaled = false;
		sprite = BitmapFactory.decodeResource(resources, R.raw.boomspot, options);
		sprite = Bitmap.createScaledBitmap(sprite,
										   sprite.getWidth() * scale, sprite.getHeight() * scale, false);


		sprite1 = BitmapFactory.decodeResource(resources, R.raw.bloodspot1, options);
		sprite1 = Bitmap.createScaledBitmap(sprite1,
											sprite1.getWidth() * scale, sprite1.getHeight() * scale, false);

		sprite2 = BitmapFactory.decodeResource(resources, R.raw.bloodspot2, options);
		sprite2 = Bitmap.createScaledBitmap(sprite2,
											sprite2.getWidth() * scale, sprite2.getHeight() * scale, false);	

		sprite3 = BitmapFactory.decodeResource(resources, R.raw.bloodspot3, options);
		sprite3 = Bitmap.createScaledBitmap(sprite3,
											sprite3.getWidth() * scale, sprite3.getHeight() * scale, false);	

		sprite4 = BitmapFactory.decodeResource(resources, R.raw.bloodspot4, options);
		sprite4 = Bitmap.createScaledBitmap(sprite4,
											sprite4.getWidth() * scale, sprite4.getHeight() * scale, false);							
	}

}
