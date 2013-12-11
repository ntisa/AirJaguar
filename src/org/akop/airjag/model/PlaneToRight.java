package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class PlaneToRight extends Enemy {

	public PlaneToRight(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 28);

		setPos(-32, -32); // FIXME: hardcoded
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(4, 4);
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
