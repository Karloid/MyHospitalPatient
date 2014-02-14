package com.krld.patient;
import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;

public class ShieldBonus extends Bonus
{
	public static Bitmap sprite;

	ShieldBonus(float x, float y, Game context)
	{
		super(x, y, context);
	}

	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawBitmap(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);
	}

	public static void init(Resources resources)
	{
		int scale = 2;
		Options options = new
			BitmapFactory.Options();
		options.inScaled = false;
		sprite = BitmapFactory.decodeResource(resources, R.raw.shieldbonus, options);
		sprite = Bitmap.createScaledBitmap(sprite,
										   sprite.getWidth() * scale, sprite.getHeight() * scale, false);

	}

	public void activate(Player owner){
		owner.effects.add(new ShieldEffect(owner));
	}
}
