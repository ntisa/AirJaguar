package org.akop.airjag;

import org.akop.airjag.model.Sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {

	private static final int TILE_WIDTH_PX  = 32;
	private static final int TILE_HEIGHT_PX = 32;

	private static final int GAME_WIDTH = 8;
	private static final int GAME_HEIGHT = 11;

	private static final int GAME_WIDTH_PX  = GAME_WIDTH * TILE_WIDTH_PX;
	private static final int GAME_HEIGHT_PX = GAME_HEIGHT * TILE_HEIGHT_PX;

	private boolean mRunning;
	private SurfaceHolder mSurfaceHolder;
	private Bitmap mVirtualBitmap;
	private Bitmap mActualBitmap;
	private Canvas mVirtualCanvas;
	private Canvas mActualCanvas;
	private Context mContext;
	private int mDensity;

	private int mMapWidth;
	private int mMapHeight;

	private int mScaledWidth;
	private int mScaledHeight;

	private int mScreenWidth;
	private int mScreenHeight;

	private int mViewableTop;

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

	private int[][] mTileMap;
	private int mY;

	private Sprite mPlayer;

	public GameLoop(Context context, SurfaceHolder holder) {
		mContext = context;
		mRunning = false;
		mSurfaceHolder = holder;
		mTiles = new SparseArray<Bitmap>();

		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		mDensity = (int)metrics.density;

		mScaledWidth = scale(GAME_WIDTH_PX);
		mScaledHeight = scale(GAME_HEIGHT_PX);

		mScreenWidth = metrics.widthPixels;
		mScreenHeight = (int)((float)metrics.widthPixels 
				* ((float)GAME_HEIGHT_PX / (float)GAME_WIDTH_PX));

		mVirtualBitmap = Bitmap.createBitmap(mScaledWidth, 
				scale(GAME_HEIGHT_PX * 2 + TILE_HEIGHT_PX), 
				Bitmap.Config.ARGB_8888);
		mVirtualCanvas = new Canvas(mVirtualBitmap);

		mActualBitmap = Bitmap.createBitmap(mScaledWidth, mScaledHeight, 
				Bitmap.Config.ARGB_8888);
		mActualCanvas = new Canvas(mActualBitmap);

		initResources();
		initTileMap();
		initSprites();
		initGame();

		mViewableTop = GAME_HEIGHT_PX + TILE_HEIGHT_PX;
	}

	private void initTileMap() {
		mMapWidth = 8;
		mMapHeight = 400;

		mTileMap = new int[mMapHeight][mMapWidth];

		for (int i = 0, n = mMapHeight; i < n; i++)
			for (int j = 0, o = mMapWidth; j < o; j++)
				mTileMap[i][j] = 0;

		int foo = 0;
		for (int i = mMapHeight - 1; i >= 0; i--) {
			mTileMap[i][foo] = 4;
			if (++foo > 7)
				foo = 0;
		}

		mY = mMapHeight - 1;
	}

	private void initResources() {
		int tileset[] = mTileSets[0];
		for (int i = 0, n = tileset.length; i < n; i++) {
			mTiles.put(i, BitmapFactory.decodeResource(mContext.getResources(),
					tileset[i]));
		}
	}

	private void initSprites() {
		mPlayer = new Sprite(mContext, 
				R.drawable.objects_00, R.drawable.objects_01);
	}

	private void initGame() {
		mPlayer.setPos((GAME_WIDTH_PX - mPlayer.getWidth()) / 2, 
				GAME_HEIGHT_PX - mPlayer.getHeight() * 2);
	}

	public void stopGameLoop() {
		mRunning = false;
	}

	private int scale(int size) {
		return size * mDensity;
	}

	private void renderTiles(Canvas canvas) {
		for (int j = 0; j < GAME_WIDTH; j++) {
			int tile = mTileMap[mY][j];
			int scaledX = scale(j * TILE_WIDTH_PX);

			canvas.drawBitmap(mTiles.get(tile), scaledX, 
					scale(mViewableTop - TILE_HEIGHT_PX), null);
			canvas.drawBitmap(mTiles.get(tile), scaledX, 
					scale(mViewableTop + GAME_HEIGHT_PX), null);
		}
	}

	private void renderSprites(Canvas canvas) {
		mPlayer.render(canvas);
	}

	@Override
	public void run() {
		mRunning = true;

		while (mRunning) {
			Canvas canvas = null;
			try {
				// Set up and render tiles
				if ((mViewableTop % TILE_HEIGHT_PX) == 0) {
					renderTiles(mVirtualCanvas);
					mY--;
				}

				mViewableTop -= 4;
				if (mViewableTop <= 0)
					mViewableTop = GAME_HEIGHT_PX + TILE_HEIGHT_PX;

				// Render actual bitmap
				Rect source = new Rect(0, scale(mViewableTop), mScaledWidth, scale(mViewableTop) + mScaledHeight);
				Rect dest = new Rect(0, 0, mScaledWidth, mScaledHeight);

				mActualCanvas.drawBitmap(mVirtualBitmap, source, dest, null);

				// Render sprites
				renderSprites(mActualCanvas);

				// Render scratch bitmap to the surface 
				canvas = mSurfaceHolder.lockCanvas();
				synchronized (mSurfaceHolder) {
					source = new Rect(0, 0, mScaledWidth, mScaledHeight);
					dest = new Rect(0, 0, mScreenWidth, mScreenHeight);

					if (canvas != null) {
						canvas.drawBitmap(mActualBitmap, source, dest, null);
					}
				}
			} finally {
				if (canvas != null)
					mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
try {
	Thread.sleep(25);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		}

		Log.v("*", "Game loop exited");
	}

	public void cleanUp() {
		mVirtualBitmap.recycle();

		for (int i = 0, n = mTiles.size(); i < n; i++) {
			int key = mTiles.keyAt(i);
			mTiles.get(key).recycle();
		}

		mPlayer.destroy();
	}
}
