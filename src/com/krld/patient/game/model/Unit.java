package com.krld.patient.game.model;

import android.content.res.*;
import android.graphics.*;
import com.krld.patient.game.Drawable;
import com.krld.patient.game.GameView;
import com.krld.patient.game.camera.GameCamera;
import com.krld.patient.game.model.bonuses.Bonus;

import java.util.*;

public abstract class Unit implements Drawable {

    public float x;

    public float y;

    protected Float moveX;
    protected Float moveY;

    public float speed;

    public int size;
    public int hp;
    public int maxHp;

    public GameView context;

    private static final float PICK_RANGE = 50;

    private static final int DECAY_DMG = 1;

    public Unit(float x, float y, GameView context) {
        this.x = x;
        this.y = y;
        this.context = context;
        size = 30;
        speed = 0;
        maxHp = 100;
        hp = maxHp;
    }

    public void draw(Canvas canvas, Paint paint, GameCamera camera) {
        paint.setColor(Color.RED);
        canvas.drawRect(x - size / 2 - camera.getX(), y - size / 2 - camera.getY(), x + size / 2, y + size / 2, paint);
    }
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.RED);
        canvas.drawRect(x - size / 2, y - size / 2, x + size / 2, y + size / 2, paint);
    }

    public void moveTo(float x, float y) {
        moveX = x;
        moveY = y;
    }

    public void move() {
        if (moveX == null || moveY == null || speed == 0)
            return;
        float tX = Math.abs(x - moveX) / speed;
        float tY = Math.abs(y - moveY) / speed;
        if (tX > tY) {
            x += (moveX - x) / tX;
            y += (moveY - y) / tX;
        } else {
            x += (moveX - x) / tY;
            y += (moveY - y) / tY;
        }
        if (Math.abs(x - moveX) < speed &&
                Math.abs(y - moveY) < speed) {
            moveX = null;
            moveY = null;
        }

    }

    public void collect() {
        List<Bonus> bonusToRemove = new ArrayList<Bonus>();
        for (Bonus bonus : context.bonuses) {
            if (Math.abs(bonus.x - x) < PICK_RANGE &&
                    Math.abs(bonus.y - y) < PICK_RANGE) {
                bonus.activate((Player) this);
                bonusToRemove.add(bonus);
            }
        }
        context.bonuses.removeAll(bonusToRemove);
    }

    public void heal(float value) {
        hp += value;
        if (hp > maxHp)
            hp = maxHp;
    }

    public void damage(float dmg) {
        hp -= dmg;    //TODO DEBUG MODE
        if (hp < 0)
            hp = 0;
    }

    public void attackPlayer() {

    }

    public static void init(Resources resources) {

    }

}
