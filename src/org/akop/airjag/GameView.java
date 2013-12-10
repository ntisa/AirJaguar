package org.akop.airjag;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements
		SurfaceHolder.Callback {

	private GameLoop mGameLoop;

	public GameView(Context context) {
		super(context);

		mGameLoop = new GameLoop(getHolder(), this);

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
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
	}
}

