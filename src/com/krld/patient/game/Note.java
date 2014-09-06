package com.krld.patient.game;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;
import com.krld.patient.R;

public class Note extends Bullet {

    public static final int SCALE_FACTOR = 5;

    public long getBirthDate() {
        return birthDate;
    }


    public static Bitmap sprite;

    long birthDate;
    static long lifeTime = 10000;

    private static final float TOUCH_RANGE = 30;

    private static float touchDmg = 10;

    Note(float x, float y, GameView context) {
        super(x, y, context);
        speed = 26;
    }

    Note(float x, float y
            , GameView context, Unit target) {
        super(x, y, context);
        speed = 26;
        size = 10;
        moveX = target.x;
        moveY = target.y;
        birthDate = System.currentTimeMillis();
    }

    public boolean touchPlayer() {
        if (Math.abs(x - context.player.x) < TOUCH_RANGE &&
                Math.abs(y - context.player.y) < TOUCH_RANGE) {
            context.animations.add(new BloodAnimation(x + 25, y, context));
            context.animations.add(new BloodAnimation(x, y + 25, context));
            context.animations.add(new BloodAnimation(x, y, context));
            context.player.damage(touchDmg);
            return true;
        }
        return false;
    }

    public boolean achieveTarget() {
        if (Math.abs(x - moveX) < TOUCH_RANGE &&
                Math.abs(y - moveY) < TOUCH_RANGE) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas, Paint paint) {
        Utils.drawBitmapRotate(sprite, x, y, Utils.getAngle(deltaX, deltaY) - 90, canvas, paint);
    }

    public static void init(Resources resources) {
        sprite = Utils.loadSprite(R.raw.note, resources, SCALE_FACTOR);
    }

    public void postAction() {
        if (touchPlayer()) return;
        //	Game.debugMessage = "ok";
        context.creeps.add(new NoteDummy(x, y, context));
        //	context.bullets.add(new Needle(x, y , context, context.player));
        /*	context.bullets.add(new Needle(x, y , context, x, y + 5));
		 context.bullets.add(new Needle(x, y , context, x - 5, y + 5));
		 context.bullets.add(new Needle(x, y , context, x - 5, y));
		 context.bullets.add(new Needle(x, y , context, x - 5, y - 5));
		 context.bullets.add(new Needle(x, y , context, x, y - 5));
		 context.bullets.add(new Needle(x, y , context, x + 5, y - 5));
		 context.bullets.add(new Needle(x, y , context, x + 5, y));
		 */
    }

    public void moveOut(Unit unit) {
        super.moveOut(unit);

    }

}
