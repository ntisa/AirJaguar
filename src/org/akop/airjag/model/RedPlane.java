package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class RedPlane extends Enemy {

	public RedPlane(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 10, TileSack.MASK_SPRITE | 11);
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(2, 8);
	}

	@Override
	public int getZIndex() {
		return 100;
	}
}
