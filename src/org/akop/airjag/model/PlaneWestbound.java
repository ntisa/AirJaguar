package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class PlaneWestbound extends Enemy {

	public PlaneWestbound(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 29);

		setPos(256, -32);
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(-4, 4);
	}

	@Override
	public boolean needsPlacement() {
		return false;
	}

	@Override
	public int getZIndex() {
		return 200;
	}
}
