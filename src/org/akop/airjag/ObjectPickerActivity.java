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

public class ObjectPickerActivity extends Activity {
	
	TypedArray availableObjects;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object_picker);
		
		// Get the array of objects
		availableObjects = getResources().obtainTypedArray(R.array.object_set);
		
		ListView listView = (ListView) findViewById(R.id.picker_object_listview);
		ObjectPickerAdapter adapter = new ObjectPickerAdapter(this, R.layout.template_available_asset_row, availableObjects);
		listView.setAdapter(adapter);	
	}
	
	@Override
	protected void onDestroy() {
		availableObjects.recycle();
		super.onDestroy();
	}
	
	public class ObjectPickerAdapter extends ArrayAdapter<Object> {
		
		private Context mContext;
		TypedArray mAvailableObjectRows;

		public ObjectPickerAdapter(Context context, int resource, TypedArray availableObjectRows) {
			super(context, resource);
			
			mContext = context;
			mAvailableObjectRows = (TypedArray) availableObjectRows;
		}
		
		@Override
		public int getCount() {
			return mAvailableObjectRows.length();
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
			
			iv.setImageResource(mAvailableObjectRows.getResourceId(position, -1));
			
			tv.setText(mAvailableObjectRows.getText(position));
			
			// Hackey again, for quickness FIXME
			v.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					String assetName = mAvailableObjectRows.getText(position).toString();
					assetName = assetName.substring(assetName.lastIndexOf("_") + 1, assetName.lastIndexOf("."));
					
					intent.putExtra("resId", mAvailableObjectRows.getResourceId(position, -1));
					intent.putExtra("assetName", assetName);
					intent.putExtra("resType", GameAsset.ASSET_TYPE_OBJECT);
					
					setResult(RESULT_OK, intent);    
					finish();
				}
			});
					
			return v;
		}
	}
}
