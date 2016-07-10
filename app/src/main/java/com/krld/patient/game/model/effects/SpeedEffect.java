package com.krld.patient.game.model.effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Player;
import com.krld.patient.game.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class SpeedEffect extends Effect {
    List<Point> positions;

    public SpeedEffect(Unit owner) {
        super(owner);
        positions = new ArrayList<Point>();
        durationTime = 1f;
    }

    public void draw(Canvas canvas, Paint paint, GameCamera camera) {
        int step = 145;
        if (positions.size() != 0)
            step = 155 / positions.size();
        int alpha = 0;
        for (Point pos : positions) {
            alpha += step;
            paint.setAlpha(alpha);
            Bitmap bitmap = owner.getBitmap();
            canvas.drawBitmap(bitmap, pos.x - bitmap.getWidth() / 2 - camera.getX(), pos.y - bitmap.getHeight() / 2f - camera.getY(), paint);
        }
        paint.setAlpha(255);
    }

    public void postEffect() {
        owner.speed = Player.SPEED;
    }

    public void doEffect(float delta) {
        super.doEffect(delta);
        positions.add(new Point((int) owner.x, (int) owner.y));
        if (positions.size() > 5) {
            positions.remove(0);
        }
        owner.speed = Player.SPEED * 2;
    }
}
