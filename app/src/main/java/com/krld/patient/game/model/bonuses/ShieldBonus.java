package com.krld.patient.game.model.bonuses;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.krld.patient.R;
import com.krld.patient.game.GameView;
import com.krld.patient.game.Utils;
import com.krld.patient.game.model.Player;
import com.krld.patient.game.model.effects.ShieldEffect;

public class ShieldBonus extends Bonus {
	public static Bitmap sprite;

	public ShieldBonus(float x, float y, GameView context) {
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
		sprite = Utils.loadSprite(R.raw.shieldbonus, resources, GameView.DEFAULT_SCALE_FACTOR_FOR_BONUS);
	}

	public void activate(Player owner) {
		owner.effects.add(new ShieldEffect(owner));
	}
}