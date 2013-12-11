package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class VTank extends Enemy {

	public VTank(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 18, TileSack.MASK_SPRITE | 19);
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(0, 2);
	}
}
