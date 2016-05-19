package com.krld.patient.game.model.animations;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;

import java.util.ArrayList;
import java.util.List;

public class CloudAnimation extends Animation {

    public static final int SCALE_FACTOR = 4;

    public static List<Bitmap> sprites;

    public CloudAnimation(float x, float y, GameView context) {
        super(x, y, context);
    }

    @Override
    protected List<Bitmap> getSprites() {
        return sprites;
    }

    public static void init(Resources resources) {
        if (sprites != null) return;
        sprites = new ArrayList<Bitmap>();

        sprites.add(Utils.loadSprite(R.raw.cloud0, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.cloud1, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.cloud2, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.cloud3, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.cloud4, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.cloud5, resources, SCALE_FACTOR));
        sprites.add(Utils.loadSprite(R.raw.cloud6, resources, SCALE_FACTOR));
    }

    public void draw(Canvas canvas, Paint paint) {
        Bitmap frame = getBitmap();
        if (frame == null) return;
        canvas.drawBitmap(frame, x - frame.getWidth() / 2, y - frame.getHeight() / 2, paint);
    }
}
