package com.krld.patient.game;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;
import com.krld.patient.R;

public class Medkit extends Bonus {
    float value = 15;
    public static Bitmap sprite;

    Medkit(float x, float y, GameView context) {
        super(x, y, context);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);
    }

    public static void init(Resources resources) {
        sprite = Utils.loadSprite(R.raw.medkit, resources, GameView.DEFAULT_SCALE_FACTOR_FOR_BONUS);

    }

    public void activate(Player owner) {
        owner.heal(value);
    }
}
