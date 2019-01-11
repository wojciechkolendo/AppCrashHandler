package com.humandevice.android.appcrashhandler.sample;

import android.app.Application;

import com.humandevice.android.appcrashhandler.AppCrashHandler;

/**
 * @author Rafal Zajfert
 */
public class MyApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		if (BuildConfig.DEBUG) {
			AppCrashHandler.setAsDefault();
		}
	}
}
