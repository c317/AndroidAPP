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
 * push notice in the group 
 * @author zm
 *
 */
public class SendWords {
	public SendWords(final String account, final String token,
			final int groupid,final int kind, final String msg,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST ,
				MyConfig.SERVER_URL_GROUP+MyConfig.ACTION_SENDWORD,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						System.out.println(response);
						try {
							JSONObject obj = new JSONObject(response);
							
							switch (obj.optInt(MyConfig.KEY_STATUS)) {
							case MyConfig.RESULT_STATUS_SUCCESS:
								 successCallback.onSuccess();
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
							Log.e("Jsonerror", "PushNotice");
							e.printStackTrace();
							if (failCallback!=null) {
								failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
							}
						}
						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("pushError", error.getMessage(),error);
						if (failCallback!=null) {
							failCallback.onFail(MyConfig.RESULT_STATUS_FAIL);
						}
					}
				}){

					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						Map< String, String> map = new HashMap<String, String>();
						map.put(MyConfig.KEY_USER_ACCOUNT, account);
						map.put(MyConfig.KEY_TOKEN, token);
						map.put(MyConfig.KEY_GROUPID, groupid+"");
						map.put(MyConfig.KEY_MSG_KIND, kind+"");
						map.put(MyConfig.KEY_MESSAGE,msg);
						return map;
					}
			
		}; 
		System.out.println(stringRequest.getUrl());
		stringRequest.setTag("sendWordsPost");
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	public interface SuccessCallback {
		void onSuccess();
	}

	public interface FailCallback {
		void onFail(int errorCode);
	}
}
