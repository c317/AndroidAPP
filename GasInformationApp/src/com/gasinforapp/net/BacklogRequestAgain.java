package com.gasinforapp.net;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

public class BacklogRequestAgain {
	/**
	 * 连接到后台发送办公请求
	 * 
 	 * @param userId
	 *            当前用户职工编号
	 * @param token
	 *            安全标识码
	 * @param itemId
	 * 			    事项id
	 * @param approverId
	 * 			    审批者Id
	 * @param title
	 * 			    请求标题
	 * @param requestTime
	 * 			   请求时间
	 * @param textContent
	 * 			   请求文本
	 * @param pictures
	 * 			   请求图片
	 * @param successCallback
	 *           成功回调接口
	 * @param failCallback
	 *           失败回调接口
	 */
	
	public BacklogRequestAgain (final String userId, final String token, final String itemId,final String approverId,final String title,
			final String requestTime,final String textContent,final String pictures,
			final SuccessCallback successCallback,final FailCallback failCallback){
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL_OA + MyConfig.ACTION_RESENDAFFAIRSREQUEST,
				new Response.Listener<String>(){

					@Override
					public void onResponse(String response) {
						
						System.out.println(response);
						
						try {
							JSONObject obj = new JSONObject(response);

							switch (obj.getInt(MyConfig.KEY_STATUS)) {
							//连接成功
							case MyConfig.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									successCallback.onSuccess();
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
				map.put(MyConfig.KEY_AFFAIRS_ITEMID, itemId + "");
				map.put(MyConfig.KEY_AFFAIRS_APPROVERID, approverId + "");
				map.put(MyConfig.KEY_AFFAIRS_TITLE, title + "");
				map.put(MyConfig.KEY_AFFAIRS_REQUESTTIME, requestTime + "");
				map.put(MyConfig.KEY_AFFAIRS_TEXTCONTENT, textContent + "");
				map.put(MyConfig.KEY_AFFAIRS_PICTURES, pictures + "");
				return map;
			}
		};
		
		System.out.println(stringRequest.getUrl());

		stringRequest.setTag("getSendffairsRequestApost");
		VolleyUtil.getRequestQueue().add(stringRequest);
	}
	
	public static interface SuccessCallback {
		void onSuccess();
	}

	public static interface FailCallback {
		void onFail(int errorCode);
	}

}

