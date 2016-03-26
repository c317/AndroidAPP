package com.gasinforapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gasinforapp.bean.Group;
import com.gasinforapp.bean.GroupNewsDTO;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.bean.NoticeDTO;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.gasinforapp.net.GroupList;
import com.gasinforapp.net.NetStatus;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MessageService extends Service {
	private static final String TAG = "MessageService";
	private NetStatus netStatus;
	private GasInforDataBaseHelper dataBaseHelper;
	private static int groupIds[];

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
		dataBaseHelper = GasInforDataBaseHelper.getDatebaseHelper(MessageService.this);
		getGroupIds();
		return super.onStartCommand(intent, flags, startId);
	}
	/**
	 * 得到该用户所有所在群的id，第三个参数使这里只能得到规定页码的群数量
	 */
	private void getGroupIds(){
		new GroupList(MyConfig.getCachedAccount(this), MyConfig.getCachedToken(this), 1, 0, new GroupList.SuccessCallback() {
			@Override
			public void onSuccess(int page, int perpage, List<Group> groupList) {
				int groupIds[] = new int[groupList.size()];
				for(int i = 0 ; i< groupList.size() ; i++){
					groupIds[i] = groupList.get(i).getGroupID();
					Log.i(TAG, "groupIds:" + groupIds[i]);
				}
				startQueryThread(groupIds);// 调用方法启动线程
			}
		}, new GroupList.FailCallback() {
			@Override
			public void onFail(int errorCode) {
				Log.e(TAG, "未得到grouplist");
				getGroupIds();
			}
		});
	}
	/**
	 * 启动轮询线程
	 * @param groupIds 用户所属群的所有群Id
	 */

	public void startQueryThread(final int[] groupIds) {
		new Thread() {
			private String lastTimeOfNotice;
			private String lastTimeOfHotNews;
			private String lastTimeOfGroupNews;
			private int time = 50;
			public void run() {
				while (true) {
					if (netStatus.getNetWorkStatus()) {
						try {
							if(time%50==0){
								lastTimeOfNotice = dataBaseHelper.getLastTimeOfNotice();
								Log.i(TAG, "lastTimeOfNotice:" + lastTimeOfNotice);
								frequentQuery(MyConfig.MODULEID_NOTICE,null,lastTimeOfNotice,MyConfig.getCachedUserid(getApplicationContext()));
								
								lastTimeOfHotNews = dataBaseHelper.getLastTimeOfHotNews();
								Log.i(TAG, "lastTimeOfHotNews:" + lastTimeOfHotNews);
								frequentQuery(MyConfig.MODULEID_HOTNEWS,null,lastTimeOfHotNews, null);
								time=50;
							}			
							String groupIdString = "";
							for(int i=0;i<groupIds.length;i++){
								groupIdString = groupIdString +groupIds[i]+",";
							}
							groupIdString = groupIdString.substring(0, groupIdString.length()-1);
							lastTimeOfGroupNews =dataBaseHelper.getLastTimeOfGroupNews(groupIds);
							Log.i(TAG, "lastTimeOfGroupNews:" + lastTimeOfGroupNews+"  groupIdString:"+groupIdString);
							frequentQuery(MyConfig.MODULEID_GROUPNEWS,groupIdString,lastTimeOfGroupNews,null);
							Thread.sleep(1000);
							time++;

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
	 * 
	 * @param moduleId
	 * @param groupIds 为null时表示该接口不需要groupIds参数
	 * @param lastTime
	 * @param userId 为null时表示该接口不需要userId参数
	 */
	public void frequentQuery(final int moduleId, final String groupIds, final String lastTime,final Integer userId) {
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
									analyzeJsonData(MyConfig.MODULEID_NOTICE,obj);
									break;
								case MyConfig.MODULEID_HOTNEWS:
									analyzeJsonData(MyConfig.MODULEID_HOTNEWS,obj);
									break;
								case MyConfig.MODULEID_GROUPNEWS:
									analyzeJsonData(MyConfig.MODULEID_GROUPNEWS, obj);
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
				if (moduleId == MyConfig.MODULEID_HOTNEWS) {
					map.put(MyConfig.LASTTIME, lastTime);
				}else if (moduleId == MyConfig.MODULEID_NOTICE) {
					map.put(MyConfig.USERID, userId + "");
					map.put(MyConfig.LASTTIME, lastTime);
				}else if(moduleId == MyConfig.MODULEID_GROUPNEWS){
					map.put(MyConfig.GROUPIDS, groupIds);
					map.put(MyConfig.LASTTIMES, lastTime);
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
			newsJsonArray = obj.getJSONArray(MyConfig.MESSAGE_GROUPS);
			GroupNewsDTO groupNewsDTO;
			for (int i = 0; i < newsJsonArray.length(); i++) {
				newsObject = newsJsonArray.getJSONObject(i);
				groupNewsDTO = new GroupNewsDTO();
				groupNewsDTO.setGroupId(newsObject.getInt(MyConfig.NEWS_GROUPID));
				groupNewsDTO.setUserName(newsObject.getString(MyConfig.NEWS_USERNAME));
				groupNewsDTO.setContent(newsObject.getString(MyConfig.MESSAGE_GROUP));
				groupNewsDTO.setKind(newsObject.getInt(MyConfig.NEWS_KIND));
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
