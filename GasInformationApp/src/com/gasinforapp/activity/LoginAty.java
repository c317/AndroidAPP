package com.gasinforapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
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
				try {
					new Login(edAccount.getText().toString(), MD5Tool
							.md5(edPassword.getText().toString()),
							new SuccessCallback() {

								@Override
								public void onSuccess(String token ,int userId) {
									pd.dismiss();
									
									MyConfig.cacheToken(LoginAty.this, token);
									MyConfig.cacheAccount(LoginAty.this, edAccount.getText().toString());
									MyConfig.cachePassword(LoginAty.this, edPassword.getText().toString());
									MyConfig.cacheUserid(LoginAty.this, userId);
									
									Intent intent = new Intent(LoginAty.this,StartPolling.class);
									startActivity(intent);
									finish();
								}
							}, new FailCallback() {

								@Override
								public void onFail(int ErrorCode) {
									pd.dismiss();
									Log.e("login", "eee");
									switch (ErrorCode) {
									case MyConfig.RESULT_STATUS_NOTFOUND:
										Toast.makeText(LoginAty.this,
											R.string.fail_to_find_user,
											Toast.LENGTH_LONG).show();
										break;
									case MyConfig.RESULT_STATUS_PASSWORD_ERROR:
										Toast.makeText(LoginAty.this,
											R.string.login_password_error,
											Toast.LENGTH_LONG).show();
									default:
										Toast.makeText(LoginAty.this,
											R.string.generic_error,
											Toast.LENGTH_LONG).show();
										break;
									}
									
								}
							});
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(LoginAty.this,
							R.string.generic_error,
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll("LoginPost");
	}

	
	
}
