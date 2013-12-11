package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class TaxiingPlane extends Sprite {

	public TaxiingPlane(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 8);
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
