package org.akop.airjag.model;

import java.util.Comparator;

import org.akop.airjag.AirJag;
import org.akop.airjag.TileSack;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;


public abstract class Sprite {

	public static final Comparator<Sprite> ZIndexComparator = new Comparator<Sprite>() {
		@Override
		public int compare(Sprite lhs, Sprite rhs) {
			return lhs.getZIndex() - rhs.getZIndex();
		}
	};

	private TileSack mSack;
	private int mFrameIds[];
	private static int sDensity = -1;

	private int mFrameCount;
	private int mCurrentFrame;

	protected int mWidth;
	protected int mHeight;

	protected int mX;
	protected int mY;

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

	public boolean needsPlacement() {
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

	public boolean isInCollision(Sprite player) {
		return false;
	}

	public int getZIndex() {
		return 0;
	}
}
