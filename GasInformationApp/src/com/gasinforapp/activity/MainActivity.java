package com.gasinforapp.activity;

import com.gasinforapp.config.MyConfig;
import com.gasinforapp.service.MessageService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String token = MyConfig.getCachedToken(this);
		String account = MyConfig.getCachedAccount(this);
		
		if (token!=null&&account!=null) {
			Intent i =new Intent(this, HomeActivity.class);
			i.putExtra(MyConfig.KEY_TOKEN, token);
			i.putExtra(MyConfig.KEY_USER_ACCOUNT, account);
			startActivity(i);
		}else{
			startActivity(new Intent(this, LoginAty.class));
		}
//		Intent messageIntent = new Intent(MainActivity.this,MessageService.class);
//		startService(messageIntent);
//		finish();
	}


}
