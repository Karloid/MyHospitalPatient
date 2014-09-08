package com.krld.patient.game;

import android.graphics.*;

public abstract class Effect {
    long birthDate;
    long duration;
    Unit owner;

    static float yCorrection = -15;

    Effect(Unit owner) {
        this.owner = owner;
        birthDate = System.currentTimeMillis();
        duration = 3000;
    }

    public void postEffect() {

    }

    public void effect() {

    }

    public void draw(Canvas canvas, Paint paint) {
    }

    public boolean checkEffectTime() {
        if (System.currentTimeMillis() - birthDate > duration) {
            return false;
        } else return true;
    }
}