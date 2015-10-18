package com.krld.patient.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.krld.patient.R;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Background;
import com.krld.patient.game.model.Player;
import com.krld.patient.game.model.Unit;
import com.krld.patient.game.model.bullets.Bullet;
import com.krld.patient.game.model.effects.Effect;

import java.util.List;

import static com.krld.patient.game.UIConstants.*;

public class GameRenderer {
    public static final String TAP_TO_RESTART = "Tap to restart!";
    private final GameView gameView;
    private Background background;
    public static final String BEST_TEXT = "BEST: ";
    public static final String SCORE_TEXT = "SCORE: ";
    private Player player;
    private Bitmap heartBitmap;
    private Bitmap gameOverSprite;

    private boolean textInGameOverInited = false;
    private TextDrawValues textDrawValues;
    private GameCamera camera;
    private Paint paint = new Paint();

    public GameRenderer(GameView gameView) {
        this.gameView = gameView;
    }

    public void drawMain(Canvas canvas, GameCamera camera) {
        this.camera = camera;
        this.player = gameView.getPlayer();

        if (gameView.isGameOver()) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, gameView.getGameWidth(), gameView.getGameHeight(), paint);
            canvas.drawBitmap(gameOverSprite, (gameView.getGameWidth()) / 2 - gameOverSprite.getWidth() / 2, 20, paint);
            drawGameOverScores(canvas, paint);
            if (System.currentTimeMillis() - gameView.getGameOverTime() > GameView.DELAY_GAME_OVER_SKIP) {
                drawTapToRestart(canvas, paint);
            }
        } else {
            background.update(gameView.decals);
            drawBackground(paint, canvas);
            drawEntities(canvas, paint);
            drawUI(canvas, paint);
            drawGradient(canvas, paint);
        }
    }

    private void drawBackground(Paint paint, Canvas canvas) {
        int xBackground = 0;
        int yBackground = 0;
        int size = 1;
        for (int x = -size; x <= size; x++) {
            for (int y = -size; y <= size; y++) {
                xBackground = x * background.getMainBitmap().getWidth();
                yBackground = y * background.getMainBitmap().getHeight();
                canvas.drawBitmap(background.getBitmap(x + 1, y + 1), xBackground - camera.getX(), yBackground - camera.getY(), paint);
            }
        }

    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public Background getBackground() {
        return background;
    }

    private void drawEntities(Canvas canvas, Paint paint) {
        drawDrawable(gameView.getDrawBonuses(), canvas, paint);
        drawDrawable(gameView.getDrawCreeps(), canvas, paint);
        drawBullets(gameView.getDrawBullets(), canvas, paint);
        drawPlayer(canvas, paint);
        drawDrawable(gameView.getDrawAnimations(), canvas, paint);
    }

    private void drawBullets(List<Bullet> drawBullets, Canvas canvas, Paint paint) {
        for (Bullet bullet : drawBullets) {
            bullet.draw(canvas, paint, camera);
        }
    }

    private void drawDrawable(List<? extends Unit> drawables, Canvas canvas, Paint paint) {
        if (drawables == null) {
            return;
        }
        for (Unit drawable : drawables) {
            if (!drawable.isVisible()) continue;
            Bitmap bitmap = drawable.getBitmap();
            canvas.drawBitmap(bitmap, drawable.x - camera.getX(), drawable.y - camera.getY(), paint);
        }
    }

    private void drawPlayer(Canvas canvas, Paint paint) {
        Bitmap bitmap = player.getBitmap();
        canvas.drawBitmap(bitmap, camera.getWidth() / 2 - bitmap.getWidth() / 2, camera.getHeight() / 2 - bitmap.getHeight() / 2, paint);
        for (Effect effect : player.effects) {
            effect.draw(canvas, paint, camera);
        }
    }

    private void drawUI(Canvas canvas, Paint paint) {
        drawHp(canvas, paint);
        drawLives(canvas, paint);
        drawScores(canvas, paint);
    }

    private void drawTapToRestart(Canvas canvas, Paint paint) {
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setTextSize(33);
        paint.setStyle(Paint.Style.FILL);

        float textWidth = paint.measureText(TAP_TO_RESTART);
        float x = gameView.getGameWidth() / 2 - textWidth / 2;
        float y = gameView.getGameHeight() / 2;

        canvas.drawText(TAP_TO_RESTART, x, y, paint);
        paint.setAntiAlias(false);
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
        float textWidthScore = paint.measureText(SCORE_TEXT);
        float leftScore = gameView.getGameWidth() / 2 - textWidthScore / 1;
        float textWidthBest = paint.measureText(BEST_TEXT);
        float leftBest = gameView.getGameWidth() / 2 - textWidthBest / 1;

        float top = (gameView.getGameHeight() / 5) * 4;
        textDrawValues.setLeftScore(leftScore);
        textDrawValues.setLeftBest(leftBest);
        textDrawValues.setTop(top);


        canvas.drawText(SCORE_TEXT + String.valueOf(gameView.getScore()), textDrawValues.getLeftScore(), textDrawValues.getTop(), paint);
        canvas.drawText(BEST_TEXT + String.valueOf(gameView.getBestScore()), textDrawValues.getLeftBest(), textDrawValues.getTop() + SCORE_TOP_MARGIN * 1.3f, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawText(SCORE_TEXT + String.valueOf(gameView.getScore()), textDrawValues.getLeftScore(), textDrawValues.getTop(), paint);
        canvas.drawText(BEST_TEXT + String.valueOf(gameView.getBestScore()), textDrawValues.getLeftBest(), textDrawValues.getTop() + SCORE_TOP_MARGIN * 1.3f, paint);
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
        canvas.drawText(SCORE_TEXT + String.valueOf(gameView.getScore()), SCORE_LEFT_MARGIN, SCORE_TOP_MARGIN, paint);
        canvas.drawText(BEST_TEXT + String.valueOf(gameView.getBestScore()), SCORE_LEFT_MARGIN, SCORE_TOP_MARGIN * 2, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawText(SCORE_TEXT + String.valueOf(gameView.getScore()), SCORE_LEFT_MARGIN, SCORE_TOP_MARGIN, paint);
        canvas.drawText(BEST_TEXT + String.valueOf(gameView.getBestScore()), SCORE_LEFT_MARGIN, SCORE_TOP_MARGIN * 2, paint);
        paint.setAntiAlias(false);
    }

    private void drawLives(Canvas canvas, Paint paint) {
        float top = gameView.getGameHeight() - HP_BAR_MARGIN - heartBitmap.getHeight();
        for (int i = 0; i < player.lives; i++) {
            canvas.drawBitmap(heartBitmap, HP_BAR_MARGIN * 2 + HP_BAR_WIDTH + i * (heartBitmap.getWidth() + heartBitmap.getWidth() / 4), top, paint);
        }
    }

    private void drawHp(Canvas canvas, Paint paint) {
        float hpStart = gameView.getGameHeight() - HP_BAR_MARGIN;
        float hpEnd = hpStart - HP_BAR_HEIGHT;
        paint.setColor(Color.rgb(55, 0, 0));
        canvas.drawRect(HP_BAR_MARGIN, hpEnd, HP_BAR_WIDTH, hpStart, paint);
        paint.setColor(Color.RED);
        float multiplayer = player.hp / (player.maxHp * 1f);
        canvas.drawRect(HP_BAR_MARGIN, hpStart - (hpStart - hpEnd) * multiplayer, HP_BAR_WIDTH, hpStart, paint);
    }

    private void drawGradient(Canvas canvas, Paint paint) {
        paint.setColor(Color.rgb(100, 10, 88));
        int height = 20;
        paint.setStrokeWidth(height);
        for (int y = 0, alpha = 130; alpha > 2; alpha -= 15, y
                += height) {
            paint.setAlpha(alpha);
            canvas.drawLine(0, y, 550, y, paint);
            if (y > 960) break;
        }
        paint.setAlpha(255);

        paint.setColor(Color.BLACK);
        // canvas.drawText(debugMessage, 20, 20, paint);
    }

    public void init(Resources resources) {
        heartBitmap = Utils.loadSprite(R.raw.heart, resources);
        gameOverSprite = Utils.loadSprite(R.raw.gameover, resources, 20);
    }
}
