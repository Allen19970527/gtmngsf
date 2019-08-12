package com.example.gsf98.notebook;

import android.app.Application;

import org.xutils.x;

public class MoodiaryApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);
		x.Ext.setDebug(BuildConfig.DEBUG);
	}

}
