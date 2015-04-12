package com.krld.patient.game.model;

import android.content.res.*;
import android.graphics.*;
import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;
import com.krld.patient.game.model.decals.BloodSpot;
import com.krld.patient.game.model.effects.Effect;
import com.krld.patient.game.model.effects.ShieldEffect;

import java.util.*;

public class Player extends Unit {
    public static Bitmap sprite;

    private float decayDmg;

    public int lives;

    public List<Effect> effects;

    public static int scale;

    public static final float SPEED = 210;

    public Player(float x, float y, GameView context) {
        super(x, y, context);
        speed = SPEED;
        hp = 55;
        decayDmg = 1;
        lives = 3;

        effects = new ArrayList<Effect>();
        effects.add(new ShieldEffect(this));
    }

    public void move(float delta) {
        super.move(delta);
        decay();
        processEffects();
        checkHp();

    }

    private void processEffects() {
        List<Effect> effectsToRemove = new ArrayList<Effect>();
        for (Effect effect : effects) {
            effect.effect();
            if (!effect.checkEffectTime()) {
                effect.postEffect();
                effectsToRemove.add(effect);
            }
        }
        effects.removeAll(effectsToRemove);
    }

    private void checkHp() {
        if (hp == 0) {
            lives--;
            hp = maxHp;
            effects.add(new ShieldEffect(this));
            x = 250;
            y = 300;
        }
    }

    private void decay() {
        if (Math.random() < 0.3f)
            damage(decayDmg);
    }

    public static void init(Resources resources) {
        sprite = Utils.loadSprite(R.raw.player, resources);
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
        return sprite;
    }
}
