package org.akop.airjag;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends Activity {

	private GameView mView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, 
//        		LayoutParams.FLAG_FULLSCREEN);

		mView = new GameView(this);
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
}
