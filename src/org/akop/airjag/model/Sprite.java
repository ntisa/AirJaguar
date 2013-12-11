package org.akop.airjag.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;


public abstract class Sprite {
	private Context mContext;
	private Bitmap[] mFrameBitmaps;
	private static int sDensity = -1;

	private int mFrameCount;
	private int mCurrentFrame;

	private int mWidth;
	private int mHeight;

	private int mX;
	private int mY;

	public Sprite(Context context, int... frameResIds) {
		mContext = context;

		if (sDensity < 0) {
			DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
			sDensity = (int)metrics.density;
		}

		initResources(frameResIds);
	}

	private void initResources(int... frameResIds) {
		mFrameCount = frameResIds.length;
		mCurrentFrame = 0;

		if (mFrameCount > 0) {
			mFrameBitmaps = new Bitmap[mFrameCount];
			for (int i = 0; i < mFrameCount; i++) {
				mFrameBitmaps[i] = BitmapFactory.decodeResource(mContext.getResources(),
						frameResIds[i]);
			}
	
			mWidth = mFrameBitmaps[0].getWidth() / sDensity;
			mHeight = mFrameBitmaps[0].getHeight() / sDensity;
		}
	}

	public void setPos(int x, int y) {
		mX = x;
		mY = y;
	}

	public int getX() {
		return mX;
	}

	public void setX(int x) {
		mX = x;
	}

	public int getY() {
		return mY;
	}

	public void setY(int y) {
		mY = y;
	}

	public void incrementY(int byHowMuch) {
		mY += byHowMuch;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	private int scale(int size) {
		return size * sDensity;
	}

	public void render(Canvas canvas) {
		canvas.drawBitmap(mFrameBitmaps[mCurrentFrame], 
				scale(mX), scale(mY), null);

		if (++mCurrentFrame >= mFrameCount)
			mCurrentFrame = 0;
	}

	public void destroy() {
		for (Bitmap bmp: mFrameBitmaps)
			bmp.recycle();
	}
}
