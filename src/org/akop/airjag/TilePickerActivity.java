package org.akop.airjag;

import org.akop.airjag.model.GameAsset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TilePickerActivity extends Activity {
	
	TypedArray availableTiles;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tile_picker);
		
		Intent intent = getIntent();
		int tileSetNumber = intent.getIntExtra("tileSet", 0);
		String tileSet = getString(R.string.tile_set_generic, tileSetNumber);
		int tileSetId = getResources().getIdentifier(tileSet, "array", getPackageName());
		
		// Get the array of tiles/sprites/assets
		availableTiles = getResources().obtainTypedArray(tileSetId);
		
		ListView listView = (ListView) findViewById(R.id.picker_tile_listview);
		TilePickerAdapter adapter = new TilePickerAdapter(this, R.layout.template_available_asset_row, availableTiles);
		listView.setAdapter(adapter);	
	}
	
	@Override
	protected void onDestroy() {
		availableTiles.recycle();
		super.onDestroy();
	}
	
	public class TilePickerAdapter extends ArrayAdapter<Object> {
		
		private Context mContext;
		TypedArray mAvailableTileRows;

		public TilePickerAdapter(Context context, int resource, TypedArray availableTileRows) {
			super(context, resource);
			
			mContext = context;
			mAvailableTileRows = (TypedArray) availableTileRows;
		}
		
		@Override
		public int getCount() {
			return mAvailableTileRows.length();
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View v = convertView;
			if (v == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				v = (LinearLayout) inflater.inflate(R.layout.template_available_asset_row, null);
			}
			
			ImageView iv = (ImageView) v.findViewById(R.id.tile_image_row);
			TextView tv = (TextView) v.findViewById(R.id.tile_title_row);
			
			iv.setImageResource(mAvailableTileRows.getResourceId(position, -1));
			
			tv.setText(mAvailableTileRows.getText(position));
			
			// Hackey again, for quickness FIXME
			v.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String assetName = mAvailableTileRows.getText(position).toString();
					assetName = assetName.substring(assetName.lastIndexOf("_") + 1, assetName.lastIndexOf("."));
					Intent intent = new Intent();
					
					intent.putExtra("resId", mAvailableTileRows.getResourceId(position, -1));
					intent.putExtra("assetName", assetName);
					intent.putExtra("resType", GameAsset.ASSET_TYPE_TILE); 
					
					setResult(RESULT_OK, intent);     
					finish();
				}
			});
					
			return v;
		}
	}
}
