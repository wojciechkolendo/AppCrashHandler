package com.humandevice.android.appcrashhandler.sample;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		float a = 5/i;
	}
}
