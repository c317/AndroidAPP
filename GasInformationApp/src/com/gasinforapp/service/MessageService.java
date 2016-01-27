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

public class MessageService extends Service {
	private static final String TAG = "MessageService";
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
		netStatus = new NetStatus(MessageService.this);
		dataBaseHelper = GasInforDataBaseHelper
				.getDatebaseHelper(MessageService.this);
		startQueryThread();// 调用方法启动线程
		return super.onStartCommand(intent, flags, startId);
	}

	public void startQueryThread() {
		new Thread() {
			private String lastTimeOfNotice;
			private String lastTimeOfHotNews;
			private String lastTimeOfGroupNews;

			public void run() {
				while (true) {
					if (netStatus.getNetWorkStatus()) {
						try {// 睡眠一段时间
							Thread.sleep(5000);
							Log.i(TAG, "lastTimeOfNotice:" + lastTimeOfNotice);
							lastTimeOfNotice = dataBaseHelper
									.getLastTimeOfNotice();
							frequentQuery(
									MyConfig.MODULEID_NOTICE,
									lastTimeOfNotice,
									MyConfig.getCachedUserid(MessageService.this));
							lastTimeOfHotNews = dataBaseHelper
									.getLastTimeOfHotNews();
							Log.i(TAG, "lastTimeOfHotNews:" + lastTimeOfHotNews);
							frequentQuery(MyConfig.MODULEID_HOTNEWS,
									lastTimeOfHotNews, -1);
							// lastTimeOfGroupNews =
							// dataBaseHelper.getLastTimeOfGroupNews();
							// frequentQuery(MyConfig.MODULEID_GROUPNEWS,lastTimeOfGroupNews);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Log.i(TAG, "开始消息轮询服务");
					} else {
						Log.i(TAG, "未开启手机网络");
					}
				}
			}

		}.start();
	}

	/**
	 * 轮询
	 * 
	 * @param moduleId
	 * @param lastTime
	 * @param userId
	 *            为-1时表示该接口不需要userId参数
	 */
	public void frequentQuery(final int moduleId, final String lastTime,
			final int userId) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL + MyConfig.ACTION_FREGUENTQUERY,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						System.out.println(response);
						try {
							JSONObject obj = new JSONObject(response);
							if (obj.getInt(MyConfig.KEY_STATUS) == MyConfig.RESULT_STATUS_SUCCESS) {
								switch (moduleId) {
								case MyConfig.MODULEID_NOTICE:
									analyzeJsonData(MyConfig.MODULEID_NOTICE,
											obj);
									break;
								case MyConfig.MODULEID_HOTNEWS:
									analyzeJsonData(MyConfig.MODULEID_HOTNEWS,
											obj);
									break;
								case MyConfig.MODULEID_GROUPNEWS:
									analyzeJsonData(
											MyConfig.MODULEID_GROUPNEWS, obj);
									break;
								default:
									break;
								}
							} else {
								Log.i(TAG, "Action返回值异常！");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.MOUDLEID, moduleId + "");
				map.put(MyConfig.LASTTIME, lastTime);
				if (moduleId == MyConfig.MODULEID_NOTICE) {
					map.put(MyConfig.USERID, userId + "");
				}
				return map;
			}
		};
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	/**
	 * 解析json数据以及将数据插入SQLite数据库
	 */
	private void analyzeJsonData(int moduleId, JSONObject obj)
			throws JSONException {
		JSONObject newsObject;
		JSONArray newsJsonArray;
		switch (moduleId) {
		case MyConfig.MODULEID_NOTICE:
			ArrayList<NoticeDTO> noticeDTOs = new ArrayList<NoticeDTO>();
			newsJsonArray = obj.getJSONArray(MyConfig.KEY_NOTICES);
			NoticeDTO noticeDTO;
			for (int i = 0; i < newsJsonArray.length(); i++) {
				newsObject = newsJsonArray.getJSONObject(i);
				noticeDTO = new NoticeDTO();
				noticeDTO.setId(newsObject.getInt(MyConfig.NEWS_ID));
				noticeDTO.setTitle(newsObject.getString(MyConfig.NEWS_TITLE));
				noticeDTO.setContent(newsObject
						.getString(MyConfig.NEWS_CONTENT));
				noticeDTO.setSource(newsObject.getString(MyConfig.NEWS_SOURCE));
				noticeDTO.setPublisher(newsObject
						.getString(MyConfig.NEWS_PUBLISHER));
				noticeDTO.setTime(newsObject.getString(MyConfig.NEWS_TIME));
				noticeDTO.setRead(0);
				noticeDTOs.add(noticeDTO);
			}
			dataBaseHelper.batchInsertNotice(noticeDTOs);
			break;
		case MyConfig.MODULEID_HOTNEWS:
			ArrayList<HotNewsDTO> hotNewsDTOs = new ArrayList<HotNewsDTO>();
			newsJsonArray = obj.getJSONArray(MyConfig.KEY_NEWS);
			HotNewsDTO hotNewsDTO;
			for (int i = 0; i < newsJsonArray.length(); i++) {
				newsObject = newsJsonArray.getJSONObject(i);
				hotNewsDTO = new HotNewsDTO();
				hotNewsDTO.setId(newsObject.getInt(MyConfig.NEWS_ID));
				hotNewsDTO.setTitle(newsObject.getString(MyConfig.NEWS_TITLE));
				hotNewsDTO.setContent(newsObject
						.getString(MyConfig.NEWS_CONTENT));
				hotNewsDTO
						.setSource(newsObject.getString(MyConfig.NEWS_SOURCE));
				hotNewsDTO.setLastTime(newsObject
						.getString(MyConfig.NEWS_LASTTIME));
				hotNewsDTO.setPubTime(newsObject
						.getString(MyConfig.NEWS_PUBTIME));
				hotNewsDTO.setRead(0);
				hotNewsDTOs.add(hotNewsDTO);
			}
			dataBaseHelper.batchInsertHotNews(hotNewsDTOs);
			break;
		case MyConfig.MODULEID_GROUPNEWS:
			ArrayList<GroupNewsDTO> groupNewsDTOs = new ArrayList<GroupNewsDTO>();
			newsJsonArray = obj.getJSONArray(MyConfig.KEY_NEWS);
			GroupNewsDTO groupNewsDTO;
			for (int i = 0; i < newsJsonArray.length(); i++) {
				newsObject = newsJsonArray.getJSONObject(i);
				groupNewsDTO = new GroupNewsDTO();
				groupNewsDTO.setGroupName(newsObject
						.getString(MyConfig.NEWS_GROUPNAME));
				groupNewsDTO.setGroupName(newsObject
						.getString(MyConfig.NEWS_USERNAME));
				groupNewsDTO.setContent(newsObject
						.getString(MyConfig.NEWS_CONTENT));
				groupNewsDTO.setTime(newsObject.getString(MyConfig.NEWS_TIME));
				groupNewsDTO.setRead(0);
				groupNewsDTOs.add(groupNewsDTO);
			}
			dataBaseHelper.batchInsertGroupNews(groupNewsDTOs);
			break;
		default:
			break;
		}
	}

}
