package com.krld.patient;

import android.graphics.*;

import java.util.*;

public class SpeedEffect extends Effect {
    List<Point> positions;

    SpeedEffect(Unit owner) {
        super(owner);
        positions = new ArrayList<Point>();
    }

    public void draw(Canvas canvas, Paint paint) {
        int step = 145;
        if (positions.size() != 0)
            step = 155 / positions.size();
        int alpha = 0;
        for (Point pos : positions) {
            alpha += step;
            paint.setAlpha(alpha);
            canvas.drawBitmap(Player.sprite, pos.x - Player.sprite.getWidth(), pos.y - Player.sprite.getHeight() / 1.5f, paint);
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
