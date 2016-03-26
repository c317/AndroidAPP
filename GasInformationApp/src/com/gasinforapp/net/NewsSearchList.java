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
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;

public class NewsSearchList {

	/**
	 * ���Ӻ�̨�õ������õ��������б�
	 * 
	 * @param title
	 *            �й�������
	 * @param page
	 *            ��ǰҳ��
	 * @param perpage
	 *            ÿҳ��ʾ�������� ��ʱ���� ��Ϊ0
	 * @param successCallback
	 *            �ɹ��ص��ӿ� ʵ����ʱд��ȡ�����б�ɹ���Ĳ���
	 * @param failCallback
	 *            ʧ�ܻص��ӿ� ʵ����ʱд��ȡ�����б�ʧ�ܺ�Ĳ���
	 */
	public NewsSearchList(final String title, final int page, final int perpage,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL + MyConfig.ACTION_SEARCH_NEWS,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						System.out.println(response);
						try {
							JSONObject obj = new JSONObject(response);

							switch (obj.getInt(MyConfig.KEY_STATUS)) {
							case MyConfig.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									List<HotNewsDTO> newsList = new ArrayList<HotNewsDTO>();
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

										HotNewsDTO ns = new HotNewsDTO();
										ns.setSource(src);
										ns.setTitle(title);
										ns.setPubTime(pubTime);
										ns.setId(newsId);
										//ns.setModule(moduleId);
										newsList.add(ns);
									}

									successCallback.onSuccess(page, perpage,
											newsList);
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
						Log.e("NewsSearchList", error.getMessage(), error);
						if(failCallback!=null)
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// ������������Ҫ�Ĳ���
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_NEWS_TITLE, title + "");
				map.put(MyConfig.KEY_PAGE, page + "");
				map.put(MyConfig.KEY_PERPAGE, perpage + "");
				return map;
			}

		};
		System.out.println(stringRequest.getUrl());

		// stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1,
		// 1.0f));
		stringRequest.setTag("getNewsSearchListpost");
		VolleyUtil.getRequestQueue().add(stringRequest);

	}

	public static interface SuccessCallback {
		void onSuccess(int page, int perpage, List<HotNewsDTO> news);
	}

	public static interface FailCallback {
		void onFail(int errorCode);
	}

}