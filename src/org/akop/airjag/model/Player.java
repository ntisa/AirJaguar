package org.akop.airjag.model;

import org.akop.airjag.TileSack;



public class Player extends Sprite {

	public Player(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 0, TileSack.MASK_SPRITE | 1);
	}
}
