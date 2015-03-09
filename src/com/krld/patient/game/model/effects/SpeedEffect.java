package com.krld.patient.game.model.effects;

import android.graphics.*;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.Player;
import com.krld.patient.game.model.Unit;

import java.util.*;

public class SpeedEffect extends Effect {
    List<Point> positions;

    public SpeedEffect(Unit owner) {
        super(owner);
        positions = new ArrayList<Point>();
    }

    public void draw(Canvas canvas, Paint paint, GameCamera camera) {
        int step = 145;
        if (positions.size() != 0)
            step = 155 / positions.size();
        int alpha = 0;
        for (Point pos : positions) {
            alpha += step;
            paint.setAlpha(alpha);
            canvas.drawBitmap(Player.sprite, pos.x - Player.sprite.getWidth() / 2 - camera.getX(), pos.y - Player.sprite.getHeight() / 2f  - camera.getY(), paint);
        }
        paint.setAlpha(255);
    }

    public void postEffect() {
        owner.speed = Player.SPEED;
    }

    public void effect() {
        positions.add(new Point((int) owner.x, (int) owner.y));
        if (positions.size() > 5) {
            positions.remove(0);
        }
        owner.speed = Player.SPEED * 2;
    }
}
