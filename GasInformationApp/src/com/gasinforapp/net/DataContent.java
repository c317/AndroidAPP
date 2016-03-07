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
import com.gasinforapp.bean.Data;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

public class DataContent {

	public DataContent(final int id, final SuccessCallback successCallback,
			final FailCallback failCallback) {
		/**
		 * 创建一个StringRequest进行连接 参数分别 为post, url, Listener：得到服务器返回的字符串后的处理,
		 * ErrorListener:连接服务端失败的处理
		 */
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL + MyConfig.ACTION_GETDATA,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						/**
						 * response 服务端返回的字符串
						 */
						System.out.println(response);
						try {
							JSONObject obj = new JSONObject(response);

							switch (obj.optInt(MyConfig.KEY_STATUS)) {
							/**
							 * 根据 返回的status值进行不同操作
							 */
							case MyConfig.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									JSONObject obj2 = obj.optJSONObject(MyConfig.KEY_DATA);
									String title = obj2
											.optString(MyConfig.KEY_DATA_TITLE);
									String pubTime = obj2
											.optString(MyConfig.KEY_DATA_PUBTIME);
									String source = obj2
											.optString(MyConfig.KEY_DATA_SOURCE);
									String fileName = obj2
											.optString(MyConfig.KEY_DATA_FILENAME);
									String content = obj2
											.optString(MyConfig.KEY_DATA_CONTENT);
									Data dt = new Data();
									dt.setTitle(title);
									dt.setPubTime(pubTime);
									dt.setSource(source);
									dt.setContent(content);
									/**
									 * 将参数加入成功回调接口
									 */
									successCallback.onSuccess(
											dt);
								}
								break;
							// case MyConfig.RESULT_STATUS_INVALID_TOKEN:
							// if (failCallback != null) {
							// failCallback
							// .onFail(MyConfig.RESULT_STATUS_INVALID_TOKEN);
							// }
							// break;
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
						if (failCallback != null) {
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要post的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_DATA_ID, id + "");
				return map;
			}
		};
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	public static interface SuccessCallback {
		void onSuccess(Data data);
	}

	public static interface FailCallback {
		void onFail(int errorCode);
	}
}
