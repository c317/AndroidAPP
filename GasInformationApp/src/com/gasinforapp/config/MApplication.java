package com.gasinforapp.config;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

/**
 * Created by zm on 16/1/12
 */
public class MApplication extends Application {
	private Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		initialize();
		recordDownPath();
	}

	private void initialize() {
		initRequestQueue();
	}

	private void initRequestQueue() {
		VolleyUtil.initialize(mContext);
	}

	private static String appUserAccount = "admin";

	public static String getAppUserName() {
		return appUserAccount;
	}

	public static void setAppUserName(String appUserName) {
		MApplication.appUserAccount = appUserName;
	}

	private void recordDownPath() {
		String sdcard = Environment.getExternalStorageDirectory() + "/";

		MyConfig.APP_PATH = sdcard;
		MyConfig.APP_DOWNPATH = MyConfig.APP_PATH + "down/";
	}

}
