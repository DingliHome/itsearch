package com;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		SDKInitializer.initialize(this);
		super.onCreate();
	}
}
