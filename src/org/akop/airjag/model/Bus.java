package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class Bus extends Sprite {

	public Bus(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 7);

		setPos(-32, -32); // FIXME: hardcoded
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(4, 4);
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
