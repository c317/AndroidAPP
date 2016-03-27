package com.gasinforapp.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.NewsAdapter;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.gasinforapp.net.NewsSearchList;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchAty extends Activity implements
OnRefreshListener2<ListView> {
	private static String TAG = "SearchAty";
	
	private LinearLayout back;

	private EditText searchKey;
	private PullToRefreshListView lvNews;
	private NewsAdapter adapter;
	private List<HotNewsDTO> newsList;
	private int pageNum = 1;
	private int numsPerPage = 10;
	private GasInforDataBaseHelper dataBaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_search);
		// 得到控件
		lvNews = (PullToRefreshListView) findViewById(R.id.mylv);
		lvNews.setMode(Mode.BOTH);
		searchKey = (EditText) findViewById(R.id.ed_search);
		dataBaseHelper = GasInforDataBaseHelper
				.getDatebaseHelper(this);
		newsList = new ArrayList<HotNewsDTO>();
		adapter = new NewsAdapter(SearchAty.this, newsList);
		ListView actualListView = lvNews.getRefreshableView();
		actualListView.setAdapter(adapter);

		Button btn = (Button) findViewById(R.id.search_send);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMessage();
			}
		});
		back = (LinearLayout) findViewById(R.id.back00);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		setOnListener();
	}

	/**
	 * 加载新闻列表
	 */
	private void loadMessage() {
		new NewsSearchList(searchKey.getText().toString(),pageNum, numsPerPage,
				new NewsSearchList.SuccessCallback(){

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(int page, int perpage, List<HotNewsDTO> news) {
						new AsyncTask<List<HotNewsDTO>, Void, List<HotNewsDTO>>(){

							@Override
							protected List<HotNewsDTO> doInBackground(
									List<HotNewsDTO>... arg0) {
								return arg0[0];
							}

							@Override
							protected void onPostExecute(List<HotNewsDTO> news) {
								super.onPostExecute(news);
								newsList = news;
								adapter.clear();
								adapter.addAll(news);
								lvNews.onRefreshComplete();
							}
							
						}.execute(news);
					}
			
		},new NewsSearchList.FailCallback(){

			@Override
			public void onFail(int errorCode) {
				new AsyncTask<Integer, Void, Integer>() {

					@Override
					protected Integer doInBackground(Integer... error) {
						return error[0];
					}

					@Override
					protected void onPostExecute(Integer result) {
						super.onPostExecute(result);
						Log.e(TAG, "error");
						switch (result) {
						case MyConfig.RESULT_STATUS_FAIL:
							Toast.makeText(SearchAty.this,
									R.string.generic_error, Toast.LENGTH_LONG)
									.show();
							break;
						default:
							Toast.makeText(SearchAty.this,
									R.string.generic_error, Toast.LENGTH_LONG)
									.show();
							break;
						}
					}

				}.execute(errorCode);
			}
			
		});
	}

	private void setOnListener() {
		/**
		 * 点击每项新闻事件
		 */
		lvNews.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(SearchAty.this, NewsBySearchedAty.class);
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
	@Override
	/**
	 * 下拉刷新
	 */
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum = 1;
		loadMessage();
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum++;
		loadMessage();
	}
}
