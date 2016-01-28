package com.gasinforapp.activity;

import com.gasinforapp.service.MessageService;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class StartPolling extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent messageIntent = new Intent(StartPolling.this,MessageService.class);
		startService(messageIntent);					
		Intent intent = new Intent(StartPolling.this,HomeActivity.class);
		startActivity(intent);
		finish();
		
	}

}
