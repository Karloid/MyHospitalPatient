package com.krld.patient.game;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;
import com.krld.patient.R;

public class SpeedBonus extends Bonus {
    public static Bitmap sprite;

    SpeedBonus(float x, float y, GameView context) {
        super(x, y, context);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);
    }

    public static void init(Resources resources) {
        sprite = Utils.loadSprite(R.raw.speedbonus, resources, GameView.DEFAULT_SCALE_FACTOR_FOR_BONUS);

    }

    public void activate(Player owner) {
        owner.effects.add(new SpeedEffect(owner));
    }
}
