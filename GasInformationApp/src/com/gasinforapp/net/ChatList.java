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
import com.gasinforapp.bean.ChatItem;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

/**
 * getting Chat List 获取聊天列表接口
 * 
 * @author zm
 */
public class ChatList {
	public ChatList(final String account, final String token,
			final int groupid, final int page,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL_GROUP + MyConfig.ACTION_GETCHATLIST,
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
									List<ChatItem> chatList = new ArrayList<ChatItem>();
									JSONArray chatArray = obj
											.optJSONArray(MyConfig.KEY_CHATS);
									for (int i = 0; i < chatArray.length(); i++) {
										JSONObject obj2 = chatArray
												.optJSONObject(i);

										String userName = obj2
												.optString(MyConfig.KEY_USERNAME);
										String content = obj2
												.optString(MyConfig.KEY_CONTENT);
										String sendtime = obj2
												.optString(MyConfig.KEY_SENDTIME);

										ChatItem ct = new ChatItem();
										ct.setContent(content);
										ct.setSendtime(sendtime);
										ct.setUserName(userName);
										chatList.add(ct);
									}
									successCallback.onSuccess(chatList);
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
							Log.e("JsonError", "ChatList.java");
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
						Log.e("TagChat", error.getMessage());
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
				return map;
			}

		};
		System.out.println(stringRequest.getUrl());
		stringRequest.setTag("getChatList");
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	public static interface SuccessCallback {
		void onSuccess(List<ChatItem> chatList);
	}

	public static interface FailCallback {
		void onFail(int ErrorCode);
	}
}
