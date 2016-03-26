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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.NoticeAdapter;
import com.gasinforapp.bean.NoticeDTO;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class Fragment_Notice extends Fragment implements
		OnRefreshListener2<ListView> {

	private PullToRefreshListView lvNotice;
	private NoticeAdapter adapter;
	private List<NoticeDTO> noticeList;
	private int pageNum = 1;
	private int numsPerPage = 10;
	private GasInforDataBaseHelper dataBaseHelper;
	
	@Override
	public void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_notice, container, false);
		// 得到控件
		lvNotice = (PullToRefreshListView) view.findViewById(R.id.mynotice);
		lvNotice.setMode(Mode.BOTH);
		dataBaseHelper = GasInforDataBaseHelper
				.getDatebaseHelper(getActivity());
		noticeList = new ArrayList<NoticeDTO>();
		adapter = new NoticeAdapter(getActivity(), noticeList);
		ListView actualListView = lvNotice.getRefreshableView();
		actualListView.setAdapter(adapter);
		// 加载数据
		loadMessage(pageNum * numsPerPage);
		// lv.setAdapter(adapter);
		setOnListener();

		return view;
	}

	private void setOnListener() {

		lvNotice.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), NoticeContentAty.class);
				Bundle bundle = new Bundle();
				bundle.putInt("noticeId", noticeList.get(arg2 - 1).getId());
				intent.putExtras(bundle);
				dataBaseHelper.updataNotice(noticeList.get(arg2 - 1).getId()+"");
				noticeList.get(arg2 - 1).setRead(1);
				startActivity(intent);
			}
		});
		/**
		 * 上拉下拉事件
		 */
		lvNotice.setOnRefreshListener(this);
	}

	/**
	 * 加载通知列表并更新页面
	 */
	private void loadMessage(int nums) {
		noticeList = dataBaseHelper.queryMultiNotice(nums);
		adapter.clear();
		adapter.addAll(noticeList);
		lvNotice.postDelayed(new Runnable() {

            @Override
            public void run() {
            	lvNotice.onRefreshComplete();
            }
        }, 300);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum = 1;
		loadMessage(pageNum * numsPerPage);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum++;
		loadMessage(pageNum * numsPerPage);

	}

}
