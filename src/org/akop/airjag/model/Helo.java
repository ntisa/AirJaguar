package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class Helo extends Enemy {

	public Helo(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 12, TileSack.MASK_SPRITE | 13);
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(0, 4);
	}

	@Override
	public int getZIndex() {
		return 100;
	}
}
