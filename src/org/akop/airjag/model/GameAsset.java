package org.akop.airjag.model;

public class GameAsset {
	
	public static final int ASSET_TYPE_TILE = 0;
	public static final int ASSET_TYPE_OBJECT = 1;
	
	private int mAssetType;
	private int mAssetResource;
	private String mAssetResourceName;
	
	private int mColumn;
	private int mRow;
	
	public GameAsset(int resource, int tileType) {
		this(resource, "", tileType, 0, ASSET_TYPE_TILE);
	}
	
	public GameAsset(int resource, String resourceName, int type, int column, int row) {
		this.setResource(resource)
			.setType(type)
			.setResourceName(resourceName)
			.setColumnPosition(column)
			.setRowPosition(row);
	}
	
	public int getType() {
		return mAssetType;
	}
	public GameAsset setType(int type) {
		this.mAssetType = type;
		return this;
	}
	public int getResource() {
		return mAssetResource;
	}
	public GameAsset setResource(int tileResource) {
		this.mAssetResource = tileResource;
		
		return this;
	}
	
	public String getResourceName() {
		return mAssetResourceName;
	}
	public GameAsset setResourceName(String tileResourceName) {
		this.mAssetResourceName = tileResourceName;
		
		return this;
	}
	
	public int getColumnPosition() {
		return this.mColumn;
	}
	public GameAsset setColumnPosition(int column) {
		this.mColumn = column;
		return this;
	}
	
	public int getRowPosition() {
		return this.mRow;
	}
	public GameAsset setRowPosition(int row) {
		this.mRow = row;
		return this;
	}
}
