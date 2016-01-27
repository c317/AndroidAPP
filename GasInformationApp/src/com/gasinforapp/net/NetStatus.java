package com.gasinforapp.net;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetStatus {
	private Context mActivity;

	public NetStatus(Context activity) {
		mActivity = activity;
	}

	/**
	 * 网络状态判断
	 */
	public boolean getNetWorkStatus() {
		boolean netSataus = false;
		ConnectivityManager cwjManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();
		if (cwjManager.getActiveNetworkInfo() != null) {
			netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
		}
		return netSataus;
	}
}
