package com.gasinforapp.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gasinforapp.bean.Group;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

public class GroupList {
	private static final String TAG = "GroupList";
	/**
	 * 连接后台得到群组列表
	 * @author zm
	 */
	public GroupList(final String account, final String token, final int page,final int perpage,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL_GROUP+MyConfig.ACTION_GETGROUPLIST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {												
						System.out.println(response);
						try {
						Log.d(TAG, "response:"+response);
						JSONObject obj = new JSONObject(response);

						switch (obj.optInt(MyConfig.KEY_STATUS)) {
						case MyConfig.RESULT_STATUS_SUCCESS:
							if (successCallback != null) {
								List<Group> groupList = new ArrayList<Group>();
								JSONArray groupJsonArray = obj
										.optJSONArray(MyConfig.KEY_GROUPS);
								for (int i = 0; i < groupJsonArray.length(); i++) {
									JSONObject groupObject = groupJsonArray
											.optJSONObject(i);
									String groupName = groupObject
											.optString(MyConfig.KEY_GROUPNAME);
									int groupID=groupObject.optInt(MyConfig.KEY_GROUPID);
									int noreadnum = groupObject.optInt(MyConfig.KEY_NOREADNUM);
									
									Group gr = new Group();
									gr.setGroupName(groupName);
									gr.setGroupID(groupID);
									gr.setNoreadnum(noreadnum);
									groupList.add(gr);
								}

								successCallback.onSuccess(
										obj.optInt(MyConfig.KEY_PAGE),        /* 这里使用optInt()代替getInt()*/
										obj.optInt(MyConfig.KEY_PERPAGE),
										groupList);
								// successCallback.onSuccess(1, 20,
								// newsList);
							}
							break;
						case MyConfig.RESULT_STATUS_INVALID_TOKEN:
							if (failCallback != null) {
								failCallback
										.onFail(MyConfig.RESULT_STATUS_INVALID_TOKEN);
							}
							break;
						default:
							if (failCallback != null) {
								failCallback
										.onFail(MyConfig.RESULT_STATUS_FAIL);
							}
							break;
						}

					} catch (JSONException e) {
						e.printStackTrace();
						if (failCallback != null) {
							failCallback
									.onFail(MyConfig.RESULT_STATUS_FAIL);
						}
					}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, error.getMessage(), error);
						if (failCallback != null) {
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
						}
					}
				}) {
		
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_USER_ACCOUNT, account);
				map.put(MyConfig.KEY_TOKEN, token);
				map.put(MyConfig.KEY_PAGE, page + "");
				map.put(MyConfig.KEY_PERPAGE, perpage + "");
				return map;
			}

			
		};
		System.out.println(stringRequest.getUrl());
		
//		stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
		stringRequest.setTag("getGroupListpost");
		VolleyUtil.getRequestQueue().add(stringRequest);
		

	}

	public static interface SuccessCallback {
		void onSuccess(int page, int perpage, List<Group> groupList);
	}

	public static interface FailCallback {
		void onFail(int errorCode);
	}

}