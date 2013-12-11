package org.akop.airjag.model;

import java.util.ArrayList;

public class GameRow {
	public static int MAX_COLUMN_SIZE = 8;
	
	private ArrayList<GameAsset> mTiles;
	private ArrayList<GameAsset> mObjects;
	
	public GameRow() {
		mTiles = new ArrayList<GameAsset>();
		mObjects = new ArrayList<GameAsset>();
	}
	
	public GameRow(int resource, int rowPosition, String resourceName) {
		this.mTiles = new ArrayList<GameAsset>(MAX_COLUMN_SIZE);
		this.mObjects = new ArrayList<GameAsset>(MAX_COLUMN_SIZE);
		
		for(int ii = 0; ii < MAX_COLUMN_SIZE; ++ii) {
			this.mTiles.add(ii,
					new GameAsset(	resource, 
									resourceName,
									GameAsset.ASSET_TYPE_TILE,
									ii,
									rowPosition));
		}
		
		for(int ii = 0; ii < MAX_COLUMN_SIZE; ++ii) {
			this.mObjects.add(ii,
					new GameAsset(	-1, 
									"",
									GameAsset.ASSET_TYPE_OBJECT,
									ii,
									rowPosition));
		}
	}

	public ArrayList<GameAsset> getTiles() {
		return mTiles;
	}
	
	public ArrayList<GameAsset> getObjects() {
		return mObjects;
	}

	public GameRow setTiles(ArrayList<GameAsset> mTiles) {
		this.mTiles = mTiles;
		return this;
	}
	
	public GameAsset getTileAtPosition(int position) {
		return mTiles.get(position);
	}
	
	public GameRow addTile(GameAsset gameAsset) {
		mTiles.add(gameAsset);
		
		return this;
	}
	
	public GameRow setTileAtPosition(int position, GameAsset gameTile) {
		mTiles.get(position).setResource(gameTile.getResource());
		mTiles.get(position).setType(gameTile.getType());
		
		return this;
	}
	
	public GameRow setTileResourceAtPosition(int position, int resource) {
		mTiles.get(position).setResource(resource);
		
		return this;
	}
	
	public GameRow setTileTypeAtPosition(int position, int tileType) {
		mTiles.get(position).setType(tileType);
		
		return this;
	}
	
	public GameAsset getObjectAtPosition(int position) {
		return mObjects.get(position);
	}
}
