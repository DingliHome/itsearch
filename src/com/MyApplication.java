package com;

import com.baidu.mapapi.SDKInitializer;

import Helper.LogHelper;
import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		SDKInitializer.initialize(this);
		LogHelper.getInstance(this).start();
		super.onCreate();
	}
}
