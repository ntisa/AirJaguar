package org.akop.airjag.model;

import org.akop.airjag.R;

import android.content.Context;
import android.graphics.Canvas;


public class RedPlane extends Sprite {
	public RedPlane(Context context) {
		super(context, R.drawable.objects_10, R.drawable.objects_11);
	}

	@Override
	public void render(Canvas canvas) {
		super.render(canvas);

		incrementY(8);
	}
}
