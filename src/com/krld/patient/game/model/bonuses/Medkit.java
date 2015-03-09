package com.krld.patient.game.model.bonuses;

import android.content.res.*;
import android.graphics.*;
import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;
import com.krld.patient.game.model.Player;
import com.krld.patient.game.model.bonuses.Bonus;

public class Medkit extends Bonus {
    float value = 15;
    public static Bitmap sprite;

    public Medkit(float x, float y, GameView context) {
        super(x, y, context);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, paint);
    }

    @Override
    public Bitmap getBitmap() {
        return sprite;
    }

    public static void init(Resources resources) {
        sprite = Utils.loadSprite(R.raw.medkit, resources, GameView.DEFAULT_SCALE_FACTOR_FOR_BONUS);

    }

    public void activate(Player owner) {
        owner.heal(value);
    }
}
