package com.krld.patient.game.model.effects;

import android.graphics.*;
import com.krld.patient.game.model.Unit;

public abstract class Effect {
    long birthDate;
    protected long duration;
    protected Unit owner;

    protected static float yCorrection = -15;

    protected Effect(Unit owner) {
        this.owner = owner;
        birthDate = owner.context.getTick();
        duration = 25;
    }

    public void postEffect() {
    }

    public void effect() {
    }

    public void draw(Canvas canvas, Paint paint) {
    }

    public boolean checkEffectTime() {
        if (owner.context.getTick() - birthDate > duration) {
            return false;
        } else return true;
    }
}
