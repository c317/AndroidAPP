package com.gasinforapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.Affairs;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.net.BacklogAlreadyDetail;
import com.gasinforapp.net.BacklogDetail;

public class AffairsHaveDoneDetailAty extends Activity {

	private TextView tvTitle;
	private TextView tvDepartment;
	private TextView tvApplicant;
	private TextView tvpubtime;
	private TextView tvcontent;
	private Button back;
	private String itemId;
	private String aftitle;
	private String afpubtime;
	private String afrequester;
	private String afdepartment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affair_havadone);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvApplicant = (TextView) findViewById(R.id.tv_applicant);
		tvDepartment = (TextView) findViewById(R.id.tv_department);
		tvpubtime = (TextView) findViewById(R.id.tv_pubtime);
		tvcontent = (TextView) findViewById(R.id.tv_content);
		back = (Button) findViewById(R.id.back01);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		itemId = bundle.getString(MyConfig.KEY_AFFAIRS_ITEMID);
		aftitle = bundle.getString(MyConfig.KEY_AFFAIRS_TITLE);
		afpubtime = bundle.getString(MyConfig.KEY_AFFAIRS_REQUESTTIME);
		afrequester = bundle.getString(MyConfig.KEY_AFFAIRS_REQUESTER);
		afdepartment = bundle.getString(MyConfig.KEY_AFFAIRS_DEPARTMENT);
		tvTitle.setText(aftitle);
		tvApplicant.setText(afrequester);
		tvDepartment.setText(afdepartment);
		tvpubtime.setText(afpubtime);
		getNewsContent();
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void getNewsContent() {
		new BacklogAlreadyDetail(MyConfig.getCachedUserid(this)+"", MyConfig.getCachedToken(this), "", itemId, new BacklogAlreadyDetail.SuccessCallback() {
			
			@Override
			public void onSuccess(Affairs Content) {
				// TODO Auto-generated method stub
				tvcontent.setText(Content.getTextContent());
			}
		}, new BacklogAlreadyDetail.FailCallback() {
			
			@Override
			public void onFail(int errorCode) {
				// TODO Auto-generated method stub
				Log.e("tag", VolleyErrorHelper.getMessage(errorCode,
						AffairsHaveDoneDetailAty.this));
				Toast.makeText(AffairsHaveDoneDetailAty.this,
						R.string.fail_to_load_news_data,
						Toast.LENGTH_LONG).show();
			}
		
		});
	}
}
