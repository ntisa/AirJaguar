package org.akop.airjag.model;

import org.akop.airjag.TileSack;


public class SmallTank extends HTank {

	public SmallTank(TileSack sack) {
		super(sack, TileSack.MASK_SPRITE | 24, TileSack.MASK_SPRITE | 25);
	}
}
