package com.gasinforapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gasinforapp.bean.GroupNewsDTO;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.bean.NoticeDTO;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.gasinforapp.net.NetStatus;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GroupMessageService extends Service {
	private static final String TAG = "GroupMessageService";
	private NetStatus netStatus;
	private GasInforDataBaseHelper dataBaseHelper;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {// 重写onCreate方法
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {// 重写onStartCommand方法
		netStatus = new NetStatus(GroupMessageService.this);
		dataBaseHelper = GasInforDataBaseHelper
				.getDatebaseHelper(GroupMessageService.this);
		startQueryThread();// 调用方法启动线程
		return super.onStartCommand(intent, flags, startId);
	}

	public void startQueryThread() {
		new Thread() {

			public void run() {
				while (true) {
					if (netStatus.getNetWorkStatus()) {
						try {// 睡眠一段时间
							Thread.sleep(5000);
							//
						} catch (Exception e) {
							e.printStackTrace();
						}
						Log.i(TAG, "开始更新群消息轮询服务");
					} else {
						Log.i(TAG, "未开启手机网络");
						try {// 睡眠一段时间
							Thread.sleep(5000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		}.start();
	}

	/**
	 * 轮询
	 * 
	 */
	public void frequentQuery() {
		
	}
		

}
