package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class PlaneToLeft extends Enemy {

	public PlaneToLeft(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 29);

		setPos(256, -32);
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(-4, 4);
	}

	@Override
	public boolean needsHorizontalPlacement() {
		return false;
	}

	@Override
	public int getZIndex() {
		return 200;
	}
}
