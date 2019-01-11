package com.humandevice.android.appcrashhandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;


public class ExceptionActivity extends Activity {

	public static final String EXTRA_EXCEPTION = "extra_exception";
	private final String NEW_LINE = "\n";
	private final String SPACE = " ";
	private String DEVICE;
	private String APPLICATION;
	private String EXCEPTION;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Throwable e = (Throwable) getIntent().getSerializableExtra(EXTRA_EXCEPTION);

		DEVICE = getString(R.string.app_crash_handler_device);
		APPLICATION = getString(R.string.app_crash_handler_application);
		EXCEPTION = getString(R.string.app_crash_handler_exception);

		SpannableStringBuilder builder = new SpannableStringBuilder();
		int start = 0;
		appendDevice(builder);
		builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, start + DEVICE.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.append(NEW_LINE).append(NEW_LINE);
		start = builder.length();
		appendApplication(builder);
		builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, start + APPLICATION.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.append(NEW_LINE).append(NEW_LINE);
		start = builder.length();
		appendException(e, builder);
		builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, start + EXCEPTION.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		final String message = builder.toString();

		new AlertDialog.Builder(ExceptionActivity.this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
				.setTitle(getString(R.string.app_crash_handler_title, getApplicationName(getApplicationContext())))
				.setMessage(builder)
				.setPositiveButton(getString(R.string.app_crash_handler_send_report), (dialogInterface, i) -> {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("plain/text");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(Intent.EXTRA_TEXT, "/code " + message);
					startActivity(intent);
					finish();
				})

				.setNeutralButton(getString(R.string.app_crash_handler_close), (dialogInterface, i) -> finishAffinity())
				.show();
	}

	private void appendDevice(SpannableStringBuilder builder) {
		builder.append(DEVICE).append(NEW_LINE);
		if (!TextUtils.isEmpty(Build.BRAND)) {
			builder.append(Build.BRAND).append(SPACE);
		}
		if (!TextUtils.isEmpty(Build.MANUFACTURER)) {
			builder.append(Build.MANUFACTURER).append(SPACE);
		}
		if (!TextUtils.isEmpty(Build.MODEL)) {
			builder.append(Build.MODEL).append(SPACE);
		}
		if (!TextUtils.isEmpty(Build.DEVICE)) {
			builder.append('(').append(Build.DEVICE).append(')').append(SPACE);
		}
		builder.append(NEW_LINE)
				.append("Android ").append(Build.VERSION.RELEASE)
				.append(" (Api ").append(String.valueOf(Build.VERSION.SDK_INT)).append(")");
	}

	private void appendApplication(SpannableStringBuilder builder) {
		builder.append(APPLICATION).append(NEW_LINE)
				.append(getApplicationName(getApplicationContext())).append(SPACE)
				.append(getApplicationVersion(getApplicationContext()));
	}

	private void appendException(Throwable e, SpannableStringBuilder builder) {
		builder.append(EXCEPTION).append(NEW_LINE);
		builder.append(Log.getStackTraceString(e));
	}

	public static String getApplicationName(Context context) {
		int stringId = context.getApplicationInfo().labelRes;
		return context.getString(stringId);
	}

	public String getApplicationVersion(Context context) {
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return "v" + pInfo.versionName + "(" + pInfo.versionCode + ")";
		} catch (PackageManager.NameNotFoundException e) {
			return "";
		}
	}
}
