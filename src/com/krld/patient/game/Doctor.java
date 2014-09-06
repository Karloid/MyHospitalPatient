package com.krld.patient.game;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;
import com.krld.patient.*;

public class Doctor extends Nurse {
    public static Bitmap sprite;

    private static final int REWARD = 500;

    private double launchNeedleChance = 0.05d;

    Doctor(float x, float y, GameView context) {
        super(x, y, context);
    }

    void launchNeedle() {
        context.bullets.add(new Note(x, y, context, context.player));
    }

    public static void init(Resources resources) {
        sprite = Utils.loadSprite(R.raw.doctor, resources);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);
    }

    public int getReward() {
        return REWARD;
    }

    public double getLaunchChance() {
        return launchNeedleChance;
    }
}
