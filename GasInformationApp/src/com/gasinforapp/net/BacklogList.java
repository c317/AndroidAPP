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

public class BacklogList {

	/**
	 * 连接后台得到待办事项列表
	 * 以当前用户职工编号在数据库中查询
	 * @param userId
	 *            当前用户职工编号
	 * @param token
	 *            安全标识码
	 * @param page
	 *            当前页数
	 * @param perpage
	 *            每页显示新闻条数 暂时不用 设为0
	 * @param successCallback
	 *            成功回调接口 实例化时写获取办公事项列表成功后的操作
	 * @param failCallback
	 *            失败回调接口 实例化时写获取办公事项列表失败后的操作
	 */
	public BacklogList(final String userId, final String token,final int page,final int perpage,final int isRead,
			final SuccessCallback successCallback,
			final FailCallback failCallback){
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL_OA + MyConfig.ACTION_CHECKTODOLIST,
				new Response.Listener<String>(){

					@Override
					public void onResponse(String response) {
						
						System.out.println(response);
						
						try {
							JSONObject obj = new JSONObject(response);

							switch (obj.optInt(MyConfig.KEY_STATUS)) {
							//连接成功
							case MyConfig.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									//事项的编号、请求者id、审批者id、标题、请求时间、部门是以JSON数组的格式
									List<Affairs> toDoItems = new ArrayList<Affairs>();
									JSONArray affairsJsonArray = obj
											.optJSONArray(MyConfig.KEY_TODOITEMS);
									for (int i = 0; i < affairsJsonArray.length(); i++) {
										JSONObject newsObject = affairsJsonArray
												.optJSONObject(i);
										String itemId = newsObject
												.optString(MyConfig.KEY_AFFAIRS_ITEMID);
										String requesterId = newsObject
												.optString(MyConfig.KEY_AFFAIRS_REQUESTERID);
										String approverId = newsObject
												.optString(MyConfig.KEY_AFFAIRS_APPROVERID);
										String title = newsObject
												.optString(MyConfig.KEY_AFFAIRS_TITLE);
										String department = newsObject
												.optString(MyConfig.KEY_AFFAIRS_DEPARTMENT);
										String requestTime = newsObject
												.optString(MyConfig.KEY_AFFAIRS_REQUESTTIME);				

										Affairs as = new Affairs();
										as.setItemId(itemId);
										as.setRequesterId(requesterId);
										as.setApproverId(approverId);
										as.setRequestTitle(title);
										as.setDepartment(department);
										as.setRequestTime(requestTime);
										toDoItems.add(as);
									}

									successCallback.onSuccess(page, perpage,isRead,toDoItems);
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
		},new Response.ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				if(failCallback!=null)
					failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
			}
		}){
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_USERID, userId + "");
				map.put(MyConfig.KEY_TOKEN, token + "");
				map.put(MyConfig.KEY_PAGE, page + "");
				map.put(MyConfig.KEY_PERPAGE, perpage + "");
				return map;
			}
		};
		
		System.out.println(stringRequest.getUrl());

		stringRequest.setTag("optAffairsListpost");
		VolleyUtil.getRequestQueue().add(stringRequest);
	}
	
	public static interface SuccessCallback {
		void onSuccess(int page, int perpage, int isRead,List<Affairs> toDoItems);
	}

	public static interface FailCallback {
		void onFail(int errorCode);
	}
}
