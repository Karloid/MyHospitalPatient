package com.krld.patient.game.model.bullets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Unit;
import com.krld.patient.game.model.animations.BloodAnimation;

public class Note extends Bullet {

    public static final int SCALE_FACTOR = 5;
    public static final int SPEED = 270;

    public long getBirthDate() {
        return birthDate;
    }


    public static Bitmap sprite;

    long birthDate;
    static long lifeTime = 10000;

    private static final float TOUCH_RANGE = 30;

    private static float touchDmg = 10;

    public Note(float x, float y, GameView context, Unit target) {
        super(x, y, context);
        speed = SPEED;
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
        return Math.abs(x - moveX) < TOUCH_RANGE &&
                Math.abs(y - moveY) < TOUCH_RANGE;
    }

    public void draw(Canvas canvas, Paint paint, GameCamera camera) {
        Utils.drawBitmapRotate(sprite, x - camera.getX(), y - camera.getY(), Utils.getAngle(deltaX, deltaY) - 90, canvas, paint);
    }

    @Override
    public Bitmap getBitmap() {
        return sprite;
    }

    public static void init(Resources resources) {
        sprite = Utils.loadSprite(R.raw.note, resources, SCALE_FACTOR);
    }

    public void postAction() {
        if (touchPlayer()) return;
        context.creeps.add(new NoteDummy(x, y, context));
    }

    public void reverseDirection(Unit unit) {
        super.reverseDirection(unit);

    }

}
