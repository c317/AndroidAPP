package com.gasinforapp.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.config.MyConfig;

public class MyMenu extends Activity {

	private Button back;
    private TextView myTextView;  
    private LinearLayout ex;
    private LinearLayout account;
    private LinearLayout setting;
    private LinearLayout collect;
    private LinearLayout contactme;
    private TextView tv_userName;
    private TextView tv_job;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_menu);
		 myTextView = (TextView)findViewById(R.id.topname);        
	     myTextView.setText("我的"); 
	     
	     back = (Button) findViewById(R.id.back01);  
	     back.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					finish();
				}
			});
	     
	     ex = (LinearLayout) findViewById(R.id.exit);  
	     ex.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					exit();
				}
			});
	     
	     
	     
	     account = (LinearLayout) findViewById(R.id.account);  
	     account.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Intent intent = new Intent(MyMenu.this,LoginAty.class);
					startActivity(intent);
				}
			});
	     
	     
	     setting = (LinearLayout) findViewById(R.id.setting);  
	     setting.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Intent intent = new Intent(MyMenu.this,SettingActivity.class);
					startActivity(intent);
				}
			});
	     
	     contactme = (LinearLayout) findViewById(R.id.contactme);  
	     contactme.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Uri uri = Uri.parse("mailto:727840548@qq.com");
					Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
					intent.putExtra(Intent.EXTRA_SUBJECT, "APP使用反馈");//
					intent.putExtra(Intent.EXTRA_TEXT, "你好，我在使用APP过程中遇到了以下问题：");//
					startActivity(intent);
				}

			});
	     
	     

	     
	     
	     collect = (LinearLayout) findViewById(R.id.collect);  
	     collect.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Intent intent = new Intent(MyMenu.this,CollectedNewsAty.class);
					startActivity(intent);
				}
			});
	     tv_userName = (TextView) findViewById(R.id.userName);
	     tv_userName.setText(MyConfig.getCachedAccount(MyMenu.this));
	     tv_job = (TextView) findViewById(R.id.user_job);
	     tv_job.setText("");
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void exit(){
		
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
		    Intent startMain = new Intent(Intent.ACTION_MAIN);
		    startMain.addCategory(Intent.CATEGORY_HOME);
		    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    startActivity(startMain);
		    System.exit(0);
		} else {// android2.1
		    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		    am.restartPackage(getPackageName());
		}
		 }
		
	

}
