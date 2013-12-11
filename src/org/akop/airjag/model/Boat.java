package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class Boat extends Enemy {

	public Boat(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 22, TileSack.MASK_SPRITE | 23);
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(0, 2);
	}
}
