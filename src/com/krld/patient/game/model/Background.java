package com.krld.patient.game.model;

import android.content.res.*;
import android.graphics.*;
import com.krld.patient.*;
import com.krld.patient.game.Drawable;
import com.krld.patient.game.Utils;
import com.krld.patient.game.model.decals.Decal;

import java.util.*;

public class Background {
	public static final int COLUMN_0 = 0;
	public static final int COLUMN_1 = 1;
	public static final int COLUMN_2 = 2;
	public static final int ROW_0 = 0;
	public static final int ROW_1 = 1;
	public static final int ROW_2 = 2;

	public static List<Bitmap> mainSprites;
	private static List<Bitmap> wastelandSprites;
	private static List<Bitmap> wallVerticalSprites;
	private static List<Bitmap> wallTCornerLeftSprites;
	private static List<Bitmap> wallTCornerRightSprites;
	private static List<Bitmap> wallHorizontalSprites;
	private static List<Bitmap> wallLCornerLeftSprites;
	private static List<Bitmap> wallLCornerRightSprites;

	private final float width;
	private final float height;

	byte[][] tileMap;

	private static int tileSize;

	private int xSize;

	private int ySize;
	private Canvas mainCanvas;
	private Paint mPaint;
	private Bitmap[][] bitmaps;
	private int realWidth;
	private int realHeight;

	public static void init(Resources resources) {
		mainSprites = new ArrayList<Bitmap>();
		int scale = 6;
		Bitmap bitmap = Utils.loadSprite(R.raw.tile, resources, scale);
		mainSprites.add(bitmap);
		mainSprites.add(Utils.loadSprite(R.raw.tile2, resources, scale));
		mainSprites.add(Utils.loadSprite(R.raw.tile3, resources, scale));
		mainSprites.add(Utils.loadSprite(R.raw.tile4, resources, scale));
		mainSprites.add(Utils.loadSprite(R.raw.tile5, resources, scale));

		wastelandSprites = new ArrayList<Bitmap>();
		wastelandSprites.add(Utils.loadSprite(R.raw.tile_wasteland, resources, scale));
		wastelandSprites.add(Utils.loadSprite(R.raw.tile_wasteland2, resources, scale));
		wastelandSprites.add(Utils.loadSprite(R.raw.tile_wasteland3, resources, scale));
		wastelandSprites.add(Utils.loadSprite(R.raw.tile_wasteland4, resources, scale));
		wastelandSprites.add(Utils.loadSprite(R.raw.tile_wasteland5, resources, scale));
		wastelandSprites.add(Utils.loadSprite(R.raw.tile_wasteland6, resources, scale));
		wastelandSprites.add(Utils.loadSprite(R.raw.tile_wasteland7, resources, scale));

		wallVerticalSprites = new ArrayList<Bitmap>();
		wallVerticalSprites.add(Utils.loadSprite(R.raw.tile_wall_vertical, resources, scale));

		wallHorizontalSprites = new ArrayList<Bitmap>();
		wallHorizontalSprites.add(Utils.loadSprite(R.raw.tile_wall_horizontal, resources, scale));

		wallTCornerLeftSprites = new ArrayList<Bitmap>();
		wallTCornerLeftSprites.add(Utils.loadSprite(R.raw.tile_wall_t_corner, resources, scale));

		wallTCornerRightSprites = new ArrayList<Bitmap>();
		wallTCornerRightSprites.add(Utils.loadSprite(R.raw.tile_wall_t_corner_right, resources, scale));

		wallLCornerLeftSprites = new ArrayList<Bitmap>();
		wallLCornerLeftSprites.add(Utils.loadSprite(R.raw.tile_wall_l_corner_left, resources, scale));

		wallLCornerRightSprites = new ArrayList<Bitmap>();
		wallLCornerRightSprites.add(Utils.loadSprite(R.raw.tile_wall_l_corner_right, resources, scale));

		tileSize = (short) (mainSprites.get(0).getWidth());
	}

	public Background(float width, float height) {
		this.width = width;
		this.height = height;
		xSize = (int) width / tileSize + 1;
		ySize = (int) height / tileSize + 1;
		generateTileMap();

		realWidth = xSize * tileSize;
		realHeight = ySize * tileSize;

		bitmaps = new Bitmap[3][3];
		for (int x = 0; x <= 2; x++) {
			for (int y = 0; y <= 2; y++) {
				Bitmap newBitmap = null;
				if (y == 1 && x == 1) {
					newBitmap = createMainBitmap();
				} else {
					newBitmap = createWastelandsBitmaps(x, y);
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


	private Bitmap createWastelandsBitmaps(int globalX, int globalY) {

		Bitmap bitmap = Bitmap.createBitmap(xSize * tileSize, ySize * tileSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.GRAY);
		mPaint = new Paint();

		int maxX = xSize - 1;
		int maxY = ySize - 1;

		for (int x = 0; x <= maxX; x++) {
			for (int y = 0; y <= maxY; y++) {
				canvas.drawBitmap(wastelandSprites.get(randomIndex(wastelandSprites)), x * tileSize, y * tileSize, mPaint);
				switch (globalX) {
					case COLUMN_0:
						switch (globalY) {
							case ROW_0:
								if (x == maxX) {
									canvas.drawBitmap(wallVerticalSprites.get(randomIndex(wallVerticalSprites)), x * tileSize, y * tileSize, mPaint);
									if (y == maxY) {
										canvas.drawBitmap(wallTCornerLeftSprites.get(randomIndex(wallTCornerLeftSprites)), x * tileSize, y * tileSize, mPaint);
									}
								}
								break;
							case ROW_1:
								if (x == maxX)
									canvas.drawBitmap(wallVerticalSprites.get(randomIndex(wallVerticalSprites)), x * tileSize, y * tileSize, mPaint);
								break;
							case ROW_2:
								if (x == maxX && y == 0) {
									canvas.drawBitmap(wallLCornerLeftSprites.get(randomIndex(wallLCornerLeftSprites)), x * tileSize, y * tileSize, mPaint);
								}
								break;
						}
						break;
					case COLUMN_1:
						switch (globalY) {
							case ROW_0:
								canvas.drawBitmap(mainSprites.get(randomIndex(mainSprites)), x * tileSize, y * tileSize, mPaint);
								if (y == maxY) {
									canvas.drawBitmap(wallHorizontalSprites.get(randomIndex(wallHorizontalSprites)), x * tileSize, y * tileSize, mPaint);
								}
								break;
							case ROW_2:
								if (y == 0) {
									canvas.drawBitmap(wallHorizontalSprites.get(randomIndex(wallHorizontalSprites)), x * tileSize, y * tileSize, mPaint);
								}
								break;
						}
						break;
					case COLUMN_2:
						switch (globalY) {
							case ROW_0:
								if (x == 0) {
									canvas.drawBitmap(wallVerticalSprites.get(randomIndex(wallVerticalSprites)), x * tileSize, y * tileSize, mPaint);
									if (y == maxY) {
										canvas.drawBitmap(wallTCornerRightSprites.get(randomIndex(wallTCornerRightSprites)), x * tileSize, y * tileSize, mPaint);
									}
								}
								break;
							case ROW_1:
								if (x == 0) {
									canvas.drawBitmap(wallVerticalSprites.get(randomIndex(wallVerticalSprites)), x * tileSize, y * tileSize, mPaint);
								}
								break;
							case ROW_2:
								if (x == y && y == 0) {
									canvas.drawBitmap(wallLCornerRightSprites.get(randomIndex(wallLCornerRightSprites)), x * tileSize, y * tileSize, mPaint);
								}
								break;
						}
						break;
				}
			}
		}
		drawDimmedRect(canvas);
		return bitmap;
	}

	private int randomIndex(List<Bitmap> list) {
		return (int) (list.size() * Math.random());
	}

	private Bitmap createMainBitmap() {

		Bitmap bitmap = Bitmap.createBitmap(realWidth, realHeight, Bitmap.Config.ARGB_8888);
		mainCanvas = new Canvas(bitmap);
		mainCanvas.drawColor(Color.GRAY);
		mPaint = new Paint();
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				mainCanvas.drawBitmap(mainSprites.get(tileMap[x][y]), x * tileSize, y * tileSize, mPaint);
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

	public int getRealWidth() {
		return realWidth;
	}

	public int getRealHeight() {
		return realHeight;
	}
}
