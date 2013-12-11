package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class HTank extends Enemy {

	public HTank(TileSack sack, int... frameIds) {
		super(sack, frameIds);
	}

	public HTank(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 20, TileSack.MASK_SPRITE | 21);
	}

	@Override
	public void advance(Sprite player) {
		super.advance(player);

		updatePos(2, 4);
	}
}
