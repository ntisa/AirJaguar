package org.akop.airjag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.akop.airjag.model.GameAsset;
import org.akop.airjag.model.GameRow;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

public class LevelEditorActivity extends Activity {
	
	private static final int MAX_ROW_SIZE = 200;
	private static final int GAME_TILE_REQUEST_CODE = 0;
	private static final int GAME_OBJECT_REQUEST_CODE = 1;
	private static final int EDIT_MODE_TILE = 3;
	private static final int EDIT_MODE_OBJECT = 4;
	
	private ArrayList<GameRow> mGameAssetRows;
	private GameAsset replacementTile;
	private GameAsset replacementObject;
	
	private int editMode = EDIT_MODE_TILE;
	
	private int currentTileSet;
	
	private void resetDefaultTileView() {
		// Initialize the Game Map Rows TODO SO KLUDGEY!		
		// Kludge . . . im tired
		int drawableID;
		switch (currentTileSet) {
		case 0:
			drawableID = R.drawable.tileset0_00;
			break;
		case 1:
			drawableID = R.drawable.tileset1_00;
			break;
		case 2:
			drawableID = R.drawable.tileset2_00;
			break;
		case 3:
			drawableID = R.drawable.tileset3_00;
			break;
		case 4:
			drawableID = R.drawable.tileset4_00;
			break;
		default:
			drawableID = R.drawable.tileset0_00;
			break;
		}
		mGameAssetRows = new ArrayList<GameRow>(MAX_ROW_SIZE);
		for(int ii = 0; ii < MAX_ROW_SIZE; ++ii) {
			mGameAssetRows.add(ii, new GameRow(drawableID, ii, "00"));
		}
		
		ListView listView = (ListView) findViewById(R.id.game_tile_listview);
		TileEditorAdapter adapter = new TileEditorAdapter(this, R.layout.template_asset_row, mGameAssetRows);
		adapter.notifyDataSetInvalidated();
		listView.setAdapter(adapter);
		
		// Set the default selected tile
		replacementTile = new GameAsset(drawableID, GameAsset.ASSET_TYPE_TILE);
		ImageView replacementTileImage = (ImageView) findViewById(R.id.selected_tile_image);
		replacementTileImage.setImageResource(replacementTile.getResource());
	}
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_editor);

		currentTileSet = 0;
		resetDefaultTileView();

		ImageView replacementTileImage = (ImageView) findViewById(R.id.selected_tile_image);
		replacementTileImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelEditorActivity.this, TilePickerActivity.class);
				intent.putExtra("tileSet", currentTileSet);
				startActivityForResult(intent, GAME_TILE_REQUEST_CODE);
			}
		});
		
		// Set the default selected object
		replacementObject = new GameAsset(R.drawable.objects_00, GameAsset.ASSET_TYPE_OBJECT);
		ImageView replacementObjectImage = (ImageView) findViewById(R.id.selected_object_image);
				
		replacementObjectImage.setImageResource(replacementObject.getResource());
		replacementObjectImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelEditorActivity.this, ObjectPickerActivity.class);
				startActivityForResult(intent, GAME_OBJECT_REQUEST_CODE);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			int selectedResourceId = data.getIntExtra("resId", -1);
			int selectedResourceType = data.getIntExtra("resType", -1);
			String selectedResourceName = data.getStringExtra("assetName");
			ImageView replacementTileImage = (ImageView) findViewById(R.id.selected_tile_image);
			ImageView replacementObjectImage = (ImageView) findViewById(R.id.selected_object_image);
			
			if(requestCode == GAME_TILE_REQUEST_CODE) {
				replacementTile	.setResource(selectedResourceId)
								.setResourceName(selectedResourceName)
								.setType(selectedResourceType);
				
				// Change the image on screen
				replacementTileImage.setImageResource(replacementTile.getResource());
				
				editMode = EDIT_MODE_TILE;
			}
			else if(requestCode == GAME_OBJECT_REQUEST_CODE) {				
				replacementObject.setResource(selectedResourceId)
								.setResourceName(selectedResourceName)
								.setType(selectedResourceType);
				
				// Change the image on screen
				replacementObjectImage.setImageResource(replacementObject.getResource());
				
				editMode = EDIT_MODE_OBJECT;
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_editor_menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_change_tile_set:
			
			final CharSequence[] items = {
	                "Tile Set 0", 
	                "Tile Set 1", 
	                "Tile Set 2",
	                "Tile Set 3",
	                "Tile Set 4"
	        };

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Choose Tile Set");
	        builder.setItems(items, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	            	currentTileSet = item;
	            	resetDefaultTileView();
	            }
	        });
	        AlertDialog alert = builder.create();
	        alert.show();
			
			break;
		case R.id.action_upload_map:
			
			builder = new AlertDialog.Builder(this);
			builder.setTitle("Enter Level Name");

			// Set up the input
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			builder.setView(input);

			// Set up the buttons
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	parseAndUploadMap(input.getText().toString());
			    }
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.cancel();
			    }
			});

			builder.show();
			break;

		default:
			return false;
		}
		return true;
	}
	
	private void parseAndUploadMap(final String mapName) {
		JSONArray innerJSONArray;
		JSONArray outerJSONArray = new JSONArray();
		JSONArray objectArray = new JSONArray();
		JSONObject objectJSON;
		
		String finalResourceName;
		for (GameRow gr : mGameAssetRows) {
			// Tiles
			innerJSONArray = new JSONArray();
			for (GameAsset ga : gr.getTiles()) {
				finalResourceName = ga.getResourceName().substring(1);
				innerJSONArray.put(Integer.parseInt(finalResourceName));
			}
			outerJSONArray.put(innerJSONArray);
			
			// Objects
			for (GameAsset ga : gr.getObjects()) {
				if(TextUtils.isEmpty(ga.getResourceName()))
					continue;
				
				objectJSON = new JSONObject();
				try {
					objectJSON.put("Column", ga.getColumnPosition());
					objectJSON.put("Row", ga.getRowPosition());
					objectJSON.put("ResourceName", Integer.parseInt(ga.getResourceName()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				objectArray.put(objectJSON);
			}
		}
		
		JSONObject fullJSONObject = new JSONObject();
		try {
			fullJSONObject.put("TileSet", currentTileSet);
			fullJSONObject.put("GameTileData", outerJSONArray);
			fullJSONObject.put("GameObjectData", objectArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		final String jsonStr = fullJSONObject.toString();
		Log.e("JSON", jsonStr);
		
		Thread t = new Thread(
		new Runnable() {
			
			@Override
			public void run() {
				HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://10.0.1.193:8080/put");

			    try {
			        // Add your data
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			        nameValuePairs.add(new BasicNameValuePair("data", jsonStr));
			        nameValuePairs.add(new BasicNameValuePair("name", mapName));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			        // Execute HTTP Post Request
			        HttpResponse response = httpclient.execute(httppost);

			    } catch (ClientProtocolException e) {
			        // TODO Auto-generated catch block
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			    }
			}
		});
		
		t.start();
		
	}
		
	public class TileEditorAdapter extends ArrayAdapter<GameRow> {
		
		private Context mContext;
		private ImageView previouslySelectedTile;
		private boolean doubleClick;

		public TileEditorAdapter(Context context, int resource, List<GameRow> gameTileRows) {
			super(context, resource, gameTileRows);
			
			mContext = context;
			doubleClick = false;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				v = (FrameLayout) inflater.inflate(R.layout.template_asset_row, null);
			}
			
			final ImageView tile0 = (ImageView) v.findViewById(R.id.image_0);
			final ImageView tile1 = (ImageView) v.findViewById(R.id.image_1);
			final ImageView tile2 = (ImageView) v.findViewById(R.id.image_2);
			final ImageView tile3 = (ImageView) v.findViewById(R.id.image_3);
			final ImageView tile4 = (ImageView) v.findViewById(R.id.image_4);
			final ImageView tile5 = (ImageView) v.findViewById(R.id.image_5);
			final ImageView tile6 = (ImageView) v.findViewById(R.id.image_6);
			final ImageView tile7 = (ImageView) v.findViewById(R.id.image_7);
			
			final ImageView Object0 = (ImageView) v.findViewById(R.id.object_0);
			final ImageView Object1 = (ImageView) v.findViewById(R.id.object_1);
			final ImageView Object2 = (ImageView) v.findViewById(R.id.object_2);
			final ImageView Object3 = (ImageView) v.findViewById(R.id.object_3);
			final ImageView Object4 = (ImageView) v.findViewById(R.id.object_4);
			final ImageView Object5 = (ImageView) v.findViewById(R.id.object_5);
			final ImageView Object6 = (ImageView) v.findViewById(R.id.object_6);
			final ImageView Object7 = (ImageView) v.findViewById(R.id.object_7);
			
			// HACKEY SACK ON CLICK LISTENERS
			tile0.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					highlightTile(tile0);
					
					if(editMode == EDIT_MODE_TILE) {
						mGameAssetRows.get(position).getTileAtPosition(0).setResource(replacementTile.getResource());
						mGameAssetRows.get(position).getTileAtPosition(0).setResourceName(replacementTile.getResourceName());
						mGameAssetRows.get(position).getTileAtPosition(0).setType(replacementTile.getType());
						
						tile0.setImageResource(
								mGameAssetRows.get(position).getTileAtPosition(0).getResource());
					}
					else {
						mGameAssetRows.get(position).getObjectAtPosition(0).setResource(replacementObject.getResource());
						mGameAssetRows.get(position).getObjectAtPosition(0).setResourceName(replacementObject.getResourceName());
						mGameAssetRows.get(position).getObjectAtPosition(0).setType(replacementObject.getType());
						
						Object0.setImageResource(
								mGameAssetRows.get(position).getObjectAtPosition(0).getResource());
					}
				}
			});
			
			tile1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					highlightTile(tile1);
					if(editMode == EDIT_MODE_TILE) {
						mGameAssetRows.get(position).getTileAtPosition(1).setResource(replacementTile.getResource());
						mGameAssetRows.get(position).getTileAtPosition(1).setResourceName(replacementTile.getResourceName());
						mGameAssetRows.get(position).getTileAtPosition(1).setType(replacementTile.getType());
						
						tile1.setImageResource(
								mGameAssetRows.get(position).getTileAtPosition(1).getResource());
					}
					
					else {
						mGameAssetRows.get(position).getObjectAtPosition(1).setResource(replacementObject.getResource());
						mGameAssetRows.get(position).getObjectAtPosition(1).setResourceName(replacementObject.getResourceName());
						mGameAssetRows.get(position).getObjectAtPosition(1).setType(replacementObject.getType());
						
						Object1.setImageResource(
								mGameAssetRows.get(position).getObjectAtPosition(1).getResource());
					}
				}
			});
			
			tile2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					highlightTile(tile2);
					if(editMode == EDIT_MODE_TILE) {
						mGameAssetRows.get(position).getTileAtPosition(2).setResource(replacementTile.getResource());
						mGameAssetRows.get(position).getTileAtPosition(2).setResourceName(replacementTile.getResourceName());
						mGameAssetRows.get(position).getTileAtPosition(2).setType(replacementTile.getType());
						
						tile2.setImageResource(
								mGameAssetRows.get(position).getTileAtPosition(2).getResource());
					}
					
					else {
						mGameAssetRows.get(position).getObjectAtPosition(2).setResource(replacementObject.getResource());
						mGameAssetRows.get(position).getObjectAtPosition(2).setResourceName(replacementObject.getResourceName());
						mGameAssetRows.get(position).getObjectAtPosition(2).setType(replacementObject.getType());
						
						Object2.setImageResource(
								mGameAssetRows.get(position).getObjectAtPosition(2).getResource());
					}
				}
			});
			
			tile3.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					highlightTile(tile3);
					if(editMode == EDIT_MODE_TILE) {
						mGameAssetRows.get(position).getTileAtPosition(3).setResource(replacementTile.getResource());
						mGameAssetRows.get(position).getTileAtPosition(3).setResourceName(replacementTile.getResourceName());
						mGameAssetRows.get(position).getTileAtPosition(3).setType(replacementTile.getType());
						
						tile3.setImageResource(
								mGameAssetRows.get(position).getTileAtPosition(3).getResource());
					}
					else {
						mGameAssetRows.get(position).getObjectAtPosition(3).setResource(replacementObject.getResource());
						mGameAssetRows.get(position).getObjectAtPosition(3).setResourceName(replacementObject.getResourceName());
						mGameAssetRows.get(position).getObjectAtPosition(3).setType(replacementObject.getType());
						
						Object3.setImageResource(
								mGameAssetRows.get(position).getObjectAtPosition(3).getResource());
					}
				}
			});
			
			tile4.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					highlightTile(tile4);
					if(editMode == EDIT_MODE_TILE) {
						mGameAssetRows.get(position).getTileAtPosition(4).setResource(replacementTile.getResource());
						mGameAssetRows.get(position).getTileAtPosition(4).setResourceName(replacementTile.getResourceName());
						mGameAssetRows.get(position).getTileAtPosition(4).setType(replacementTile.getType());
						
						tile4.setImageResource(
								mGameAssetRows.get(position).getTileAtPosition(4).getResource());
					}
					else {
						mGameAssetRows.get(position).getObjectAtPosition(4).setResource(replacementObject.getResource());
						mGameAssetRows.get(position).getObjectAtPosition(4).setResourceName(replacementObject.getResourceName());
						mGameAssetRows.get(position).getObjectAtPosition(4).setType(replacementObject.getType());
						
						Object4.setImageResource(
								mGameAssetRows.get(position).getObjectAtPosition(4).getResource());
					}
				}
			});
			
			tile5.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					highlightTile(tile5);
					if(editMode == EDIT_MODE_TILE) {
						mGameAssetRows.get(position).getTileAtPosition(5).setResource(replacementTile.getResource());
						mGameAssetRows.get(position).getTileAtPosition(5).setResourceName(replacementTile.getResourceName());
						mGameAssetRows.get(position).getTileAtPosition(5).setType(replacementTile.getType());
						
						tile5.setImageResource(
								mGameAssetRows.get(position).getTileAtPosition(5).getResource());
					}
					else {
						mGameAssetRows.get(position).getObjectAtPosition(5).setResource(replacementObject.getResource());
						mGameAssetRows.get(position).getObjectAtPosition(5).setResourceName(replacementObject.getResourceName());
						mGameAssetRows.get(position).getObjectAtPosition(5).setType(replacementObject.getType());
						
						Object5.setImageResource(
								mGameAssetRows.get(position).getObjectAtPosition(5).getResource());
					}
				}
			});
			
			tile6.setOnClickListener(new View.OnClickListener() {
							
				@Override
				public void onClick(View v) {
//					highlightTile(tile6);
					if(editMode == EDIT_MODE_TILE) {
						mGameAssetRows.get(position).getTileAtPosition(6).setResource(replacementTile.getResource());
						mGameAssetRows.get(position).getTileAtPosition(6).setResourceName(replacementTile.getResourceName());
						mGameAssetRows.get(position).getTileAtPosition(6).setType(replacementTile.getType());
						
						tile6.setImageResource(
								mGameAssetRows.get(position).getTileAtPosition(6).getResource());
					}
					else {
						mGameAssetRows.get(position).getObjectAtPosition(6).setResource(replacementObject.getResource());
						mGameAssetRows.get(position).getObjectAtPosition(6).setResourceName(replacementObject.getResourceName());
						mGameAssetRows.get(position).getObjectAtPosition(6).setType(replacementObject.getType());
						
						Object6.setImageResource(
								mGameAssetRows.get(position).getObjectAtPosition(6).getResource());
					}
				}
			});

			tile7.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					highlightTile(tile7);
					if(editMode == EDIT_MODE_TILE) {
						mGameAssetRows.get(position).getTileAtPosition(7).setResource(replacementTile.getResource());
						mGameAssetRows.get(position).getTileAtPosition(7).setResourceName(replacementTile.getResourceName());
						mGameAssetRows.get(position).getTileAtPosition(7).setType(replacementTile.getType());
						
						tile7.setImageResource(
								mGameAssetRows.get(position).getTileAtPosition(7).getResource());
					}
					else {
						mGameAssetRows.get(position).getObjectAtPosition(7).setResource(replacementObject.getResource());
						mGameAssetRows.get(position).getObjectAtPosition(7).setResourceName(replacementObject.getResourceName());
						mGameAssetRows.get(position).getObjectAtPosition(7).setType(replacementObject.getType());
						
						Object7.setImageResource(
								mGameAssetRows.get(position).getObjectAtPosition(7).getResource());
					}
				}
			});
			
			tile0.setImageResource(
					mGameAssetRows.get(position).getTileAtPosition(0).getResource());
			tile1.setImageResource(
					mGameAssetRows.get(position).getTileAtPosition(1).getResource());
			tile2.setImageResource(
					mGameAssetRows.get(position).getTileAtPosition(2).getResource());
			tile3.setImageResource(
					mGameAssetRows.get(position).getTileAtPosition(3).getResource());
			tile4.setImageResource(
					mGameAssetRows.get(position).getTileAtPosition(4).getResource());
			tile5.setImageResource(
					mGameAssetRows.get(position).getTileAtPosition(5).getResource());
			tile6.setImageResource(
					mGameAssetRows.get(position).getTileAtPosition(6).getResource());
			tile7.setImageResource(
					mGameAssetRows.get(position).getTileAtPosition(7).getResource());
			
			Object0.setImageResource(
					mGameAssetRows.get(position).getObjectAtPosition(0).getResource());
			Object1.setImageResource(
					mGameAssetRows.get(position).getObjectAtPosition(1).getResource());
			Object2.setImageResource(
					mGameAssetRows.get(position).getObjectAtPosition(2).getResource());
			Object3.setImageResource(
					mGameAssetRows.get(position).getObjectAtPosition(3).getResource());
			Object4.setImageResource(
					mGameAssetRows.get(position).getObjectAtPosition(4).getResource());
			Object5.setImageResource(
					mGameAssetRows.get(position).getObjectAtPosition(5).getResource());
			Object6.setImageResource(
					mGameAssetRows.get(position).getObjectAtPosition(6).getResource());
			Object7.setImageResource(
					mGameAssetRows.get(position).getObjectAtPosition(7).getResource());
				
			return v;
		}
		
		@Override
		public GameRow getItem(int position) {
			return super.getItem(position);
		}
		
//		private void highlightTile(ImageView selectedTile) {
//			if(previouslySelectedTile != null) {
//				previouslySelectedTile.setColorFilter(null);
//			}
//			selectedTile.setColorFilter(getResources().getColor(R.color.gamefly_orange), Mode.LIGHTEN);
//			previouslySelectedTile = selectedTile;
//		}
	}
}
