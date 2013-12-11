package org.akop.airjag;

import org.akop.airjag.model.GameMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends Activity {

	private GameView mView;
	private GameMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, 
//        		LayoutParams.FLAG_FULLSCREEN);

		mMap = getIntent().getParcelableExtra("map");
		mView = new GameView(this, mMap);

		setContentView(mView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_load_level:
			LevelSelectorActivity.launch(this);
			return true;
		}

		return false;
	}

	public static void launch(Context context, GameMap map) {
		Intent intent = new Intent(context, GameActivity.class);
		intent.putExtra("map", map);
		context.startActivity(intent);
	}
}
