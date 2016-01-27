package com.gasinforapp.activity;


import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class NewsContentAty extends Activity {

	private TextView tvTitle;
	private TextView tvsrc;
	private TextView tvpubtime;
	private TextView tvcontent;
	private Button back;
	private int newsId;
	private GasInforDataBaseHelper dataBaseHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_page);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvsrc = (TextView) findViewById(R.id.tvsrc);
		tvpubtime = (TextView) findViewById(R.id.tvpubtime);
		tvcontent = (TextView) findViewById(R.id.tvcontent);
		back=(Button) findViewById(R.id.back01);
		dataBaseHelper = GasInforDataBaseHelper.getDatebaseHelper(NewsContentAty.this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		newsId = bundle.getInt("newsId");
		getNewsContent(newsId);
		back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	private void getNewsContent(int newsId) {
		HotNewsDTO hotNewsDTO =dataBaseHelper.queryOneHotNews(newsId);
		tvTitle.setText(hotNewsDTO.getTitle()) ;
		tvcontent.setText(Html.fromHtml(hotNewsDTO.getContent()));
		tvpubtime.setText(hotNewsDTO.getPubTime());
		tvsrc.setText(hotNewsDTO.getSource());
	}
}
