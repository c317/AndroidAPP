package com.gasinforapp.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gasinforapp.bean.Notice;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

/**
 *  getting  NoticeList
 *  获取通知列表接口
 * @author zm
 */
public class NoticeList {
	public NoticeList(final String account, final String token,
			final int groupid, final int page, final int perpage,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL_GROUP + MyConfig.ACTION_GETNOTICELIST,
				new Response.Listener<String>() {
					// 成功回调
					@Override
					public void onResponse(String response) {
						System.out.println(response);
						try {
							JSONObject obj = new JSONObject(response);
							switch (obj.optInt(MyConfig.KEY_STATUS)) {
							case MyConfig.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									List<Notice> noticeList = new ArrayList<Notice>();
									JSONArray noticeArray = obj
											.optJSONArray(MyConfig.KEY_NOTICES);
									for (int i = 0; i < noticeArray.length(); i++) {
										JSONObject obj2 = noticeArray
												.optJSONObject(i);
										int id = obj2
												.optInt(MyConfig.KEY_NOTICEID);
										int sendGroup = obj2
												.optInt(MyConfig.KEY_SENDGROUP);
										int getGroup = obj2
												.optInt(MyConfig.KEY_GETGROUP);
										String title = obj2
												.optString(MyConfig.KEY_NOTICE_TITLE);
										String content = obj2
												.optString(MyConfig.KEY_CONTENT);
										String sendtime = obj2
												.optString(MyConfig.KEY_SENDTIME);

										Notice nc = new Notice();
										nc.setTitle(title);
										nc.setNotiID(id);
										nc.setContent(content);
										nc.setSendGroup(sendGroup);
										nc.setGetGroup(getGroup);
										nc.setSendtime(sendtime);

										noticeList.add(nc);
									}
									successCallback.onSuccess(page, perpage,
											noticeList);
								}
								break;
							case MyConfig.RESULT_STATUS_INVALID_TOKEN:
								if (failCallback != null)
									failCallback
											.onFail(MyConfig.RESULT_STATUS_INVALID_TOKEN);
								break;
							default:
								if (failCallback != null)
									failCallback
											.onFail(MyConfig.RESULT_STATUS_FAIL);
								break;
							}
						} catch (JSONException e) {
							Log.e("JsonError", "NoticeList.java");
							e.printStackTrace();
							if (failCallback != null)
								failCallback
										.onFail(MyConfig.RESULT_STATUS_FAIL);
						}

					}
				}, new Response.ErrorListener() {
					// 失败回调
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TagNotice", error.getMessage());
						if (failCallback != null) {
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
						}
					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// post请求的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_USER_ACCOUNT, account);
				map.put(MyConfig.KEY_TOKEN, token);
				map.put(MyConfig.KEY_GROUPID, groupid + "");
				map.put(MyConfig.KEY_PAGE, page + "");
				map.put(MyConfig.KEY_PERPAGE, perpage + "");
				return map;
			}

		};
		System.out.println(stringRequest.getUrl());
		stringRequest.setTag("getNoticeList");
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	public static interface SuccessCallback {
		void onSuccess(int page, int perpage, List<Notice> noticeList);
	}

	public static interface FailCallback {
		void onFail(int ErrorCode);
	}
}
