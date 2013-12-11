package org.akop.airjag;

import android.app.Application;

public class AirJag extends Application {

	private static AirJag sInstance;

	public void onCreate() {
		super.onCreate();

		sInstance = this;
	}

	public static AirJag getInstance() {
		return sInstance;
	}
}
