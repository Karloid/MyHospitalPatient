package com.krld.patient.game.model.bullets;

import android.content.res.*;
import android.graphics.*;
import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;
import com.krld.patient.game.model.Unit;
import com.krld.patient.game.model.animations.BloodAnimation;

public class Needle extends Bullet {

    public static final int SCALE_FACTOR = 2;

    public long getBirthDate() {
        return birthDate;
    }


    public static Bitmap sprite;

    long birthDate;
    public static long lifeTime = 10000;

    private static final float TOUCH_RANGE = 30;

    private static float touchDmg = 10;

    Needle(float x, float y, GameView context) {
        super(x, y, context);
        speed = 26;
    }

    public Needle(float x, float y
            , GameView context, Unit target) {
        this(x, y, context, target.x, target.y);
    }

    Needle(float x, float y
            , GameView context, float targetX, float targetY) {
        super(x, y, context);
        speed = 16;
        size = 10;
        moveX = targetX;
        moveY = targetY;
        birthDate = System.currentTimeMillis();
    }

    public boolean touchPlayer() {
        if (Math.abs(x - context.player.x) < TOUCH_RANGE &&
                Math.abs(y - context.player.y) < TOUCH_RANGE) {
            context.player.damage(touchDmg);
            context.animations.add(new BloodAnimation(x, y, context));
            return true;
        }
        return false;
    }


    public void draw(Canvas canvas, Paint paint) {
        Utils.drawBitmapRotate(sprite, x, y, Utils.getAngle(deltaX, deltaY) - 90, canvas, paint);
    }

    public static void init(Resources resources) {
        sprite = Utils.loadSprite(R.raw.needle, resources, SCALE_FACTOR);
    }
}
