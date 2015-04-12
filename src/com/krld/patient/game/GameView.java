package com.krld.patient.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.*;
import android.graphics.*;
import android.util.Log;
import android.view.*;

import java.util.*;

import com.krld.patient.ActiveView;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.*;
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

public class GameView extends SurfaceView implements ActiveView {
	public static final float WIDTH_BASIS = 540f;
	static final String TAG = "PLOG";
	public static final int DEFAULT_SCALE_FACTOR = 6;
	public static final int DEFAULT_SCALE_FACTOR_FOR_BONUS = 4;
	public static final int NURSE_SPAWN_COOLDOWN = 6000;
	private static final String BEST_SCORE_KEY = "BEST_SCORE_KEY";
	public static final int TIME_BETWEEN_TICKS = 50;
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


	public GameView(Context context) {
		super(context);
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				updateSurface();
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
		Log.i(TAG, "INIT");
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
		runner = new Thread(new Runnable() {
			public void run() {
				updateLoop();
			}
		}
		);
		runner.start();
	}

	private void updateLoop() {
		long time;
		long delay;
		while (true) {
			if (this.gameOver) {
				return;
			}
			this.update();
			time = System.currentTimeMillis();
			this.updateSurface();
			if (this.gameOver) {
				return;
			}
			try {
				delay = TIME_BETWEEN_TICKS - (System.currentTimeMillis() - time);
				Thread.sleep((delay < 0 ? 0 : delay));
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	@SuppressLint("WrongCall")
	private void updateSurface() {
		if (!holder.getSurface().isValid()) {
			return;
		}
		Canvas canvas = holder.lockCanvas();
		drawGame(canvas);
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

	public void drawGame(Canvas canvas) {
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


	private void update() {
		try {
			if (gameOver)
				return;
			tick++;
			gameContentUpdate();
			moveUnits();
			player.move();
			player.collect();
			updateDrawCollections();
			checkGameOver();
		} catch (Exception e) {
			debugMessage = Utils.getExceptionContent(e);
		}
	}

	private void updateDrawCollections() {
		drawBonuses = new ArrayList<Unit>(bonuses);
		drawCreeps = new ArrayList<Unit>(creeps);
		drawBullets = new ArrayList<Bullet>(bullets);
		drawAnimations = new ArrayList<Unit>(animations);
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
		editor.apply();
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
			if (bullet.achieveTarget() || bullet.touchPlayer() || bullet.touchObstacle()
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
			Log.i(TAG, "Runner has terminated");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void onResume() {
		if (runner != null && !runner.isAlive()) {
			createAndStartRunner();
			Log.i(TAG, "Runner has started");
		} else {
			updateSurface();
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
		return newX >= 0 && newX<= WIDTH_BASIS && newY >= 0 && newY <= gameHeight;
	}

	private class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			//Log.d(TAG, "Mouse event! " + event.getAction());
			float x = event.getX() / canvasScale + camera.getX();
			float y = event.getY() / canvasScale + camera.getY();
			player.moveTo(x, y);
			if (gameOver) {
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
