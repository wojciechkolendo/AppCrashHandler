package com.humandevice.android.appcrashhandler;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;

import software.rsquared.androidlogger.Logger;


/**
 * Uncaught exception handler. This handler is invoked in case any Thread dies due to an unhandled exception.
 */
public class AppCrashHandler implements Thread.UncaughtExceptionHandler {

	private static Application context;

	/**
	 * Sets as the default uncaught exception handler. This handler is invoked in case any Thread dies due to an unhandled exception.
	 */
	public static void setAsDefault() {
		Thread.setDefaultUncaughtExceptionHandler(new AppCrashHandler());
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		Logger.error(throwable);
		Intent crashedIntent = new Intent(getContext(), ExceptionActivity.class);
		crashedIntent.putExtra(ExceptionActivity.EXTRA_EXCEPTION, throwable);
		crashedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		getContext().startActivity(crashedIntent);
	}

	@SuppressLint("PrivateApi")
	private static Application getContext() {
		if (context == null) {
			try {
				context = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return context;
	}

}
