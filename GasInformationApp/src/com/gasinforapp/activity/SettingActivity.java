package com.gasinforapp.activity;

import com.example.gasinformationapp_101.R;
import com.example.gasinformationapp_101.R.layout;
import com.example.gasinformationapp_101.R.menu;
import com.gasinforapp.config.MyConfig;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {

    private TextView myTextView;  
    private LinearLayout updateapp;
    private LinearLayout dataclean;
    private LinearLayout night;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		 myTextView = (TextView)findViewById(R.id.topname);        
	     myTextView.setText("设置"); 
	     
	     
	     updateapp = (LinearLayout) findViewById(R.id.updateapp);  
	     updateapp.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Uri uri = Uri.parse("https://www.wandoujia.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
			}
		});	
	     
/*	     night = (LinearLayout) findViewById(R.id.night);  
	     night.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Toast.makeText(SettingActivity.this, "待续", Toast.LENGTH_LONG).show();
			}
		});	 */    
	     
	
	     dataclean = (LinearLayout) findViewById(R.id.dataclean);  
	     dataclean.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				DataCleanManager.cleanInternalCache(SettingActivity.this);
				Toast.makeText(SettingActivity.this, "已清除", Toast.LENGTH_LONG).show();
			}
		});
	
	
	}


}
