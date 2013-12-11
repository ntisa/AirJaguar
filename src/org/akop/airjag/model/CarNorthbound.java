package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class CarNorthbound extends Sprite {

	private int mCounter;

	public CarNorthbound(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 5);

		mCounter = 0;
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		int yDelta = -2;
		if (++mCounter < 30)
			yDelta = 4;

		updatePos(0, yDelta);
	}

	@Override
	public int getZIndex() {
		return 0;
	}
}
