package com.gasinforapp.activity;


import com.example.gasinformationapp_101.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SearchAty extends Activity{
	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_search);
		
		Button btn=(Button)findViewById(R.id.search_send);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		back = (Button) findViewById(R.id.back01);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
