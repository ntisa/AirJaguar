package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class Canoe extends Sprite {

	public Canoe(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 2);
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(1, 4);
	}

	@Override
	public int getZIndex() {
		return 0;
	}
}
