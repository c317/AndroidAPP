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
import com.gasinforapp.bean.News;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

public class TTTest {

	/**
	 * 连接后台得到新闻内容
	 * 
	 * @param moduleId
	 *            某块id
	 * @param page
	 *            当前页数
	 * @param perpage
	 *            每页显示新闻条数	暂时不用 设为0
	 * @param successCallback
	 *            成功回调接口 实例化时写获取新闻列表成功后的操作
	 * @param failCallback
	 *            失败回调接口 实例化时写获取新闻列表失败后的操作
	 */
	public TTTest(final int moduleId, final int page, final int perpage,
			final com.gasinforapp.net.NewsList.SuccessCallback successCallback,
			final com.gasinforapp.net.NewsList.FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL+MyConfig.ACTION_GETNEWSLIST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {												
						System.out.println(response);
						try {
						Log.d("response", response);
						JSONObject obj = new JSONObject(response);

						switch (obj.getInt(MyConfig.KEY_STATUS)) {
						case MyConfig.RESULT_STATUS_SUCCESS:
							if (successCallback != null) {
								List<News> newsList = new ArrayList<News>();
								JSONArray newsJsonArray = obj
										.getJSONArray(MyConfig.KEY_NEWS);
								for (int i = 0; i < newsJsonArray.length(); i++) {
									JSONObject newsObject = newsJsonArray
											.getJSONObject(i);
									String title = newsObject
											.getString(MyConfig.KEY_NEWS_TITLE);
									String src = newsObject
											.getString(MyConfig.KEY_NEWS_SOURCE);
									String pubTime = newsObject
											.getString(MyConfig.KEY_NEWS_PUBTIME);
									int newsId = newsObject
											.getInt(MyConfig.KEY_NEWS_ID);
									int moduleId = newsObject
											.getInt(MyConfig.KEY_NEWS_MODULE_ID);

									News ns = new News();
									ns.setOriginSource(src);
									ns.setTitle(title);
									ns.setPubTime(pubTime);
									ns.setId(newsId);
									ns.setModule(moduleId);
									newsList.add(ns);
								}

								successCallback.onSuccess(
										obj.optInt(MyConfig.KEY_PAGE),
										obj.optInt(MyConfig.KEY_PERPAGE),
										newsList);
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
					}
				}) {
		
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_NEWS_MODULE_ID, moduleId + "");
				map.put(MyConfig.KEY_PAGE, page + "");
				map.put(MyConfig.KEY_PERPAGE, perpage + "");
				return map;
			}
//			@Override
//			protected Map<String, String> getParams() {
//				// 在这里设置需要的参数
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("action", "GetNewsListAction");
//				map.put("page", 1+ "");
//				map.put("moduleId",10+ "");
//				return map;
//			}
			
		};
		System.out.println(stringRequest.getUrl());
		
//		stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
		stringRequest.setTag("volleypost");
		VolleyUtil.getRequestQueue().add(stringRequest);
		

	}

	public static interface SuccessCallback {
		void onSuccess(int page, int perpage, List<News> news);
	}

	public static interface FailCallback {
		void onFail(int errorCode);
	}

}