package org.akop.airjag;

import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
	private boolean mRunning;
	private GameView mView;
	private SurfaceHolder mSurfaceHolder;

	public GameLoop(SurfaceHolder holder, GameView view) {
		mRunning = false;
		mSurfaceHolder = holder;
		mView = view;
	}

	public void stopGameLoop() {
		mRunning = false;
	}

	@Override
	public void run() {
		mRunning = true;
int foo = 0;
		while (mRunning) {
			Log.v("*", String.format("Incr %d", foo++));
		}

		Log.v("*", "Game loop exited");
	}
}
