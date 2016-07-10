package com.krld.patient.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.krld.patient.game.camera.GameCamera;

public interface Drawable {
    public void draw(Canvas canvas, Paint paint, GameCamera camera);

    public void draw(Canvas canvas, Paint paint);

    public Bitmap getBitmap();
}
