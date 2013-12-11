package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class Cannon extends Enemy {

	public Cannon(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 14, TileSack.MASK_SPRITE | 15, 
				TileSack.MASK_SPRITE | 16, TileSack.MASK_SPRITE | 17);
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(0, 4);
	}
}
