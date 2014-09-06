package com.krld.patient.game;


import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;
import com.krld.patient.*;

import java.util.*;

public class BloodAnimation extends Animation {

    private byte frameIndex;

    private boolean stop;
    public static final int SCALE_FACTOR = 4;

    public byte getFrameIndex() {
        return frameIndex;
    }

    public Bitmap getFrame() {
        return sprites.get(getFrameIndex());
    }

    public static List<Bitmap> sprites;

    BloodAnimation(float x, float y, GameView context) {
        super(x, y, context);
        birthDate = System.currentTimeMillis();
        frameIndex = 0;
        stop = false;
    }

    public static void init(Resources resources) {
        sprites = new ArrayList<Bitmap>();
        sprites.add(Utils.loadSprite(R.raw.bloodanim0, resources, SCALE_FACTOR));

        sprites.add(Utils.loadSprite(R.raw.bloodanim1, resources, SCALE_FACTOR));

        sprites.add(Utils.loadSprite(R.raw.bloodanim2, resources, SCALE_FACTOR));

        sprites.add(Utils.loadSprite(R.raw.bloodanim3, resources, SCALE_FACTOR));

        sprites.add(Utils.loadSprite(R.raw.bloodanim4, resources, SCALE_FACTOR));

        sprites.add(Utils.loadSprite(R.raw.bloodanim5, resources, SCALE_FACTOR));
    }

    public void draw(Canvas canvas, Paint paint) {
        if (stop) return;
        canvas.drawBitmap(getFrame(), x - getFrame().getWidth() / 2, y - getFrame().getHeight() / 2, paint);
        frameIndex++;
        if (frameIndex > sprites.size() - 1) {
            stop = true;
        }
    }

    public boolean checkAlive() {
        return !stop;
    }
}
