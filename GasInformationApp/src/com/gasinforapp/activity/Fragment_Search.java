package com.gasinforapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/*import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.NewsAdapter;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.datebase.GasInforDataBaseHelper;*/
/*import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;*/
/*
public class Fragment_Search extends Fragment implements
		OnRefreshListener2<ListView> 
{
	private PullToRefreshListView lvNews;
	private NewsAdapter adapter;
	private List<HotNewsDTO> newsList;
	private int pageNum = 1;
	private int numsPerPage =10;
	private GasInforDataBaseHelper dataBaseHelper;

	@Override
	public void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_news, container, false);
		// 得到控件
		lvNews = (PullToRefreshListView) view.findViewById(R.id.mylv);
		lvNews.setMode(Mode.BOTH);
		dataBaseHelper = GasInforDataBaseHelper.getDatebaseHelper(getActivity());
		newsList = new ArrayList<HotNewsDTO>();
		adapter = new NewsAdapter(getActivity(), newsList);
		ListView actualListView = lvNews.getRefreshableView();
		actualListView.setAdapter(adapter);
		// 加载数据
		loadMessage(pageNum*numsPerPage);
		setOnListener();
		return view;
	}
	
	private void setOnListener() {
	*//**
		 * 点击每项新闻事件
		 *//*
		lvNews.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), NewsContentAty.class);
				Bundle bundle = new Bundle();
				bundle.putInt("newsId", newsList.get(arg2 - 1).getId());
				intent.putExtras(bundle);
				dataBaseHelper.updataHotNews(newsList.get(arg2 - 1).getId()+"");
				newsList.get(arg2 - 1).setRead(1);
				startActivity(intent);
			}
		});
		*//**
		 * 上拉下拉事件
		 *//*
		lvNews.setOnRefreshListener(this);
	}

	*//**
	 * 加载新闻列表并更新页面
	 *//*
	private void loadMessage(int nums) {
		newsList = dataBaseHelper.queryMultiHotNews(nums);
		adapter.clear();
		adapter.addAll(newsList);
		lvNews.onRefreshComplete();
	}

	@Override
	*//**
	 * 下拉刷新
	 *//*
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum = 1;
		loadMessage(pageNum*numsPerPage);
	}

	*//**
	 * 上拉加载
	 *//*
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum++;
		loadMessage(pageNum*numsPerPage);
	}
	
}

*/