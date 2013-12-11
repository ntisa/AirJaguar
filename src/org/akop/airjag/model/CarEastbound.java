package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class CarEastbound extends Sprite {

	public CarEastbound(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 4);

		setPos(-32, -32); // FIXME: hardcoded
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(2, 4);
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
