package com.gasinforapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.config.MApplication;
import com.gasinforapp.config.MD5Tool;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.net.Login;
import com.gasinforapp.net.Login.FailCallback;
import com.gasinforapp.net.Login.SuccessCallback;

public class LoginAty extends Activity {
	private EditText edAccount;
	private EditText edPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_aty);
		edAccount = (EditText) findViewById(R.id.account);
		edPassword = (EditText) findViewById(R.id.password);

		findViewById(R.id.btnLogin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(edAccount.getText())) {
					Toast.makeText(LoginAty.this,
							R.string.account_can_not_be_empty,
							Toast.LENGTH_LONG).show();
					return;
				}

				if (TextUtils.isEmpty(edPassword.getText())) {
					Toast.makeText(LoginAty.this,
							R.string.password_can_not_be_empty,
							Toast.LENGTH_LONG).show();
					return;
				}
				final ProgressDialog pd = ProgressDialog
						.show(LoginAty.this,
								getResources().getString(R.string.connecting),
								getResources().getString(
										R.string.connecting_to_server));
//				new Login(edAccount.getText().toString(), MD5Tool
//						.md5(edPassword.getText().toString()),
//						new SuccessCallback() {
//
//							@Override
//							public void onSuccess(String token) {
//								pd.dismiss();
//								
//								MyConfig.cacheToken(LoginAty.this, token);
//								MyConfig.cacheAccount(LoginAty.this, edAccount.getText().toString());
//								MyConfig.cachePassword(LoginAty.this, edPassword.getText().toString());
//								
//								Intent intent = new Intent(LoginAty.this,HomeActivity.class);
//								startActivity(intent);
//								finish();
//							}
//						}, new FailCallback() {
//
//							@Override
//							public void onFail(int ErrorCode) {
//								pd.dismiss();
//
//								Toast.makeText(LoginAty.this,
//										R.string.fail_to_login,
//										Toast.LENGTH_LONG).show();
//							}
//						});
	/** 测试用，token给为定值，不经过后台判断直接跳转至主页面*/			
				MyConfig.cacheToken(LoginAty.this, "abcd");
				MyConfig.cacheAccount(LoginAty.this, edAccount.getText().toString());
				MyConfig.cachePassword(LoginAty.this, edPassword.getText().toString());
				
				Intent intent = new Intent(LoginAty.this,HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll("LoginPost");
	}

	
	
}
