package com.wojciechkolendo.appcrashhandler.sample;

import android.app.Application;

import com.wojciechkolendo.appcrashhandler.AppCrashHandler;

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
