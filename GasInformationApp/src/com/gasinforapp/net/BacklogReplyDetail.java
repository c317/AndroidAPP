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
import com.gasinforapp.bean.Affairs;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

public class BacklogReplyDetail {
	/**
	 * 连接到后台办公回复详情
	 * 
	 * @param userId
	 *            当前用户职工编号
	 * @param token
	 *            安全标识码
	 * @param approverId
	 *            审批者Id
	 * @param itemId
	 *            事项编号
	 * @param successCallback
	 *            成功回调接口 实例化时写获取办公事项详情成功后的操作
	 * @param failCallback
	 *            失败回调接口 实例化时写获取办公事项详情失败后的操作
	 */

	public BacklogReplyDetail(final String userId, final String token,
			final String approverId, final String itemId,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(
				Request.Method.POST,
				MyConfig.SERVER_URL_OA + MyConfig.ACTION_CHECKOFFICEREPLYDETAIL,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						System.out.println(response);

						try {
							JSONObject obj = new JSONObject(response);

							switch (obj.optInt(MyConfig.KEY_STATUS)) {
							// 连接成功
							case MyConfig.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									// 事项的编号、请求人id、审批人id、请求时间、文本内容、图片url、部门是以JSON数组的格式
									JSONObject newsObject = obj
											.optJSONObject(MyConfig.KEY_AFFAIR_RPCONTENT);
									String itemId = newsObject
											.optString(MyConfig.KEY_AFFAIRS_ITEMID);
									String requestId = newsObject
											.optString(MyConfig.KEY_AFFAIRS_REQUESTERID);
									String approverId = newsObject
											.optString(MyConfig.KEY_AFFAIRS_APPROVERID);
									String requestTime = newsObject
											.optString(MyConfig.KEY_AFFAIRS_REQUESTTIME);
									String textContent = newsObject
											.optString(MyConfig.KEY_AFFAIRS_MESSAGE);
									String pictureUrl = newsObject
											.optString(MyConfig.KEY_AFFAIRS_PICTURES_URL);
									String picture = newsObject
											.optString(MyConfig.KEY_AFFAIRS_PICNAME);
									String department = newsObject
											.optString(MyConfig.KEY_AFFAIRS_DEPARTMENT);
									String comment = newsObject
											.optString(MyConfig.KEY_AFFAIRS_COMMENT);
									Affairs as = new Affairs();
									as.setRequestTitle(itemId);
									as.setRequesterId(requestId);
									as.setApproverId(approverId);
									as.setRequestTime(requestTime);
									as.setTextContent(textContent);
									as.setPicURL(pictureUrl);
									as.setPictures(picture);
									as.setDepartment(department);
									as.setComment(comment);
									successCallback.onSuccess(as);
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
						Log.e("TAG", error.getMessage(), error);
						if (failCallback != null)
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_USERID, userId + "");
				map.put(MyConfig.KEY_TOKEN, token + "");
				map.put(MyConfig.KEY_AFFAIRS_APPROVERID, approverId + "");
				map.put(MyConfig.KEY_AFFAIRS_AFFAIRID, itemId + "");
				return map;
			}
		};

		System.out.println(stringRequest.getUrl());

		stringRequest.setTag("getAffairsDetailpost");
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	public static interface SuccessCallback {
		void onSuccess(Affairs toDoContent);
	}

	public static interface FailCallback {
		void onFail(int errorCode);
	}
}