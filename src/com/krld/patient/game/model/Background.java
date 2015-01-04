package com.krld.patient.game.model;

import android.content.res.*;
import android.graphics.*;
import com.krld.patient.*;
import com.krld.patient.game.Drawable;
import com.krld.patient.game.Utils;
import com.krld.patient.game.model.decals.Decal;

import java.util.*;

public class Background {
	public static List<Bitmap> sprites;
	private final float width;
	private final float height;

	byte[][] tileMap;

	private static int tileSize;

	private int xSize;

	private int ySize;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mPaint;

	public Background(float width, float height) {
		this.width = width;
		this.height = height;
		xSize = (int) width / tileSize + 1;
		ySize = (int) height / tileSize + 1;
		tileMap = new byte[xSize][ySize];
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				tileMap[x][y] = 0;
				if (Math.random() > 0.9)
					tileMap[x][y] = (byte) (Math.random() * 5);
			}
		}
		createBitmap();
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(mBitmap, 0, 0, paint);
	}

	public static void init(Resources resources) {
		sprites = new ArrayList<Bitmap>();
		int scale = 6;
		Bitmap bitmap = Utils.loadSprite(R.raw.tile, resources, scale);
		sprites.add(bitmap);
		sprites.add(Utils.loadSprite(R.raw.tile2, resources, scale));
		sprites.add(Utils.loadSprite(R.raw.tile3, resources, scale));
		sprites.add(Utils.loadSprite(R.raw.tile4, resources, scale));
		sprites.add(Utils.loadSprite(R.raw.tile5, resources, scale));
		tileSize = (short) (sprites.get(0).getWidth());

	}

	private void createBitmap() {
		mBitmap = Bitmap.createBitmap(xSize * tileSize, ySize * tileSize, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mCanvas.drawColor(Color.GRAY);
		mPaint = new Paint();
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				mCanvas.drawBitmap(sprites.get(tileMap[x][y]), x * tileSize, y * tileSize, mPaint);
			}
		}
		mPaint.setColor(Color.GRAY);
		mPaint.setAlpha(190);
		mCanvas.drawRect(0, 0, width, height, mPaint);
		mPaint.setAlpha(255);
	}

	public void update(List<Decal> drawables) {
		if (drawables == null) {
			return;
		}
		Iterator iter = drawables.iterator();
		while (iter.hasNext()) {
			Drawable drawable = (Drawable) iter.next();
			drawable.draw(mCanvas, mPaint);
		}
		drawables.clear();
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}
}
