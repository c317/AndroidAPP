package com.gasinforapp.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.bean.News;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.gasinforapp.net.NewsContent;


public class NewsBySearchedAty extends Activity {

	private TextView tvTitle;
	private TextView tvsrc;
	private TextView tvpubtime;
	private TextView tvcontent;
	private Button back;
	private int newsId;
	private int moduleId;
	private LinearLayout collect;
	private LinearLayout share;
	private News hNews;  
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
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		newsId = bundle.getInt("newsId");
		moduleId = MyConfig.MODULEID_SEARCH;
		
		dataBaseHelper = GasInforDataBaseHelper.getDatebaseHelper(NewsBySearchedAty.this);
		getNewsContent();
		
		back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				finish();
			}
		});
		
		share=(LinearLayout) findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(Intent.ACTION_SEND);  
                intent.setType("text/tvcontent");  
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");  
                intent.putExtra(Intent.EXTRA_TITLE,tvTitle.getText().toString());  
                intent.putExtra(Intent.EXTRA_TEXT, tvcontent.getText().toString());  
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
                startActivity(Intent.createChooser(intent, getTitle()));  
				
			}
		});
		
		collect=(LinearLayout) findViewById(R.id.collect);
		collect.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {

				dataBaseHelper.collectHotNews(newsId+"");
				Toast.makeText(NewsBySearchedAty.this, "已收藏", Toast.LENGTH_LONG).show();
			}
		});
		
	}
	
	
	private void getNewsContent() {
		final ProgressDialog pd = ProgressDialog.show(this, getResources()
				.getString(R.string.connecting),
				getResources().getString(R.string.connecting_to_server));

			new NewsContent( newsId, moduleId, new NewsContent.SuccessCallback() {

				@Override
				public void onSuccess(int newId, int moduleId, News news) {
					new AsyncTask<News, Void, News>(){

						@Override
						protected News doInBackground(News... arg0) {
							NewsBySearchedAty.this.hNews = arg0[0];
							List<HotNewsDTO> newsDTOs = new ArrayList<HotNewsDTO>();
							HotNewsDTO dnews = new HotNewsDTO(hNews);
							newsDTOs.add(dnews);
							dataBaseHelper.batchInsertHotNews(newsDTOs);
							return arg0[0];
						}

						@Override
						protected void onPostExecute(News news) {
							super.onPostExecute(news);
							
							pd.dismiss();
							tvTitle.setText(news.getTitle()) ;
							tvcontent.setText(Html.fromHtml(news.getContent()));
							tvpubtime.setText(news.getPubTime());
							tvsrc.setText(news.getOriginSource());
						}
						
					}.execute(news);
				}
	
			}, new NewsContent.FailCallback() {
	
				@Override
				public void onFail(int errorCode) {
					pd.dismiss();

						Toast.makeText(NewsBySearchedAty.this,
								R.string.fail_to_load_news_data,
								Toast.LENGTH_LONG).show();
					}
			});
	}
}
