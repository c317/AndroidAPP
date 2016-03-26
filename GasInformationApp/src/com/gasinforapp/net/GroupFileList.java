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
import com.gasinforapp.bean.Data;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

public class GroupFileList {

	public GroupFileList(final String groupid,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL_GROUP + MyConfig.ACTION_GETDATALIST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						System.out.println(response);
						try {
							JSONObject obj = new JSONObject(response);

							switch (obj.optInt(MyConfig.KEY_STATUS)) {
							case MyConfig.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									List<Data> dataList = new ArrayList<Data>();
									JSONArray dataJsonArray = obj
											.getJSONArray(MyConfig.KEY_DATA);
									for (int i = 0; i < dataJsonArray.length(); i++) {
										JSONObject dataObject = dataJsonArray
												.optJSONObject(i);
										String fileName = dataObject
												.optString(MyConfig.KEY_DATA_FILENAME);
										String src = dataObject
												.optString(MyConfig.KEY_DATA_SOURCE);
										String pubTime = dataObject
												.optString(MyConfig.KEY_DATA_PUBTIME);
										String url = dataObject
												.optString(MyConfig.KEY_DATA_URL);
										int id = dataObject
												.optInt(MyConfig.KEY_DATA_ID);

										Data dt = new Data();
										dt.setSource(src);
										dt.setFileName(fileName);
										dt.setPubTime(pubTime);
										dt.setUrl(url);
										dt.setId(id);
										dataList.add(dt);
									}

									successCallback.onSuccess(
											dataList);
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
						Log.e("TAG", error.getMessage(), error);
						if (failCallback != null)
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_GROUPID, groupid);
				return map;
			}

		};
		System.out.println(stringRequest.getUrl());

		// stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1,
		// 1.0f));
		stringRequest.setTag("getGroupFileListpost");
		VolleyUtil.getRequestQueue().add(stringRequest);

	}

	public static interface SuccessCallback {
		void onSuccess(List<Data> data);
	}

	public static interface FailCallback {
		void onFail(int errorCode);
	}

}