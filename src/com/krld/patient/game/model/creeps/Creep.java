package com.krld.patient.game.model.creeps;

import com.krld.patient.game.GameView;
import com.krld.patient.game.model.animations.BloodAnimation;
import com.krld.patient.game.model.animations.CloudAnimation;
import com.krld.patient.game.model.Unit;
import com.krld.patient.game.model.decals.BloodSpot;

public abstract class Creep extends Unit {

    private static final int REWARD = 0;

    public Creep(float x, float y, GameView context) {
        super(x, y, context);
        birth();
    }

    public void birth() {
        for (int i = 0; i < 10; i++)
            context.animations.add(new CloudAnimation(x + (float) Math.random() * 80 - 40, y + (float) Math.random() * 80 - 40, context));
    }

    public boolean needRemove() {
        return false;
    }

    public void attackPlayer() {

    }

    public void createCorpse() {
        for (int i = 0; i < 5; i++)
            context.decals.add(new BloodSpot(x + (float) Math.random() * 80 - 40, y + (float) Math.random() * 80 - 40, context));
        for (int i = 0; i < 14; i++)
            context.animations.add(new BloodAnimation(x + (float) Math.random() * 80 - 40, y + (float) Math.random() * 80 - 40, context));


    }

    public void die() {
        context.increaseScore(getReward());
        createCorpse();
    }

    public int getReward() {
        return REWARD;
    }
}
