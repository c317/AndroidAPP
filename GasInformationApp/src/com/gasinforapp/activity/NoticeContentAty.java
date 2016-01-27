package com.gasinforapp.activity;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.NoticeDTO;
import com.gasinforapp.datebase.GasInforDataBaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NoticeContentAty extends Activity{
	
	private TextView tvTitle;
	private TextView tvsrc;
	private TextView tvpubtime;
	private TextView tvcontent;
	private Button back;
	private int noticeId;
	private GasInforDataBaseHelper dataBaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_page);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvsrc = (TextView) findViewById(R.id.tvsrc);
		tvpubtime = (TextView) findViewById(R.id.tvpubtime);
		tvcontent = (TextView) findViewById(R.id.tvcontent);
		back=(Button) findViewById(R.id.back01);
		dataBaseHelper = GasInforDataBaseHelper.getDatebaseHelper(NoticeContentAty.this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		noticeId = bundle.getInt("noticeId");
			
		getNewsContent(noticeId);
		
		back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	private void getNewsContent(int noticeId) {
		NoticeDTO noticeDTO =dataBaseHelper.queryOneNotice(noticeId);
		tvTitle.setText(noticeDTO.getTitle()) ;
		tvcontent.setText(Html.fromHtml(noticeDTO.getContent()));
		tvpubtime.setText(noticeDTO.getTime().split(" ")[0]);
		tvsrc.setText(noticeDTO.getSource());
	}
}
