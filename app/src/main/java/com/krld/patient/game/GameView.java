package com.krld.patient.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import com.krld.patient.ActiveView;
import com.krld.patient.Application;
import com.krld.patient.R;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Background;
import com.krld.patient.game.model.Player;
import com.krld.patient.game.model.Unit;
import com.krld.patient.game.model.animations.Animation;
import com.krld.patient.game.model.animations.BloodAnimation;
import com.krld.patient.game.model.animations.CloudAnimation;
import com.krld.patient.game.model.bonuses.*;
import com.krld.patient.game.model.bullets.Bullet;
import com.krld.patient.game.model.bullets.Needle;
import com.krld.patient.game.model.bullets.Note;
import com.krld.patient.game.model.creeps.Creep;
import com.krld.patient.game.model.creeps.Doctor;
import com.krld.patient.game.model.creeps.Nurse;
import com.krld.patient.game.model.decals.BloodSpot;
import com.krld.patient.game.model.decals.BombSpot;
import com.krld.patient.game.model.decals.Decal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameView extends SurfaceView implements ActiveView {
    public static final float WIDTH_BASIS = 540f;
    public static final int DEFAULT_SCALE_FACTOR = 6;
    public static final int DEFAULT_SCALE_FACTOR_FOR_BONUS = 4;
    public static final int NURSE_SPAWN_COOLDOWN = 6000;
    private static final String BEST_SCORE_KEY = "BEST_SCORE_KEY";
    public static final int TIME_BETWEEN_TICKS = 50;
    static final long DELAY_GAME_OVER_SKIP = 1000;
    private final SurfaceHolder holder;

    public Player player;

    public List<Bonus> bonuses;
    public List<Creep> creeps;
    public List<Bullet> bullets;
    public List<Decal> decals;
    public List<Animation> animations;

    public int score;

    private boolean gameOver;

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

    private GameRenderer gameRenderer;
    private long tick;
    private List<Unit> drawDecals;
    private List<Unit> drawBonuses;
    private List<Unit> drawCreeps;
    private List<Bullet> drawBullets;
    private List<Unit> drawAnimations;
    private GameCamera camera;

    private long lastSpawnBonuses;
    private long gameOverTime;


    public GameView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                updateSurface(0);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        setOnTouchListener(new MyOnTouchListener());
        gameRenderer = new GameRenderer(this);
    }

    private void initGame() {
        Log.i(Application.TAG, "INIT");
        debugMessage = "";
        score = 0;
        tick = 0;
        loadBestScore();

        background = new Background(WIDTH_BASIS, gameHeight);
        gameRenderer.setBackground(background);
        gameOver = false;
        player = new Player(WIDTH_BASIS / 2, gameHeight / 2, this);
        bonuses = new LinkedList<Bonus>();
        creeps = new LinkedList<Creep>();
        nurseSpawnCoolDown = NURSE_SPAWN_COOLDOWN;
        bullets = new LinkedList<Bullet>();
        decals = new LinkedList<Decal>();
        animations = new LinkedList<Animation>();
        //	final MediaPlayer mp = MediaPlayer.create(context, R.raw.s1);
        createAndStartRunner();
    }

    private void createAndStartRunner() {
        if (runner != null) {
            runner.interrupt();
        }
        runner = new Thread(this::updateLoop);
        runner.start();
    }

    private void updateLoop() {
        long currentTime;
        long lastTime = System.currentTimeMillis();
        float delta;
        long delay;
        while (true) {

            currentTime = System.currentTimeMillis();
            delta = (currentTime - lastTime) / 1000f;
            lastTime = currentTime;
            if (!gameOver) {
                this.update(delta);
            }
            this.updateSurface(delta);
            try {
                delay = TIME_BETWEEN_TICKS - (System.currentTimeMillis() - currentTime);
                Thread.sleep((delay < 0 ? 0 : delay));
            } catch (InterruptedException e) {
                Log.d(Application.TAG, "stopped");
                return;
            }
        }
    }

    @SuppressLint("WrongCall")
    private void updateSurface(float delta) {
        if (!holder.getSurface().isValid()) {
            return;
        }
        Canvas canvas = holder.lockCanvas();
        drawGame(canvas, delta);
        holder.unlockCanvasAndPost(canvas);
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

        gameRenderer.init(getResources());
    }

    public void drawGame(Canvas canvas, float delta) {
        fitCanvas(canvas);
        camera.setX(player.x - camera.getWidth() / 2);
        camera.setY(player.y - camera.getHeight() / 2);

        gameRenderer.drawMain(canvas, camera);
    }

    private void init() {
        initSprites();
        initGame();
        firstRun = false;
    }

    private void fitCanvas(Canvas canvas) {
        if (!canvasScaleInited) {
            canvasScale = canvas.getWidth() / WIDTH_BASIS;
            canvasScaleInited = true;
            gameHeight = canvas.getHeight() / canvasScale;
            camera = new GameCamera();
            camera.setWidth(WIDTH_BASIS);
            camera.setHeight(gameHeight);
            init();
        }
        if (canvasScale != 1f)
            canvas.scale(canvasScale, canvasScale);
    }


    private void update(float delta) {
        try {
            if (gameOver)
                return;
            tick++;
            gameContentUpdate(delta);
            moveMainUnits(delta);
            player.move(delta);
            player.collect(delta);
            updateDrawCollections(delta);
            checkGameOver(delta);
        } catch (Exception e) {
            e.printStackTrace();
            debugMessage = Utils.getExceptionContent(e);
        }
    }

    private void updateDrawCollections(float delta) {
        drawBonuses = new ArrayList<Unit>(bonuses);
        drawCreeps = new ArrayList<Unit>(creeps);
        drawBullets = new ArrayList<Bullet>(bullets);
        drawAnimations = new ArrayList<Unit>(animations);
    }

    private void gameContentUpdate(float delta) {
        spawnBonuses(delta);
        spawnCreeps(delta);
    }

    private void spawnCreeps(float delta) {
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

    private void checkGameOver(float delta) {
        if (player.lives == 0) {
            gameOver = true;
            gameOverTime = System.currentTimeMillis();
            if (score >= bestScore) {
                bestScore = score;
                saveBestScore();
            }

            if (!Application.getAllScores().isNewRecord(score)) {
                return;
            }
            post(() -> {
                View layout = LayoutInflater.from(getContext()).inflate(R.layout.new_record, null);
                EditText editText = (EditText) layout.findViewById(R.id.edit_text);
                editText.setText(Application.getLastPlayerName());
                new AlertDialog.Builder(getContext()).setView(layout)
                        .setTitle(R.string.new_record_enter_your_name)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            Application.getAllScores().saveScore(editText.getText().toString(), score);
                        })
                        .setOnCancelListener(dialog1 -> Application.getAllScores().saveScore(editText.getText().toString(), score))
                        .create()
                        .show();
            });
        }
    }

    private void saveBestScore() {
        SharedPreferences preferences = ((Activity) getContext()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(BEST_SCORE_KEY, bestScore);
        editor.apply();
    }

    private void moveMainUnits(float delta) {
        removeCreeps(delta);
        removeBullets(delta);
        moveUnits(delta, animations);
    }

    private void moveUnits(float delta, List<? extends Unit> units) {
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
    }

    private void removeBullets(float delta) {
        List<Bullet> bulletsToRemove = new ArrayList<Bullet>();
        long currentTimeMillis = System.currentTimeMillis();
        for (Bullet bullet : bullets) {
            bullet.move(delta);
            if (bullet.achieveTarget() || bullet.touchPlayer() || bullet.touchObstacle()
                    || currentTimeMillis - bullet.getBirthDate() > Needle.lifeTime)
                bulletsToRemove.add(bullet);
        }
        for (Bullet bullet : bulletsToRemove) {
            bullet.postAction();
        }
        bullets.removeAll(bulletsToRemove);
    }

    private void removeCreeps(float delta) {
        List<Creep> creepsToRemove = null;
        for (Creep creep : creeps) {
            creep.move(delta);
            if (creep.needRemove()) {
                if (creepsToRemove == null) {
                    creepsToRemove = new ArrayList<Creep>();
                }
                creepsToRemove.add(creep);
            }
        }

        if (creepsToRemove != null) {
            creeps.removeAll(creepsToRemove);
        }
    }

    private void spawnBonuses(float delta) {   //TODO rework
        long spawnBonusesDelay = 50;
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastSpawnBonuses > spawnBonusesDelay) {
            lastSpawnBonuses = currentTimeMillis;
        } else {
            return;
        }
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

    public List<Decal> getDecals() {
        return decals;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public List<Creep> getCreeps() {
        return creeps;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Animation> getAnimations() {
        return animations;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public float getGameHeight() {
        return gameHeight;
    }

    public float getGameWidth() {
        return WIDTH_BASIS;
    }

    public int getScore() {
        return score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void onPause() {
        saveBestScore();
        runner.interrupt();
        try {
            runner.join();
            Log.i(Application.TAG, "Runner has terminated");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        if (runner != null && !runner.isAlive()) {
            createAndStartRunner();
            Log.i(Application.TAG, "Runner has started");
        } else {
            updateSurface(0);
        }
    }

    public long getTick() {
        return tick;
    }

    public void increaseScore(int reward) {
        score += reward;
        if (score > getBestScore()) {
            setBestScore(score);
        }
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public boolean checkLegalPosition(float newX, float newY, Unit unit) {
        boolean legalX = newX >= 0 && newX <= background.getRealWidth();
        boolean legalY = newY >= 0 && newY <= background.getRealHeight();
        return legalX && legalY;
    }

    public long getGameOverTime() {
        return gameOverTime;
    }

    private class MyOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            //Log.d(TAG, "Mouse event! " + event.getAction());
            float x = event.getX() / canvasScale + camera.getX();
            float y = event.getY() / canvasScale + camera.getY();
            player.moveTo(x, y);
            if (gameOver && System.currentTimeMillis() - gameOverTime > DELAY_GAME_OVER_SKIP) {
                gameOver = false;
                initGame();
            }
            return true;
        }
    }

    public List<Unit> getDrawDecals() {
        return drawDecals;
    }

    public List<Unit> getDrawBonuses() {
        return drawBonuses;
    }

    public List<Unit> getDrawCreeps() {
        return drawCreeps;
    }

    public List<Bullet> getDrawBullets() {
        return drawBullets;
    }

    public List<Unit> getDrawAnimations() {
        return drawAnimations;
    }
}
