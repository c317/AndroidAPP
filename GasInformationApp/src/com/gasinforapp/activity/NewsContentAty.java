package com.gasinforapp.activity;


import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class NewsContentAty extends Activity {

	private TextView tvTitle;
	private TextView tvsrc;
	private TextView tvpubtime;
	private TextView tvcontent;
	private LinearLayout back;
	private int newsId;
	private LinearLayout collect;
	private LinearLayout share;
	
	private GasInforDataBaseHelper dataBaseHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_page);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvsrc = (TextView) findViewById(R.id.tvsrc);
		tvpubtime = (TextView) findViewById(R.id.tvpubtime);
		tvcontent = (TextView) findViewById(R.id.tvcontent);
		back=(LinearLayout) findViewById(R.id.back00);
		dataBaseHelper = GasInforDataBaseHelper.getDatebaseHelper(NewsContentAty.this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		newsId = bundle.getInt("newsId");
		getNewsContent(newsId);
		

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				/*back.setBackgroundColor(Color.parseColor("#F5F5DC"));*/
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
//				 new AlertDialog.Builder(NewsContentAty.this)  
//	                .setTitle("收藏夹")  
//	                .setItems(R.array.dialog_arrays,  
//	                        new DialogInterface.OnClickListener() {  
//	                            public void onClick(DialogInterface dialog,  
//	                                    int which) {  
//	                                String[] items = getResources()  
//	                                        .getStringArray(  
//	                                                R.array.dialog_arrays);  
//	                                Toast.makeText(  
//	                                		NewsContentAty.this,  
//	                                        "You selected: " + which  
//	                                                + " , " + items[which],  
//	                                        1000).show();  
//	                            }  
//	                        }).create().show();  
				dataBaseHelper.collectHotNews(newsId+"");
				Toast.makeText(NewsContentAty.this, "已收藏", Toast.LENGTH_LONG).show();
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
