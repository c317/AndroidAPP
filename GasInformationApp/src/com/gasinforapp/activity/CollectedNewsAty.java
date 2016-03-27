package com.gasinforapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.NewsAdapter;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CollectedNewsAty extends Activity implements
		OnRefreshListener2<ListView> {
	private PullToRefreshListView lvNews;
	private NewsAdapter adapter;
	private List<HotNewsDTO> newsList;
	private int pageNum = 1;
	private int numsPerPage = 10;
	private GasInforDataBaseHelper dataBaseHelper;
	private TextView myTextView; 
	private LinearLayout back;

	@Override
	public void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
		
		 myTextView = (TextView)findViewById(R.id.topname);        
	     myTextView.setText("我的收藏"); 
	     
	     back = (LinearLayout) findViewById(R.id.back00);  
	     back.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					finish();
				}
			});
		
		// 得到控件
				lvNews = (PullToRefreshListView)findViewById(R.id.mylv);
				lvNews.setMode(Mode.BOTH);
				dataBaseHelper = GasInforDataBaseHelper
						.getDatebaseHelper(this);
				newsList = new ArrayList<HotNewsDTO>();
				adapter = new NewsAdapter(this, newsList);
				ListView actualListView = lvNews.getRefreshableView();
				actualListView.setAdapter(adapter);
				// 加载数据
				loadMessage(pageNum * numsPerPage);
				setOnListener();
	}



	private void setOnListener() {
		/**
		 * 点击每项新闻事件
		 */
		lvNews.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(CollectedNewsAty.this, NewsContentAty.class);
				Bundle bundle = new Bundle();
				bundle.putInt("newsId", newsList.get(arg2 - 1).getId());
				intent.putExtras(bundle);
				dataBaseHelper.updataHotNews(newsList.get(arg2 - 1).getId()
						+ "");
				newsList.get(arg2 - 1).setRead(1);
				startActivity(intent);
			}
		});
		/**
		 * 上拉下拉事件
		 */
		lvNews.setOnRefreshListener(this);
	}

	/**
	 * 加载新闻列表并更新页面
	 */
	private void loadMessage(int nums) {
		newsList = dataBaseHelper.queryMultiCollectedHotNews(nums);
		adapter.clear();
		adapter.addAll(newsList);
		lvNews.postDelayed(new Runnable() {

            @Override
            public void run() {
            	lvNews.onRefreshComplete();
            }
        }, 300);
	}

	@Override
	/**
	 * 下拉刷新
	 */
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum = 1;
		loadMessage(pageNum * numsPerPage);
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum++;
		loadMessage(pageNum * numsPerPage);
	}

}
