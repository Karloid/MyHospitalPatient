package com.krld.patient;

import android.content.res.*;
import android.graphics.*;
import com.krld.patient.*;

import java.util.*;

public class Background {
    public static List<Bitmap> sprites;

    short[][] tileMap;

    private static int tileSize;

    private int xSize;

    private int ySize;

    Background() {
        xSize = 540 / tileSize + 1;
        ySize = 880 / tileSize + 1;
        tileMap = new short[xSize][ySize];
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                tileMap[x][y] = 0;
            }
        }

    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawColor(Color.GRAY);
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                canvas.drawBitmap(sprites.get(tileMap[x][y]), x * tileSize, y * tileSize, paint);
            }
        }
        paint.setColor(Color.GRAY);
        paint.setAlpha(190);
        canvas.drawRect(0, 0, 540, 900, paint);
        paint.setAlpha(255);

    }

    public static void init(Resources resources) {
        sprites = new ArrayList<Bitmap>();
        int scale = 4;
        sprites.add(Utils.loadSprite(R.raw.tile, resources, scale));
        tileSize = (short) (sprites.get(0).getWidth() * 2f);

    }

}
