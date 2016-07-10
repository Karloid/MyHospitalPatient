package com.krld.patient.game.model.bonuses;

import com.krld.patient.game.GameView;
import com.krld.patient.game.model.Player;
import com.krld.patient.game.model.Unit;

public abstract class Bonus extends Unit {
    protected Bonus(float x, float y, GameView context) {
        super(x, y, context);
    }

    public void activate(Player owner) {

    }
}
