package com.krld.patient.views;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.krld.patient.R;
import com.krld.patient.game.model.Point;
import com.krld.patient.game.model.Unit;
import com.krld.patient.game.model.animations.Animation;
import com.krld.patient.game.model.animations.CloudAnimation;

import java.util.ArrayList;
import java.util.List;

public class BackgroundView extends View implements View.OnTouchListener, ActiveView {
    public static final float CLOUD_RECREATE_RATIO = 0.4f;
    static final long BACKGROUND_DRAW_DELAY = 10;
    private List<Level> mLevels;
    private List<Animation> animations;
    private List<Animation> animationsCopy;
    private int currentTick;
    private Paint paint;
    private Point touchPoint;
    private Thread mDrawer;


    public int colorFade = Color.BLACK;
    public int colorEnd;
    public int colorLevel2;
    public int colorLevel3;


    public BackgroundView(Context context) {
        super(context);
        initDefaultColors();
        init();
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultColors();
        init();
    }

    public BackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initDefaultColors();
        init();
    }

    private void init() {
        animations = new ArrayList<>();
        CloudAnimation.init(getResources());
        setOnTouchListener(this);
        paint = new Paint();
    }

    private void initDefaultColors() {
        colorEnd = getResources().getColor(R.color.background_end);
        colorLevel2 = getResources().getColor(R.color.level_2);
        colorLevel3 = getResources().getColor(R.color.level_3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLevels == null) {
            initLevels();
        }
        drawEnd(canvas, paint);
        drawLevels(canvas, paint);
        drawDrawable(animationsCopy, canvas, paint);
        drawFade(canvas, paint);
        drawVersionCode(canvas, paint);
    }

    private void drawVersionCode(Canvas canvas, Paint paint) {
        try {
            String versionName = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
            paint.setTextSize(35);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(R.color.version_text_color));
            canvas.drawText(versionName, 0, canvas.getHeight() - 5, paint);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void drawFade(Canvas canvas, Paint paint) {
        int alphaLevel = 255 - currentTick;

        if (alphaLevel < 0) return;
        currentTick++;
        paint.setColor(colorFade);
        paint.setAlpha(alphaLevel);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
    }


    private void drawDrawable(List<? extends Unit> drawables, Canvas canvas, Paint paint) {
        if (drawables == null) {
            return;
        }
        for (Unit drawable : drawables) {
            if (!drawable.isVisible()) continue;
            Bitmap bitmap = drawable.getBitmap();
            canvas.drawBitmap(bitmap, drawable.x, drawable.y, paint);
        }
    }

    private void initLevels() {
        mLevels = new ArrayList<>();
        //	mLevels.add(new Level(getResources().getColor(R.color.level_1), 20, 20));
        mLevels.add(new Level(colorLevel2, 5, 86));
        mLevels.add(new Level(colorLevel3, 3, 130));
    }

    private void drawLevels(Canvas canvas, Paint paint) {
        for (Level level : mLevels) {
            level.draw(canvas, paint);
        }
    }

    private void drawEnd(Canvas canvas, Paint paint) {
        paint.setColor(colorEnd);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
    }

    public void update(float delta) {
        addNewAnimations(delta);
        moveUnits(delta, animations);
        animationsCopy = new ArrayList<>(animations);
        if (mLevels != null)
            for (Level level : mLevels) {
                level.update(delta);
            }
    }

    private void addNewAnimations(float delta) {
        Point currentTouchPoint = touchPoint;
        if (currentTouchPoint == null) return;
        touchPoint = null;
        int disp = 100;
        int dispDiv2 = disp / 2;
        for (int i = 0; i < 3; i++) {
            animations.add(new CloudAnimation(currentTouchPoint.getX() + (float) Math.random() * disp - dispDiv2,
                    currentTouchPoint.getY() + (float) Math.random() * disp - dispDiv2, null));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touchPoint = new Point(event.getX(), event.getY());
        return true;
    }

    private void moveUnits(float delta, List<? extends Unit> units) {
        try {

            List<Unit> unitsToRemove = null;
            for (Unit unit : units) {
                unit.move(delta);
                if (unit.needRemove()) {
                    if (unitsToRemove == null) {
                        unitsToRemove = new ArrayList<Unit>();
                    }
                    unitsToRemove.add(unit);
                }
            }

            if (unitsToRemove != null) {
                units.removeAll(unitsToRemove);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDrawerThread() {
        mDrawer = new Thread(this::drawerLoop);
        mDrawer.start();
    }

    private void drawerLoop() {
        long currentTime;
        long lastTime = System.currentTimeMillis();
        float delta;
        long delay;
        try {
            while (true) {
                currentTime = System.currentTimeMillis();
                delta = (currentTime - lastTime) / 1000f;
                lastTime = currentTime;

                update(delta);
                postInvalidate();
                Thread.sleep(BackgroundView.BACKGROUND_DRAW_DELAY);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        mDrawer.interrupt();
    }

    @Override
    public void onResume() {
        createDrawerThread();
    }

    @Override
    public View getView() {
        return this;
    }

    public void refreshColors() {
        initLevels();
    }


    private class Level {
        private final int mColor;
        private final int mCount;
        private final int mHeight;
        private List<Rectangle> mRectangles;

        public Level(int color, int count, int width) {
            mColor = color;
            mCount = count;
            mHeight = width;

            createRectangles();
        }

        private void createRectangles() {
            mRectangles = new ArrayList<Rectangle>();
            int space = (int) (getHeight() / ((mCount + 1) * 1f));
            for (int i = 0; i <= mCount; i++) {
                mRectangles.add(new Rectangle(i * space - mHeight));
            }
        }

        public void draw(Canvas canvas, Paint paint) {
            paint.setColor(colorFade);
            paint.setAlpha(100);
            for (Rectangle rect : mRectangles) {
                canvas.drawRect(0, rect.mY + mHeight / 4, canvas.getWidth(), rect.mY + mHeight + mHeight / 4, paint);
            }
            paint.setAlpha(255);
            paint.setColor(mColor);
            for (Rectangle rect : mRectangles) {
                canvas.drawRect(0, rect.mY, canvas.getWidth(), rect.mY + mHeight, paint);
            }
        }

        public void update(float delta) {
            for (Rectangle rect : mRectangles) {
                rect.mY += getSpeed();
                if (rect.mY > getHeight()) {
                    rect.mY = -mHeight - mHeight / 4;
                }
            }
        }

        private float getSpeed() {
            return mHeight / 300f;
        }
    }

    private class Rectangle {
        public float mY;

        public Rectangle(int y) {
            mY = y;
        }
    }
}
