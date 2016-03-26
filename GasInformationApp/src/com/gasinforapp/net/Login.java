package com.gasinforapp.net;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;
/**
 * 登录接口，在successCallback的onSuccess方法中处理获取token
 * @author zm
 *
 */
public class Login {
	public Login(final String account, final String password,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				MyConfig.SERVER_URL + MyConfig.ACTION_LOGIN,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						System.out.println(response);
						try {
							JSONObject obj = new JSONObject(response);
							switch (obj.optInt(MyConfig.KEY_STATUS)) {
							case MyConfig.RESULT_STATUS_SUCCESS:
								String token = obj
										.optString(MyConfig.KEY_TOKEN);
								int userId = obj.optInt(MyConfig.KEY_USERID);
								successCallback.onSuccess(token,userId);
								break;
							case MyConfig.RESULT_STATUS_NOTFOUND:
								if(failCallback!=null)
									failCallback.onFail(MyConfig.RESULT_STATUS_NOTFOUND);
								break;
							case MyConfig.RESULT_STATUS_PASSWORD_ERROR:
								if(failCallback!=null)
									failCallback.onFail(MyConfig.RESULT_STATUS_PASSWORD_ERROR);
								break;
							default:
								if(failCallback!=null)
									failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
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
						Log.e("TAG_LOGIN", error.getMessage());
						if (failCallback != null) {
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
						}
					}

				}) {

					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						Map<String, String> map = new HashMap<String, String>();
						map.put(MyConfig.KEY_USER_ACCOUNT, account);
						map.put(MyConfig.KEY_PASSWORD, password);
						return map;
					}
		};
		System.out.println(stringRequest.getUrl());
		stringRequest.setTag("LoginPost");
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	public interface SuccessCallback {
		void onSuccess(String token,int uresId);
	}

	public interface FailCallback {
		void onFail(int ErrorCode);
	}
}
