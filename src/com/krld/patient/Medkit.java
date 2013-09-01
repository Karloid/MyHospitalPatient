package com.krld.patient;
import android.graphics.*;

public class Medkit extends Unit
{
	float value = 15;
	Medkit(float x, float y, Game context)
	{
		super(x, y, context);
	}

	public void draw(Canvas canvas, Paint paint)
	{
		paint.setColor(Color.GRAY);
		canvas.drawRect(x - size / 2 - 3, y - size / 2 - 3 ,
						x + size / 2 + 3, y + size / 2 + 3, paint);


		paint.setColor(Color.WHITE);
		canvas.drawRect(x - size / 2, y - size / 2 ,
						x + size / 2, y + size / 2, paint);

		paint.setColor(Color.RED);
		canvas.drawRect(x - size / 3, y - size / 10,
						x + size / 3, y + size / 10, paint);
		canvas.drawRect(x - size / 10, y - size / 3,
						x + size / 10, y + size / 3, paint);	

	}
}
