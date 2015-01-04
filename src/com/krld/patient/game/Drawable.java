package com.krld.patient.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public interface Drawable {
    public void draw(Canvas canvas, Paint paint);
    public Bitmap getBitmap();
}
