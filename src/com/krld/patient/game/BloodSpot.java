package com.krld.patient.game;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.*;
import com.krld.patient.R;

import java.util.ArrayList;
import java.util.List;

public class BloodSpot extends Decal {
    private static final int SCALE_FACTOR = 3;
    public static Bitmap sprite;
    private static List<Bitmap> sprites;

    private byte type;

    private static Bitmap sprite1;

    private static Bitmap sprite2;

    private static Bitmap sprite3;

    private static Bitmap sprite4;
    private float rotare;

    BloodSpot(float x, float y, GameView context) {
        super(x, y, context);
        type = (byte) (Math.random() * 5);
        rotare = Math.round(360 * Math.random());
    }

    public void draw(Canvas canvas, Paint paint) {
        Bitmap bitmap = sprites.get(type);
        paint.setAlpha(200);
        Utils.drawBitmapRotate(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, rotare, canvas, paint);
        paint.setAlpha(255);
    }

    public static void init(Resources resources) {
        sprites = new ArrayList<Bitmap>();
        sprites.add(Utils.loadSprite(R.raw.bloodspot, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.bloodspot1, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.bloodspot2, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.bloodspot3, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.bloodspot4, resources, SCALE_FACTOR));
    }

}