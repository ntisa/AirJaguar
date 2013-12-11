package org.akop.airjag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.akop.airjag.model.Boat;
import org.akop.airjag.model.Bus;
import org.akop.airjag.model.Cannon;
import org.akop.airjag.model.Canoe;
import org.akop.airjag.model.CarEastbound;
import org.akop.airjag.model.CarNorthbound;
import org.akop.airjag.model.CarSouthbound;
import org.akop.airjag.model.CarWestbound;
import org.akop.airjag.model.HTank;
import org.akop.airjag.model.Helo;
import org.akop.airjag.model.Jet;
import org.akop.airjag.model.PlaneEastbound;
import org.akop.airjag.model.PlaneWestbound;
import org.akop.airjag.model.Player;
import org.akop.airjag.model.RedPlane;
import org.akop.airjag.model.SmallTank;
import org.akop.airjag.model.Sprite;
import org.akop.airjag.model.TaxiingPlane;
import org.akop.airjag.model.Tractor;
import org.akop.airjag.model.VTank;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {

	private static class SpriteHolder {
		public int mType;
		public int mX;

		public SpriteHolder(int type, int x) {
			mType = type;
			mX = x;
		}
	}

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

	private static final int mSpriteSets[] = {
		R.drawable.objects_00,
		R.drawable.objects_01,
		R.drawable.objects_02,
		R.drawable.objects_03,
		R.drawable.objects_04,
		R.drawable.objects_05,
		R.drawable.objects_06,
		R.drawable.objects_07,
		R.drawable.objects_08,
		R.drawable.objects_09,
		R.drawable.objects_10,
		R.drawable.objects_11,
		R.drawable.objects_12,
		R.drawable.objects_13,
		R.drawable.objects_14,
		R.drawable.objects_15,
		R.drawable.objects_16,
		R.drawable.objects_17,
		R.drawable.objects_18,
		R.drawable.objects_19,
		R.drawable.objects_20,
		R.drawable.objects_21,
		R.drawable.objects_22,
		R.drawable.objects_23,
		R.drawable.objects_24,
		R.drawable.objects_25,
		R.drawable.objects_26,
		R.drawable.objects_27,
		R.drawable.objects_28,
		R.drawable.objects_29,
	};

	private SparseArray<List<SpriteHolder>> mSpriteMap;
	private int[][] mTileMap;

	private int mY;

	private TileSack mSack;
	private Sprite mPlayer;
	private List<Sprite> mOnscreenSprites;

	public GameLoop(Context context, SurfaceHolder holder) {
		mContext = context;
		mRunning = false;
		mSurfaceHolder = holder;
		mSpriteMap = new SparseArray<List<SpriteHolder>>();
		mOnscreenSprites = new ArrayList<Sprite>();

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

		mSack = new TileSack(mContext);

		initResources();
		initTileMap();
		initSprites();
		initGame();

		mViewableTop = GAME_HEIGHT_PX + TILE_HEIGHT_PX;
	}

	private void initResources() {
		int tileset[] = mTileSets[0];
		for (int i = 0, n = tileset.length; i < n; i++) {
			mSack.addTile(TileSack.MASK_TILE | i, tileset[i]);
		}

		for (int i = 0, n = mSpriteSets.length; i < n; i++) {
			mSack.addTile(TileSack.MASK_SPRITE | i, mSpriteSets[i]);
		}
	}

	private void initTileMap() {
		mMapHeight = 400;

		mTileMap = new int[mMapHeight][GAME_WIDTH];

		for (int i = 0, n = mMapHeight; i < n; i++)
			for (int j = 0, o = GAME_WIDTH; j < o; j++)
				mTileMap[i][j] = 0;

		mY = mMapHeight - 1;
	}

	private void initSprites() {
		mPlayer = new Player(mSack);
	}

	private void initGame() {
		mPlayer.setPos((GAME_WIDTH_PX - mPlayer.getWidth()) / 2, 
				GAME_HEIGHT_PX - mPlayer.getHeight() * 2);

		// Generate some dummy data
		int foo = 0;
		for (int i = mMapHeight - 1; i >= 0; i--) {
			mTileMap[i][foo] = 4;
			if (++foo > 7)
				foo = 0;
		}
int sprites[] = new int[] { 7,8,9,2,3,4,5,6,14, 29, 26, 28, 24, 22, 18, 20, 10, 12 };
int spriteIdx = 0;
		for (int i = mMapHeight - 11; i >= 0; i-=3) {
			List<SpriteHolder> spriteHolders = new ArrayList<SpriteHolder>();
			int x = (int)(Math.random() * GAME_WIDTH_PX);
			spriteHolders.add(new SpriteHolder(sprites[spriteIdx], x));
			mSpriteMap.put(i, spriteHolders);
if (++spriteIdx >= sprites.length)
	spriteIdx = 0;
		}
	}

	public void stopGameLoop() {
		mRunning = false;
	}

	private int scale(int size) {
		return size * mDensity;
	}

	private void placeSprites() {
		List<SpriteHolder> spriteHolders = mSpriteMap.get(mY);
		if (spriteHolders != null) {
			for (SpriteHolder spriteHolder: spriteHolders) {
				Sprite sprite = null;

				switch(spriteHolder.mType) {
				case 2:
					sprite = new Canoe(mSack);
					break;
				case 3:
					sprite = new CarWestbound(mSack);
					break;
				case 4:
					sprite = new CarEastbound(mSack);
					break;
				case 5:
					sprite = new CarNorthbound(mSack);
					break;
				case 6:
					sprite = new CarSouthbound(mSack);
					break;
				case 7:
					sprite = new Bus(mSack);
					break;
				case 8:
					sprite = new TaxiingPlane(mSack);
					break;
				case 9:
					sprite = new Tractor(mSack);
					break;
				case 10:
					sprite = new RedPlane(mSack);
					break;
				case 12:
					sprite = new Helo(mSack);
					break;
				case 14:
					sprite = new Cannon(mSack);
					break;
				case 18:
					sprite = new VTank(mSack);
					break;
				case 20:
					sprite = new HTank(mSack);
					break;
				case 22:
					sprite = new Boat(mSack);
					break;
				case 24:
					sprite = new SmallTank(mSack);
					break;
				case 26:
					sprite = new Jet(mSack);
					break;
				case 28:
					sprite = new PlaneEastbound(mSack);
					break;
				case 29:
					sprite = new PlaneWestbound(mSack);
					break;
				}

				if (sprite != null) {
					if (sprite.needsPlacement())
						sprite.setPos(spriteHolder.mX, -TILE_HEIGHT_PX);

					mOnscreenSprites.add(sprite);
					Collections.sort(mOnscreenSprites, Sprite.ZIndexComparator);
				}
			}
		}
	}

	private void pruneOnscreenSprites() {
		for (int i = mOnscreenSprites.size() - 1; i >= 0; i--) {
			Sprite sprite = mOnscreenSprites.get(i);
			if (sprite.getY() > GAME_HEIGHT_PX || 
					sprite.getX() > GAME_WIDTH_PX ||
					sprite.isKilled()) {
				mOnscreenSprites.remove(i);
				Log.v("*", "Removed sprite");
			}
		}
	}

	private void renderTiles(Canvas canvas) {
		for (int j = 0; j < GAME_WIDTH; j++) {
			int scaledX = scale(j * TILE_WIDTH_PX);

			Bitmap tile = mSack.getTile(TileSack.MASK_TILE | mTileMap[mY][j]);
			canvas.drawBitmap(tile, scaledX, scale(mViewableTop - TILE_HEIGHT_PX), null);
			canvas.drawBitmap(tile, scaledX, scale(mViewableTop + GAME_HEIGHT_PX), null);
		}
	}

	@Override
	public void run() {
		mRunning = true;

		while (mRunning) {
			Canvas canvas = null;
			try {
				// Set up and render tiles
				if ((mViewableTop % TILE_HEIGHT_PX) == 0) {
					pruneOnscreenSprites();
					placeSprites();
					renderTiles(mVirtualCanvas);
					mY--;
				}

				mViewableTop -= 4;
				if (mViewableTop <= 0)
					mViewableTop = GAME_HEIGHT_PX + TILE_HEIGHT_PX;

				// Render tile map to scratch bitmap
				Rect source = new Rect(0, scale(mViewableTop), mScaledWidth, scale(mViewableTop) + mScaledHeight);
				Rect dest = new Rect(0, 0, mScaledWidth, mScaledHeight);

				mActualCanvas.drawBitmap(mVirtualBitmap, source, dest, null);

				// Render sprites to scratch bitmap
				for (Sprite sprite: mOnscreenSprites) {
					if (mPlayer.isInCollision(sprite)) {
						sprite.kill();
					} else if (!sprite.isKilled()) {
						sprite.advance(mPlayer);
						sprite.render(mActualCanvas);
					}
				}

				mPlayer.advance(null);
				mPlayer.render(mActualCanvas);

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
		mActualBitmap.recycle();
		mSack.destroy();
	}
}
