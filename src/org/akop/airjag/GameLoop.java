package org.akop.airjag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
	private boolean mRunning;
	private SurfaceHolder mSurfaceHolder;
	private Bitmap mGameBitmap;
	private Canvas mGameCanvas;
	private Context mContext;
	private int mDensityFactor;

	private static final int mTileSets[][] = {
		{ R.drawable.tileset0_00, 
			R.drawable.tileset0_01, 
			R.drawable.tileset0_02, 
			R.drawable.tileset0_03, 
			R.drawable.tileset0_04, 
			R.drawable.tileset0_05, 
			R.drawable.tileset0_06, 
			R.drawable.tileset0_07, 
			R.drawable.tileset0_08, 
			R.drawable.tileset0_09, 
			R.drawable.tileset0_10, 
			R.drawable.tileset0_11, 
			R.drawable.tileset0_12, 
			R.drawable.tileset0_13, 
			R.drawable.tileset0_14, 
			R.drawable.tileset0_15, 
			R.drawable.tileset0_16, 
			R.drawable.tileset0_17, 
			R.drawable.tileset0_18, 
			R.drawable.tileset0_19, 
			R.drawable.tileset0_20, 
			R.drawable.tileset0_21, 
			R.drawable.tileset0_22, 
			R.drawable.tileset0_23, 
		},
		{ R.drawable.tileset1_00, 
			R.drawable.tileset1_01, 
			R.drawable.tileset1_02, 
			R.drawable.tileset1_03, 
			R.drawable.tileset1_04, 
			R.drawable.tileset1_05, 
			R.drawable.tileset1_06, 
			R.drawable.tileset1_07, 
			R.drawable.tileset1_08, 
			R.drawable.tileset1_09, 
			R.drawable.tileset1_10, 
			R.drawable.tileset1_11, 
			R.drawable.tileset1_12, 
			R.drawable.tileset1_13, 
			R.drawable.tileset1_14, 
			R.drawable.tileset1_15, 
			R.drawable.tileset1_16, 
			R.drawable.tileset1_17, 
			R.drawable.tileset1_18, 
			R.drawable.tileset1_19, 
			R.drawable.tileset1_20, 
			R.drawable.tileset1_21, 
			R.drawable.tileset1_22, 
			R.drawable.tileset1_23, 
		},
		{ R.drawable.tileset2_00, 
			R.drawable.tileset2_01, 
			R.drawable.tileset2_02, 
			R.drawable.tileset2_03, 
			R.drawable.tileset2_04, 
			R.drawable.tileset2_05, 
			R.drawable.tileset2_06, 
			R.drawable.tileset2_07, 
			R.drawable.tileset2_08, 
			R.drawable.tileset2_09, 
			R.drawable.tileset2_10, 
			R.drawable.tileset2_11, 
			R.drawable.tileset2_12, 
			R.drawable.tileset2_13, 
			R.drawable.tileset2_14, 
			R.drawable.tileset2_15, 
			R.drawable.tileset2_16, 
			R.drawable.tileset2_17, 
			R.drawable.tileset2_18, 
			R.drawable.tileset2_19, 
			R.drawable.tileset2_20, 
			R.drawable.tileset2_21, 
			R.drawable.tileset2_22, 
			R.drawable.tileset2_23, 
		},
		{ R.drawable.tileset3_00, 
			R.drawable.tileset3_01, 
			R.drawable.tileset3_02, 
			R.drawable.tileset3_03, 
			R.drawable.tileset3_04, 
			R.drawable.tileset3_05, 
			R.drawable.tileset3_06, 
			R.drawable.tileset3_07, 
			R.drawable.tileset3_08, 
			R.drawable.tileset3_09, 
			R.drawable.tileset3_10, 
			R.drawable.tileset3_11, 
			R.drawable.tileset3_12, 
			R.drawable.tileset3_13, 
			R.drawable.tileset3_14, 
			R.drawable.tileset3_15, 
			R.drawable.tileset3_16, 
			R.drawable.tileset3_17, 
			R.drawable.tileset3_18, 
			R.drawable.tileset3_19, 
			R.drawable.tileset3_20, 
			R.drawable.tileset3_21, 
			R.drawable.tileset3_22, 
			R.drawable.tileset3_23, 
		},
		{ R.drawable.tileset4_00, 
			R.drawable.tileset4_01, 
			R.drawable.tileset4_02, 
			R.drawable.tileset4_03, 
			R.drawable.tileset4_04, 
			R.drawable.tileset4_05, 
			R.drawable.tileset4_06, 
			R.drawable.tileset4_07, 
			R.drawable.tileset4_08, 
			R.drawable.tileset4_09, 
			R.drawable.tileset4_10, 
			R.drawable.tileset4_11, 
			R.drawable.tileset4_12, 
			R.drawable.tileset4_13, 
			R.drawable.tileset4_14, 
			R.drawable.tileset4_15, 
			R.drawable.tileset4_16, 
			R.drawable.tileset4_17, 
			R.drawable.tileset4_18, 
			R.drawable.tileset4_19, 
			R.drawable.tileset4_20, 
			R.drawable.tileset4_21, 
			R.drawable.tileset4_22, 
			R.drawable.tileset4_23, 
		}
	};

	private SparseArray<Bitmap> mTiles;

	public GameLoop(Context context, SurfaceHolder holder) {
		mContext = context;
		mRunning = false;
		mSurfaceHolder = holder;
		mTiles = new SparseArray<Bitmap>();

		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		mDensityFactor = (int)metrics.density;

		initResources();
	}

	private void initResources() {
		mGameBitmap = Bitmap.createBitmap(256 * mDensityFactor, 
				352 * 2 * mDensityFactor, Bitmap.Config.ARGB_8888);
		mGameCanvas = new Canvas(mGameBitmap);

		Log.v("*", "Loading resources");

		int tileset[] = mTileSets[0];
		for (int i = 0, n = tileset.length; i < n; i++) {
			mTiles.put(i, BitmapFactory.decodeResource(mContext.getResources(),
					tileset[i]));
		}

		Log.v("*", "Done");
	}

	public void stopGameLoop() {
		mRunning = false;
	}

	public void doShit() {
		for (int i = 0; i < 8; i++)
			mGameCanvas.drawBitmap(mTiles.get(5), i * 32 * mDensityFactor, i, null);
	}

	@Override
	public void run() {
		mRunning = true;

		while (mRunning) {
			Canvas canvas = null;
			try {
				doShit();
				canvas = mSurfaceHolder.lockCanvas();
				synchronized (mSurfaceHolder) {
					if (canvas != null) {
					    canvas.drawBitmap(mGameBitmap, 0, 0, null);
					}
				}
			} finally {
				if (canvas != null)
					mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
		}

		Log.v("*", "Game loop exited");
	}

	public void cleanUp() {
		mGameBitmap.recycle();

		for (int i = 0, n = mTiles.size(); i < n; i++) {
			int key = mTiles.keyAt(i);
			mTiles.get(key).recycle();
		}
	}
}
