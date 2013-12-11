package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class Enemy extends Sprite {

	public Enemy(TileSack sack, int... frameIds) {
		super(sack, frameIds);
	}

	@Override
	public boolean isInCollision(Sprite player) {
		int dx = player.mX - mX;
		int dy = player.mY - mY;

		return dx < mWidth && dx > -mWidth && dy < mWidth && dy > -mWidth;
	}
}
