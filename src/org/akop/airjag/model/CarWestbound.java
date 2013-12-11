package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class CarWestbound extends Sprite {

	public CarWestbound(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 3);

		setPos(256, -32); // FIXME: hardcoded
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(-2, 4);
	}

	@Override
	public boolean needsPlacement() {
		return false;
	}

	@Override
	public int getZIndex() {
		return 0;
	}
}
