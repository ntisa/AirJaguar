package org.akop.airjag.model;

import org.akop.airjag.AirJag;
import org.akop.airjag.TileSack;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;


public abstract class Sprite {

	private TileSack mSack;
	private int mFrameIds[];
	private static int sDensity = -1;

	private int mFrameCount;
	private int mCurrentFrame;

	private int mWidth;
	private int mHeight;

	private int mX;
	private int mY;

	private boolean mIsKilled;

	public Sprite(TileSack sack, int... frameIds) {
		mIsKilled = false;
		mSack = sack;
		mFrameIds = frameIds;
		mFrameCount = mFrameIds.length;
		mCurrentFrame = 0;

		if (sDensity < 0) {
			DisplayMetrics metrics = AirJag.getInstance().getResources().getDisplayMetrics();
			sDensity = (int) metrics.density;
		}

		if (frameIds.length > 0) {
			mWidth = sack.getWidth(frameIds[0]);
			mHeight = sack.getHeight(frameIds[0]);
		}
	}

	public boolean needsHorizontalPlacement() {
		return true;
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

	public void updatePos(int xDelta, int yDelta) {
		mX += xDelta;
		mY += yDelta;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	protected static int scale(int size) {
		return size * sDensity;
	}

	public void advance(Sprite player) {
		if (++mCurrentFrame >= mFrameCount)
			mCurrentFrame = 0;
	}

	public boolean isKilled() {
		return mIsKilled;
	}

	public void kill() {
		mIsKilled = true;
	}

	public void render(Canvas canvas) {
		Bitmap bmp = mSack.getTile(mFrameIds[mCurrentFrame]);
		if (bmp != null)
			canvas.drawBitmap(bmp, scale(mX), scale(mY), null);
	}

	public boolean isInCollision(Sprite sprite) {
		int dx = sprite.mX - mX;
		int dy = sprite.mY - mY;

		return dx < mWidth && dx > -mWidth && dy < mWidth && dy > -mWidth;
	}

	public int getZIndex() {
		return 0;
	}
}
