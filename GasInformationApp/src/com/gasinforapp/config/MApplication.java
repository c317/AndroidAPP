package com.gasinforapp.config;

import android.app.Application;
import android.content.Context;

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

}
