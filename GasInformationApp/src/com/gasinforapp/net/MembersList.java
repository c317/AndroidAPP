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
import com.gasinforapp.bean.User;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

/**
 *  获取用户列表接口
 * 
 * @author zm
 * 
 */
public class MembersList {
	/**
	 * get memberlist have been in group given groupID
	 * @param account
	 * @param token
	 * @param groupID
	 * @param successCallback
	 * @param failCallback
	 */
	public MembersList(final String account, final String token,
			final int groupID, final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL_GROUP + MyConfig.ACTION_GETMEMBERS,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						System.out.println(response);
						try {
							JSONObject object = new JSONObject(response);
							switch (object.getInt(MyConfig.KEY_STATUS)) {
							case MyConfig.RESULT_STATUS_SUCCESS:
								List<User> memberList = new ArrayList<User>();
								JSONArray memArray = object
										.optJSONArray(MyConfig.KEY_MEMBERS);
								for (int i = 0; i < memArray.length(); i++) {
									JSONObject obj = memArray.getJSONObject(i);
									String userName = obj
											.optString(MyConfig.KEY_USERNAME);
									String userAccount = obj
											.optString(MyConfig.KEY_USER_ACCOUNT);
									int userid = obj
											.optInt(MyConfig.KEY_USERID);
									String department = obj
											.optString(MyConfig.KEY_DEPARTMENT);
									String job = obj
											.optString(MyConfig.KEY_JOB);

									User us = new User();
									us.setUserID(userid);
									us.setDepartment(department);
									us.setJob(job);
									us.setAccount(userAccount);
									us.setUserName(userName);

									memberList.add(us);
								}
								successCallback.onSuccess(memberList);

								break;
							default:
								if (failCallback != null)
									failCallback
											.onFail(MyConfig.RESULT_STATUS_FAIL);
								break;
							}

						} catch (JSONException e) {
							Log.e("errortag", "jsonMemberlist");
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
						Log.e("MemberlistTag", error.getMessage());
						if (failCallback != null) {
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
						}
					}
				}) {
			/**
			 * post请求参数
			 */
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_USER_ACCOUNT, account);
				map.put(MyConfig.KEY_TOKEN, token);
				map.put(MyConfig.KEY_GROUPID, groupID + "");
				return map;
			}

		};
		System.out.println(stringRequest.getUrl());
		stringRequest.setTag("getMemberListPost");
		VolleyUtil.getRequestQueue().add(stringRequest);
	}
	/**
	 * get memberlist not jion group given groupID
	 * @param groupID
	 * @param account
	 * @param token
	 * @param successCallback
	 * @param failCallback
	 */
	public MembersList(final int groupID,final String account, final String token,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				 MyConfig.SERVER_URL_GROUP + MyConfig.ACTION_GETALLUSER,
				new Response.Listener<String>() {
			
			@Override
			public void onResponse(String response) {
				System.out.println(response);
				try {
					JSONObject object = new JSONObject(response);
					switch (object.getInt(MyConfig.KEY_STATUS)) {
					case MyConfig.RESULT_STATUS_SUCCESS:
						List<User> memberList = new ArrayList<User>();
						JSONArray memArray = object
								.optJSONArray(MyConfig.KEY_MEMBERS);
						for (int i = 0; i < memArray.length(); i++) {
							JSONObject obj = memArray.getJSONObject(i);
							String userName = obj
									.optString(MyConfig.KEY_USERNAME);
							String userAccount = obj
									.optString(MyConfig.KEY_USER_ACCOUNT);
							int userid = obj
									.optInt(MyConfig.KEY_USERID);
							String department = obj
									.optString(MyConfig.KEY_DEPARTMENT);
							String job = obj
									.optString(MyConfig.KEY_JOB);
							
							User us = new User();
							us.setUserID(userid);
							us.setDepartment(department);
							us.setJob(job);
							us.setAccount(userAccount);
							us.setUserName(userName);
							
							memberList.add(us);
						}
						successCallback.onSuccess(memberList);
						
						break;
					default:
						if (failCallback != null)
							failCallback
							.onFail(MyConfig.RESULT_STATUS_FAIL);
						break;
					}
					
				} catch (JSONException e) {
					Log.e("errortag", "jsonMemberlist");
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
				Log.e("MemberlistTag", error.getMessage());
				if (failCallback != null) {
					failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
				}
			}
		}) {
			/**
			 * post请求参数
			 */
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_USER_ACCOUNT, account);
				map.put(MyConfig.KEY_TOKEN, token);
				map.put(MyConfig.KEY_GROUPID, groupID + "");
				return map;
			}
			
		};
		System.out.println(stringRequest.getUrl());
		stringRequest.setTag("getMemberListPost");
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	public interface SuccessCallback {
		void onSuccess(List<User> members);
	}

	public interface FailCallback {
		void onFail(int ErrorCode);
	}
}
