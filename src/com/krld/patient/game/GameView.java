package com.krld.patient.game;

import android.app.Activity;
import android.content.*;
import android.graphics.*;
import android.util.Log;
import android.view.*;

import java.util.*;

import com.krld.patient.R;

import static com.krld.patient.game.UIConstants.*;

public class GameView extends View {
    public static final float WIDTH_BASIS = 540f;
    public static final String BEST_TEXT = "BEST: ";
    private static final String TAG = "PLOG";
    public static final int DEFAULT_SCALE_FACTOR = 6;
    public static final int DEFAULT_SCALE_FACTOR_FOR_BONUS = 4;
    public static final int NURSE_SPAWN_COOLDOWN = 6000;
    private static final String BEST_SCORE_KEY = "BEST_SCORE_KEY";
    public static final String SCORE_TEXT = "SCORE: ";
    public Player player;

    public List<Bonus> bonuses;
    public List<Creep> creeps;
    public List<Bullet> bullets;
    public List<Decal> decals;
    public List<Animation> animations;

    public int score;

    Bitmap heartBitmap;

    private boolean gameOver;

    private Bitmap gameOverSprite;

    private Thread runner;

    private float canvasScale = 1;

    private long lastNurseSpawnTime;

    private long nurseSpawnCoolDown;

    public static String debugMessage;

    private Background background;
    private boolean canvasScaleInited;
    private boolean firstRun = true;
    private float gameHeight;
    private int bestScore;
    private boolean textInGameOverInited = false;
    private TextDrawValues textDrawValues;


    public GameView(Context context) {
        super(context);
        setOnTouchListener(new MyOnTouchListener());
    }

    private void initGame() {
        Log.i(TAG, "INIT");
        debugMessage = "";
        score = 0;
        loadBestScore();

        background = new Background(WIDTH_BASIS, gameHeight);
        final GameView game = this;
        gameOver = false;
        player = new Player(WIDTH_BASIS / 2, gameHeight / 2, this);
        bonuses = new ArrayList<Bonus>();
        creeps = new ArrayList<Creep>();
        nurseSpawnCoolDown = NURSE_SPAWN_COOLDOWN;
        bullets = new ArrayList<Bullet>();
        decals = new ArrayList<Decal>();
        animations = new ArrayList<Animation>();
        //	final MediaPlayer mp = MediaPlayer.create(context, R.raw.s1);
        runner = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (game.gameOver)
                        return;
                    game.update();
                    game.postInvalidate();
                    if (game.gameOver)
                        return;
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }
        );
        runner.start();
    }

    private void loadBestScore() {
        SharedPreferences preferences = ((Activity) getContext()).getPreferences(Context.MODE_PRIVATE);
        bestScore = preferences.getInt(BEST_SCORE_KEY, 0);
    }

    private void initSprites() {
        Player.init(getResources());
        Nurse.init(getResources());
        Medkit.init(getResources());
        Needle.init(getResources());
        ShieldBonus.init(getResources());
        BombBonus.init(getResources());
        BloodSpot.init(getResources());
        BombSpot.init(getResources());
        Doctor.init(getResources());
        Note.init(getResources());
        BloodAnimation.init(getResources());
        SpeedBonus.init(getResources());
        Background.init(getResources());
        CloudAnimation.init(getResources());

        heartBitmap = Utils.loadSprite(R.raw.heart, getResources());
        gameOverSprite = Utils.loadSprite(R.raw.gameover, getResources(), 20);
    }

    @Override
    public void onDraw(Canvas canvas) {
        fitCanvas(canvas);
        Paint paint = new Paint();
        drawBackground(paint, canvas);
        try {
            drawEntitys(canvas, paint);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage() + " " + e.getCause());
            e.printStackTrace();
            debugMessage = Utils.getExceptionContent(e);
        }
        drawUI(canvas, paint);
        drawMisc(canvas, paint);
    }

    private void init() {
        initSprites();
        initGame();
        firstRun = false;
    }

    private void drawMisc(Canvas canvas, Paint paint) {
        paint.setColor(Color.rgb(100, 10, 88));
        paint.setStrokeWidth(40);
        for (int y = 0, alpha = 130; alpha > 2; alpha -= 15, y
                += 40) {
            paint.setAlpha(alpha);
            canvas.drawLine(0, y, 550, y, paint);
            if (y > 960) break;
        }
        paint.setAlpha(255);

        paint.setColor(Color.BLACK);
        canvas.drawText(debugMessage, 20, 20, paint);
    }

    private void drawBackground(Paint paint, Canvas canvas) {
        background.draw(canvas, paint);
    }

    private void fitCanvas(Canvas canvas) {
        if (!canvasScaleInited) {
            canvasScale = canvas.getWidth() / WIDTH_BASIS;
            canvasScaleInited = true;
            gameHeight = canvas.getHeight() / canvasScale;
            init();
        }
        if (canvasScale != 1f)
            canvas.scale(canvasScale, canvasScale);
    }

    private void drawEntitys(Canvas canvas, Paint paint) {
        for (Decal decal : decals) {
            decal.draw(canvas, paint);
        }
        for (Bonus bonus : bonuses) {
            bonus.draw(canvas, paint);
        }
        for (Creep creep : creeps) {
            creep.draw(canvas, paint);
        }
        for (Bullet needle : bullets) {
            needle.draw(canvas, paint);
        }
        drawPlayer(canvas, paint);
        for (Animation anim : animations) {
            anim.draw(canvas, paint);
        }

    }

    private void update() {
        try {
            if (gameOver)
                return;
            gameContentUpdate();
            moveUnits();
            player.move();
            player.collect();
            checkGameOver();
        } catch (Exception e) {
            debugMessage = Utils.getExceptionContent(e);
        }
    }

    private void gameContentUpdate() {
        spawnBonuses();
        spawnCreeps();
    }

    private void spawnCreeps() {
        if (creeps.size() < 50 && System.currentTimeMillis() - lastNurseSpawnTime > nurseSpawnCoolDown) {
            if (Math.random() > 0.3f) {
                creeps.add(new Nurse((float) (20 + Math.random() * 500),
                        (float) (20 + Math.random() * 690), this));
            } else {
                creeps.add(new Doctor((float) (20 + Math.random() * 500),
                        (float) (20 + Math.random() * 690), this));
            }

            lastNurseSpawnTime = System.currentTimeMillis();
        }
    }

    private void checkGameOver() {
        if (player.lives == 0) {
            gameOver = true;
            if (score > bestScore) {
                bestScore = score;
                saveBestScore();
            }
        }
    }

    private void saveBestScore() {
        SharedPreferences preferences = ((Activity) getContext()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(BEST_SCORE_KEY, bestScore);
        editor.commit();
    }

    private void moveUnits() {
        List<Creep> creepToRemove = null;
        for (Creep creep : creeps) {
            creep.move();
            if (creep.needRemove()) {
                if (creepToRemove == null) {
                    creepToRemove = new ArrayList<Creep>();
                }
                creepToRemove.add(creep);
            }
        }

        if (creepToRemove != null) {
            creeps.removeAll(creepToRemove);
        }

        List<Bullet> bulletsToRemove = new ArrayList<Bullet>();
        long currentTimeMillis = System.currentTimeMillis();
        for (Bullet bullet : bullets) {
            bullet.move();
            if (bullet.achieveTarget() || bullet.touchPlayer()
                    || currentTimeMillis - bullet.getBirthDate() > Needle.lifeTime)
                bulletsToRemove.add(bullet);
        }
        for (Bullet bullet : bulletsToRemove) {
            bullet.postAction();
        }
        bullets.removeAll(bulletsToRemove);
    }

    private void spawnBonuses() {
        if (Math.random() < 0.0512f && bonuses.size() < 10)
            bonuses.add(new Medkit((float) (20 + Math.random() * 500),
                    (float) (20 + Math.random() * 690), this));

        if (Math.random() < 0.0112f && bonuses.size() < 10)
            bonuses.add(new ShieldBonus((float) (20 + Math.random() * 500),
                    (float) (20 + Math.random() * 690), this));

        if (Math.random() < 0.0112f && bonuses.size() < 10)
            bonuses.add(new SpeedBonus((float) (20 + Math.random() * 500),
                    (float) (20 + Math.random() * 690), this));

        if (Math.random() < 0.0052f * creeps.size() && bonuses.size() < 10)
            bonuses.add(new BombBonus((float) (20 + Math.random() * 500),
                    (float) (20 + Math.random() * 690), this));
    }

    private void drawUI(Canvas canvas, Paint paint) {
        drawHp(canvas, paint);
        drawLives(canvas, paint);
        drawScores(canvas, paint);
        if (gameOver) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, WIDTH_BASIS, gameHeight, paint);
            canvas.drawBitmap(gameOverSprite, (WIDTH_BASIS) / 2 - gameOverSprite.getWidth() / 2, 20, paint);
            drawGameOverScores(canvas, paint);
        }
    }

    private void drawGameOverScores(Canvas canvas, Paint paint) {
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        if (!textInGameOverInited) {
            textDrawValues = new TextDrawValues();
            textInGameOverInited = true;
        }
        float textWidthScore = paint.measureText(SCORE_TEXT );
        float leftScore = WIDTH_BASIS / 2 - textWidthScore / 1;
        float textWidthBest = paint.measureText(BEST_TEXT );
        float leftBest = WIDTH_BASIS / 2 - textWidthBest / 1;

        float top = (gameHeight / 5) * 4;
        textDrawValues.setLeftScore(leftScore);
        textDrawValues.setLeftBest(leftBest);
        textDrawValues.setTop(top);


        canvas.drawText(SCORE_TEXT + String.valueOf(score), textDrawValues.getLeftScore(), textDrawValues.getTop(), paint);
        canvas.drawText(BEST_TEXT + String.valueOf(bestScore), textDrawValues.getLeftBest(), textDrawValues.getTop() + SCORE_TOP_MARGIN * 1.3f, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawText(SCORE_TEXT + String.valueOf(score), textDrawValues.getLeftScore(), textDrawValues.getTop(), paint);
        canvas.drawText(BEST_TEXT + String.valueOf(bestScore), textDrawValues.getLeftBest(), textDrawValues.getTop() + SCORE_TOP_MARGIN * 1.3f, paint);
        paint.setAntiAlias(false);
    }

    private void drawScores(Canvas canvas, Paint paint) {
        // Typeface typeFace = Typeface.create("Roboto", Typeface.BOLD);
        //  paint.setTypeface(typeFace);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawText(SCORE_TEXT + String.valueOf(score), SCORE_LEFT_MARGIN, SCORE_TOP_MARGIN, paint);
        canvas.drawText(BEST_TEXT + String.valueOf(bestScore), SCORE_LEFT_MARGIN, SCORE_TOP_MARGIN * 2, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawText(SCORE_TEXT + String.valueOf(score), SCORE_LEFT_MARGIN, SCORE_TOP_MARGIN, paint);
        canvas.drawText(BEST_TEXT + String.valueOf(bestScore), SCORE_LEFT_MARGIN, SCORE_TOP_MARGIN * 2, paint);
        paint.setAntiAlias(false);
    }

    private void drawLives(Canvas canvas, Paint paint) {
        float top = gameHeight - HP_BAR_MARGIN - heartBitmap.getHeight();
        for (int i = 0; i < player.lives; i++) {
            canvas.drawBitmap(heartBitmap, HP_BAR_MARGIN * 2 + HP_BAR_WIDTH + i * (heartBitmap.getWidth() + heartBitmap.getWidth() / 4), top, paint);
        }
    }

    private void drawHp(Canvas canvas, Paint paint) {
        float hpStart = gameHeight - HP_BAR_MARGIN;
        float hpEnd = hpStart - HP_BAR_HEIGHT;
        paint.setColor(Color.rgb(55, 0, 0));
        canvas.drawRect(HP_BAR_MARGIN, hpEnd, HP_BAR_WIDTH, hpStart, paint);
        paint.setColor(Color.RED);
        float multiplayer = player.hp / (player.maxHp * 1f);
        canvas.drawRect(HP_BAR_MARGIN, hpStart - (hpStart - hpEnd) * multiplayer, HP_BAR_WIDTH, hpStart, paint);
    }

    private void drawPlayer(Canvas canvas, Paint paint) {
        player.draw(canvas, paint);
    }

    private class MyOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            //Log.d(TAG, "Mouse event! " + event.getAction());
            float x = event.getX() / canvasScale;
            float y = event.getY() / canvasScale;
            player.moveTo(x, y);
            if (gameOver) {
                gameOver = false;
                initGame();
            }
            return true;
        }
    }
}
