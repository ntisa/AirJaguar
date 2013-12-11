package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class Jet extends Enemy {

	private int mCounter;

	public Jet(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 26, TileSack.MASK_SPRITE | 27);

		mCounter = 0;
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		++mCounter;

		int yDelta = 0;
		if (mCounter < 30)
			yDelta = 2;
		else if (mCounter > 60)
			yDelta = 6;

		int xDelta = 0;
		if (getX() < player.getX())
			xDelta = 2;
		else if (getX() > player.getX())
			xDelta = -2;

		updatePos(xDelta, yDelta);
	}

	@Override
	public int getZIndex() {
		return 100;
	}
}
