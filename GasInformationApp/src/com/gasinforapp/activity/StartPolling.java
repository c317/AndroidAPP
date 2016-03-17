package com.gasinforapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.service.MessageService;

public class StartPolling extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startpage);
		Intent messageIntent = new Intent(StartPolling.this,
				MessageService.class);
		startService(messageIntent);
		// Intent intent = new Intent(StartPolling.this,HomeActivity.class);
		// startActivity(intent);
		// finish();

		Runnable r = new Runnable() {
			@Override
			public void run() {
				startGoPage();
			}
		};

		new Handler().postDelayed(r, 2000);
	}

	private void startGoPage() {
		Intent intent = new Intent(StartPolling.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
}
