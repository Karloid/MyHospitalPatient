package com.krld.patient.game.model.creeps;

import android.content.res.*;
import android.graphics.*;
import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;
import com.krld.patient.game.model.bullets.Needle;

public class Nurse extends Creep {
    public static Bitmap sprite;

    private long lastMove;

    private static final long MOVE_DELAY = 10000;

    private long lastLaunch;

    private static long launchCooldown = 300;

    private static final float launchNeedleChance = 0.1f;

    private static final int REWARD = 200;

    public Nurse(float x, float y, GameView context) {
        super(x, y, context);
        speed = 150;
        moveX = x + 1;
        moveY = y + 2;
        lastMove = System.currentTimeMillis();
        float rnd = (float) Math.random();
        if (rnd < 0.25f) {
            x = 0;
        } else if (rnd < 0.5f) {
            x = 545;
        } else if (rnd < 0.75f) {
            y = 0;
        } else {
            y = 670;
        }

    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);
    }

    @Override
    public Bitmap getBitmap() {
        return sprite;
    }

    public void move(float delta) {
        super.move(delta);
        attackPlayer();
        if (moveX == null && moveY == null &&
                System.currentTimeMillis() - lastMove > MOVE_DELAY) {
            moveX = (float) (20 + Math.random() * 500);
            moveY = (float) (20 + Math.random() * 690);
            lastMove = System.currentTimeMillis();
        }
    }

    public void attackPlayer() {
        if (System.currentTimeMillis() - lastLaunch > launchCooldown && Math.random() < getLaunchChance()) {
            launchNeedle();
            lastLaunch = System.currentTimeMillis();
        }
    }

    public double getLaunchChance() {
        return launchNeedleChance;
    }

    void launchNeedle() {
        context.bullets.add(new Needle(x, y, context, context.player));
    }

    public static void init(Resources resources) {
        sprite = Utils.loadSprite(R.raw.nurse, resources);
    }

    public int getReward() {
        return REWARD;
    }
}
