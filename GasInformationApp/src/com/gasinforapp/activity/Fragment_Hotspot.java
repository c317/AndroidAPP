package com.gasinforapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.DataAdapter;
import com.gasinforapp.bean.Data;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.net.DataList;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Fragment_Hotspot extends Fragment implements
		OnRefreshListener2<ListView> {

	private PullToRefreshListView lvdata;
	private DataAdapter adapter;
	private List<Data> datalist;
	private int pageNum = 1;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tab_data, container, false);
		init();
		setOnListener();
		return view;
	}

	private void setOnListener() {
		// 点击列表每项跳转事件
		lvdata.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), HotspotContentAty.class);
				Bundle bundle = new Bundle();
				bundle.putString(MyConfig.KEY_DATA_FILENAME, datalist.get(arg2 - 1).getFileName());
				bundle.putString(MyConfig.KEY_DATA_PUBTIME, datalist.get(arg2 - 1).getPubTime());
				bundle.putString(MyConfig.KEY_DATA_SOURCE, datalist.get(arg2 - 1).getSource()); 
				bundle.putString(MyConfig.KEY_DATA_URL, datalist.get(arg2 - 1).getUrl());
				bundle.putString(MyConfig.KEY_DATA_MODULE_ID, MyConfig.MODULEID_HOTSPOT+"");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		// 上拉下拉事件
		lvdata.setOnRefreshListener(this);
	}

	private void init() {
		// 得到控件
		lvdata = (PullToRefreshListView) view.findViewById(R.id.mydata);
		lvdata.setMode(Mode.BOTH);
		datalist = new ArrayList<Data>();
		Data d1 = new Data();
		d1.setTitle("data1");
		Data d2 = new Data();
		d2.setTitle("data2");
		Data d3 = new Data();
		d3.setTitle("data3");
		datalist.add(d1);
		datalist.add(d2);
		datalist.add(d3);
		adapter = new DataAdapter(getActivity(), datalist);
		ListView actualListView = lvdata.getRefreshableView();
		actualListView.setAdapter(adapter);
		getDataList();
	}

	private void getDataList() {
		new DataList(MyConfig.MODULEID_HOTSPOT+"",pageNum, new DataList.SuccessCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<Data> data) {
				new AsyncTask<List<Data>, Void, List<Data>>() {

					@Override
					protected List<Data> doInBackground(List<Data>... dts) {
						return dts[0];
					}

					@Override
					protected void onPostExecute(List<Data> result) {
						super.onPostExecute(result);
						datalist = result;
						adapter.clear();
						adapter.addAll(result);
						lvdata.onRefreshComplete();
					}

				}.execute(data);
			}
		}, new DataList.FailCallback() {
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
						Log.e("DATA", "error");
						switch (result) {
						case MyConfig.RESULT_STATUS_FAIL:
							Toast.makeText(getActivity(),
									R.string.generic_error, Toast.LENGTH_LONG)
									.show();
							break;
						default:
							Toast.makeText(getActivity(),
									R.string.generic_error, Toast.LENGTH_LONG)
									.show();
							break;
						}
					}

				}.execute(errorCode);
			}

		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum = 1;
		getDataList();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum++;
		getDataList();
	}
}