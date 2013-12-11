package org.akop.airjag;

import org.akop.airjag.model.GameMap;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements
		SurfaceHolder.Callback {

	private GameLoop mGameLoop;

	public GameView(Context context, GameMap map) {
		super(context);

		mGameLoop = new GameLoop(context, getHolder(), map);

		getHolder().addCallback(this);
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mGameLoop.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mGameLoop.stopGameLoop();

		boolean retry = true;
		while (retry) {
			try {
				mGameLoop.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}

		mGameLoop.cleanUp();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
}
