package org.akop.airjag.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;



public class GameMap implements Parcelable {

	public static class SpriteHolder implements Parcelable {

		public int mType;
		public int mX;
		public int mY;

		private SpriteHolder(Parcel in) {
			mType = in.readInt();
			mX = in.readInt();
			mY = in.readInt();
		}

		public SpriteHolder(int type, int x, int y) {
			mType = type;
			mX = x;
			mY = y;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(mType);
			dest.writeInt(mX);
			dest.writeInt(mY);
		}

		public static final Parcelable.Creator<SpriteHolder> CREATOR = new Parcelable.Creator<SpriteHolder>() {
			public SpriteHolder createFromParcel(Parcel in) {
				return new SpriteHolder(in);
			}

			public SpriteHolder[] newArray(int size) {
				return new SpriteHolder[size];
			}
		};
	}

	private static final int WIDTH = 8;

	private int mTileSet;
	private int mWidth;
	private int mHeight;

	private int mTiles[][];
	private SparseArray<List<SpriteHolder>> mSpriteMap;

	private GameMap(Parcel in) {
		mWidth = in.readInt();
		mHeight = in.readInt();
		mTileSet = in.readInt();

		mTiles = new int[mHeight][mWidth];
		for (int i = 0; i < mHeight; i++) {
			in.readIntArray(mTiles[i]);
		}

		mSpriteMap = new SparseArray<List<SpriteHolder>>();

		List<SpriteHolder> sprites = new ArrayList<SpriteHolder>();
		in.readTypedList(sprites, SpriteHolder.CREATOR);

		for (SpriteHolder holder: sprites) {
			List<SpriteHolder> list = mSpriteMap.get(holder.mY);
			if (list == null) {
				list = new ArrayList<SpriteHolder>();
				mSpriteMap.put(holder.mY, list);
			}

			list.add(holder);
		}
	}

	public GameMap(int height, int tileSet) {
		mWidth = WIDTH;
		mHeight = height;
		mTileSet = tileSet;
		mTiles = new int[mHeight][mWidth];
		mSpriteMap = new SparseArray<List<SpriteHolder>>();
	}

	public void setTile(int x, int y, int tile) {
		mTiles[y][x] = tile;
	}

	public int getTile(int x, int y) {
		return mTiles[y][x];
	}

	public int getHeight() {
		return mHeight;
	}

	public int getTileSet() {
		return mTileSet;
	}

	public void setSprite(int x, int y, int sprite) {
		List<SpriteHolder> list = mSpriteMap.get(y);
		if (list == null) {
			list = new ArrayList<SpriteHolder>();
			mSpriteMap.put(y, list);
		}

		SpriteHolder holder = new SpriteHolder(sprite, x, y);
		list.add(holder);
	}

	public List<SpriteHolder> getSprites(int y) {
		return mSpriteMap.get(y);
	}

	public static final Parcelable.Creator<GameMap> CREATOR = new Parcelable.Creator<GameMap>() {
		public GameMap createFromParcel(Parcel in) {
			return new GameMap(in);
		}

		public GameMap[] newArray(int size) {
			return new GameMap[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mWidth);
		dest.writeInt(mHeight);
		dest.writeInt(mTileSet);

		for (int i = 0; i < mHeight; i++) {
			dest.writeIntArray(mTiles[i]);
		}

		List<SpriteHolder> sprites = new ArrayList<SpriteHolder>();
		for (int i = 0, n = mSpriteMap.size(); i < n; i++) {
			int key = mSpriteMap.keyAt(i);
			sprites.addAll(mSpriteMap.get(key));
		}

		dest.writeTypedList(sprites);
	}
}
