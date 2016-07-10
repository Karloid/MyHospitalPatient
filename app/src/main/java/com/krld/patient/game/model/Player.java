package com.krld.patient.game.model;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;
import com.krld.patient.game.model.animations.BloodAnimation;
import com.krld.patient.game.model.decals.BloodSpot;
import com.krld.patient.game.model.effects.Effect;
import com.krld.patient.game.model.effects.ShieldEffect;

import java.util.ArrayList;
import java.util.List;

public class Player extends Unit {
    private static Bitmap spriteAlive;
    private static Bitmap spriteDead;
    private float decayDmg;

    public int lives;

    public List<Effect> effects;

    public static int scale;

    public static final float SPEED = 210;

    // death related
    private float deadDurationMax;
    private boolean isDead;
    private long deadAtTime;
    private float deadDuration;

    public Player(float x, float y, GameView context) {
        super(x, y, context);
        speed = SPEED;
        hp = 55;
        decayDmg = 1;
        lives = 3;
        deadDurationMax = 3;

        effects = new ArrayList<Effect>();
        effects.add(new ShieldEffect(this));
    }

    public void move(float delta) {
        super.move(delta);
        decay(delta);
        processEffects(delta);
        checkHp(delta);
    }

    @Override
    public void moveTo(float x, float y) {
        if (!isDead)
            super.moveTo(x, y);
    }

    private void processEffects(float delta) {
        List<Effect> effectsToRemove = new ArrayList<Effect>();
        for (Effect effect : effects) {
            effect.doEffect(delta);
            if (!effect.isEndEffectTime()) {
                effect.postEffect();
                effectsToRemove.add(effect);
            }
        }
        effects.removeAll(effectsToRemove);
    }

    private void checkHp(float delta) {
        if (hp == 0) {
            if (isDead) {
                deadDuration += delta;
                if (deadDuration > deadDurationMax) {
                    resurrect(delta);
                }
            } else {
                die();
            }
        }
    }

    private void die() {
        isDead = true;
        deadAtTime = System.currentTimeMillis();
        deadDuration = 0;
        moveX = null;
        moveY = null;
        createDeadDecalsAndAnimations();
    }

    private void resurrect(float delta) {
        lives--;
        hp = maxHp;
        effects.add(new ShieldEffect(this));
        isDead = false;
    }

    private void decay(float delta) {
        damage(delta * decayDmg);
    }

    public static void init(Resources resources) {
        spriteAlive = Utils.loadSprite(R.raw.player_alive, resources);
        spriteDead = Utils.loadSprite(R.raw.player_dead, resources);
    }

    @Override
    public void damage(float dmg) {
        for (Effect effect : effects) {
            if (effect instanceof ShieldEffect)
                return;
        }
        if (dmg > 6)
            context.decals.add(new BloodSpot(x + (float) Math.random() * 80 - 40, y + (float) Math.random() * 80 - 40, context));
        super.damage(dmg);
    }

    @Override
    public Bitmap getBitmap() {
        return isDead ? spriteDead : spriteAlive;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }

    public void createDeadDecalsAndAnimations() {
        for (int i = 0; i < 10; i++)
            context.decals.add(new BloodSpot(x + (float) Math.random() * 80 - 40, y + (float) Math.random() * 80 - 40, context));
        for (int i = 0; i < 14; i++)
            context.animations.add(new BloodAnimation(x + (float) Math.random() * 80 - 40, y + (float) Math.random() * 80 - 40, context));
    }
}
