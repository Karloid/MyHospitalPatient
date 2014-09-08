package com.krld.patient.game;

import android.graphics.*;

public class ShieldEffect extends Effect {


    ShieldEffect(Unit owner) {
        super(owner);
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.rgb(0, 255, 255));
        paint.setAlpha(60);
        canvas.drawCircle(owner.x, owner.y + yCorrection, 55, paint);
        paint.setAlpha(255);
    }
}