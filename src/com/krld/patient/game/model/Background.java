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
	private Canvas mainCanvas;
	private Paint mPaint;
	private Bitmap[][] bitmaps;

	public Background(float width, float height) {
		this.width = width;
		this.height = height;
		xSize = (int) width / tileSize + 1;
		ySize = (int) height / tileSize + 1;
		generateTileMap();

		bitmaps = new Bitmap[3][3];
		for (int x = 0; x <= 2; x++) {
			for (int y = 0; y <= 2; y++) {
				Bitmap newBitmap = null;
				if (y == 1 && x == 1) {
					newBitmap = createMainBitmap();
				} else {
				   newBitmap = createEnivormentBitmaps(x, y);
				}
				bitmaps[x][y] = newBitmap;
			}
		}
	}

	private void generateTileMap() {
		tileMap = new byte[xSize][ySize];
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				tileMap[x][y] = 0;
				if (Math.random() > 0.9)
					tileMap[x][y] = (byte) (Math.random() * 5);
			}
		}
	}

	private Bitmap createEnivormentBitmaps(int globalX, int globalY) {

		Bitmap bitmap = Bitmap.createBitmap(xSize * tileSize, ySize * tileSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.GRAY);
		mPaint = new Paint();
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				canvas.drawBitmap(sprites.get(tileMap[0][0]), x * tileSize, y * tileSize, mPaint);
			}
		}
		drawDimmedRect(canvas);
		return bitmap;
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

	private Bitmap createMainBitmap() {

		Bitmap bitmap = Bitmap.createBitmap(xSize * tileSize, ySize * tileSize, Bitmap.Config.ARGB_8888);
		mainCanvas = new Canvas(bitmap);
		mainCanvas.drawColor(Color.GRAY);
		mPaint = new Paint();
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				mainCanvas.drawBitmap(sprites.get(tileMap[x][y]), x * tileSize, y * tileSize, mPaint);
			}
		}
		drawDimmedRect(mainCanvas);

		return bitmap;
	}

	private void drawDimmedRect(Canvas canvas) {
		mPaint.setColor(Color.GRAY);
		mPaint.setAlpha(190);
		canvas.drawRect(0, 0, xSize * tileSize, ySize * tileSize, mPaint);
		mPaint.setAlpha(255);
	}

	public void update(List<Decal> drawables) {
		if (drawables == null) {
			return;
		}
		for (Object drawable1 : drawables) {
			Drawable drawable = (Drawable) drawable1;
			drawable.draw(mainCanvas, mPaint);
		}
		drawables.clear();
	}

	public Bitmap getMainBitmap() {
		return bitmaps[1][1];
	}

	public Bitmap getBitmap(int x, int y) {
		return bitmaps[x][y];
	}
}
