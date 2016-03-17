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

public class ChangePassword {

	public ChangePassword(final String account,final String token,
			final String oldPassword,final String newPassword,  final SuccessCallback successCallback,
			final FailCallback failCallback) {

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL_PERSONAL + MyConfig.ACTION_PERSONAL_CHAGEPASSWORD,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						System.out.println(response);
						JSONObject obj;
						try {
							obj = new JSONObject(response);
							switch (obj.getInt(MyConfig.KEY_STATUS)) {
							case 1: {
								if(successCallback!=null)
								successCallback.onSuccess();
								break;
							}
							case 2: {
								if(failCallback!=null)
								failCallback.onFail(2);
								break;
							}
							case 3: {
								if(failCallback!=null)
								failCallback.onFail(3);
								break;
							}
							default:
								if(failCallback!=null)
								failCallback.onFail(0);
								return;
							}

						} catch (JSONException e) {
							e.printStackTrace();
							
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("deletegroupTAG", error.getMessage(), error);
						if (failCallback != null) {
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要post的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put(MyConfig.KEY_USER_ACCOUNT, account + "");
				map.put(MyConfig.KEY_TOKEN, token + "");
				map.put(MyConfig.KEY_OLDPASSWORD, oldPassword+"");
				map.put(MyConfig.KEY_NEWPASSWORD, newPassword+"");
				return map;
			}
		};
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	public interface SuccessCallback {
		void onSuccess();
	}

	public interface FailCallback {
		void onFail(int errorCode);
	}

}
