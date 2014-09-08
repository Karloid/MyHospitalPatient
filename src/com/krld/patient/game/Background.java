package com.krld.patient.game;

import android.content.res.*;
import android.graphics.*;
import com.krld.patient.*;

import java.util.*;

public class Background {
    public static List<Bitmap> sprites;
    private final float width;
    private final float height;

    byte[][] tileMap;

    private static int tileSize;

    private int xSize;

    private int ySize;

    Background(float width, float height) {
        this.width = width;
        this.height = height;
        xSize = (int) width / tileSize + 1;
        ySize = (int) height / tileSize + 1;
        tileMap = new byte[xSize][ySize];
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawColor(Color.GRAY);
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                canvas.drawBitmap(sprites.get(0), x * tileSize, y * tileSize, paint);
            }
        }
        paint.setColor(Color.GRAY);
        paint.setAlpha(190);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setAlpha(255);

    }

    public static void init(Resources resources) {
        sprites = new ArrayList<Bitmap>();
        int scale = 6;
        Bitmap bitmap = Utils.loadSprite(R.raw.tile, resources, scale);
        sprites.add(bitmap);
        tileSize = (short) (sprites.get(0).getWidth());

    }

}
