package org.akop.airjag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.SparseArray;

public class TileSack {

	public static final int MASK_TILE   = 0x100000;
	public static final int MASK_SPRITE = 0x200000;

	private class BitmapHolder {
		public Bitmap mBmp;
		public int mWidth;
		public int mHeight;
	}

	private Context mContext;
	private SparseArray<BitmapHolder> mBitmaps;
	private int mDensity;

	public TileSack(Context context) {
		mContext = context;
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

		mDensity = (int)metrics.density;
		mBitmaps = new SparseArray<BitmapHolder>();
	}

	public void addTile(int keyId, int tileResId) {
		BitmapHolder holder = new BitmapHolder();
		holder.mBmp = BitmapFactory.decodeResource(mContext.getResources(), tileResId);
		holder.mWidth = holder.mBmp.getWidth() / mDensity;
		holder.mHeight = holder.mBmp.getHeight() / mDensity;

		mBitmaps.put(keyId, holder);
	}

	public Bitmap getTile(int keyId) {
		BitmapHolder holder = mBitmaps.get(keyId);
		if (holder == null)
			return null;

		return holder.mBmp;
	}

	public int getWidth(int keyId) {
		BitmapHolder holder = mBitmaps.get(keyId);
		if (holder == null)
			return 0;

		return holder.mWidth;
	}

	public int getHeight(int keyId) {
		BitmapHolder holder = mBitmaps.get(keyId);
		if (holder == null)
			return 0;

		return holder.mHeight;
	}

	public void destroy() {
		for (int i = 0, n = mBitmaps.size(); i < n; i++) {
			int key = mBitmaps.keyAt(i);
			mBitmaps.get(key).mBmp.recycle();
		}

		mBitmaps.clear();
	}
}
