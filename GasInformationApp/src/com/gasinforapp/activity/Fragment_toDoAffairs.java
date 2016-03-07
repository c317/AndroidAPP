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
import com.gasinforapp.adapter.ToDoAffairsAdapter;
import com.gasinforapp.bean.Affairs;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.net.BacklogList;
import com.gasinforapp.net.BacklogList.FailCallback;
import com.gasinforapp.net.BacklogList.SuccessCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Fragment_toDoAffairs extends Fragment implements
		OnRefreshListener2<ListView> {
	private PullToRefreshListView lvNews;
	private ToDoAffairsAdapter adapter;
	private List<Affairs> affairsList;
	private int pageNum = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_news, container, false);
		// 得到控件
		lvNews = (PullToRefreshListView) view.findViewById(R.id.mylv);
		lvNews.setMode(Mode.BOTH);
		affairsList = new ArrayList<Affairs>();
		adapter = new ToDoAffairsAdapter(getActivity(), affairsList);
		ListView actualListView = lvNews.getRefreshableView();
		actualListView.setAdapter(adapter);
		// 加载数据
		loadMessage();
		setOnListener();
		return view;
	}

	private void setOnListener() {
		/**
		 * 点击每项待办事项事件
		 */
		lvNews.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), AffairsToDoDetailAty.class);
				Bundle bundle = new Bundle();
				bundle.putString(MyConfig.KEY_AFFAIRS_ITEMID, affairsList.get(arg2 - 1)
						.getItemId());
				bundle.putString(MyConfig.KEY_AFFAIRS_REQUESTERID, affairsList.get(arg2 - 1)
						.getRequesterId());
				bundle.putString(MyConfig.KEY_AFFAIRS_APPROVERID, affairsList.get(arg2 - 1)
						.getApproverId());
				bundle.putString(MyConfig.KEY_AFFAIRS_TITLE, affairsList.get(arg2 - 1)
						.getRequestTitle());
				bundle.putString(MyConfig.KEY_AFFAIRS_REQUESTTIME, affairsList.get(arg2 - 1)
						.getRequestTime());
				bundle.putString(MyConfig.KEY_AFFAIRS_REQUESTER, affairsList.get(arg2 - 1)
						.getRequester());
				bundle.putString(MyConfig.KEY_AFFAIRS_DEPARTMENT, affairsList.get(arg2 - 1)
						.getDepartment());
				intent.putExtras(bundle);
				startActivity(intent);
				// startActivityForResult(intent,1);
			}
		});
		/**
		 * 上拉下拉事件
		 */
		lvNews.setOnRefreshListener(this);
	}

	/**
	 * 加载待办事项列表并更新页面
	 */
	private void loadMessage() {
		// BacklogList(userId,token,page,perpage,success,error)
		new BacklogList(MyConfig.getCachedUserid(getActivity()),MyConfig.getCachedToken(getActivity()), pageNum, 0, new SuccessCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int page, int perpage, List<Affairs> affairs) {

				new AsyncTask<List<Affairs>, Void, List<Affairs>>() {
					/**
					 * 获取数据
					 */
					@Override
					protected List<Affairs> doInBackground(
							List<Affairs>... affairs) {
						return affairs[0];
					}

					/**
					 * 更新ui
					 */
					@Override
					protected void onPostExecute(List<Affairs> affairs) {
						affairsList = affairs;

						adapter.clear();
						adapter.addAll(affairs);
						lvNews.onRefreshComplete();
						// adapter.notifyDataSetChanged();
						super.onPostExecute(affairs);
					}

				}.execute(affairs);

			}
		}, new FailCallback() {

			@Override
			public void onFail(int errorCode) {
				new AsyncTask<Integer, Void, Integer>() {

					@Override
					protected Integer doInBackground(Integer... errorCode) {
						return errorCode[0];
					}

					@Override
					protected void onPostExecute(Integer result) {
						Log.e("tag", VolleyErrorHelper.getMessage(result,
								getActivity()));
						Toast.makeText(getActivity(),
								R.string.fail_to_load_news_data,
								Toast.LENGTH_LONG).show();
						super.onPostExecute(result);
					}

				}.execute(errorCode);

			}
		});
	}

	@Override
	/**
	 * 下拉刷新
	 */
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

		pageNum = 2;
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

	@Override
	public void onStop() {
		super.onStop();

		VolleyUtil.getRequestQueue().cancelAll("optAffairsListpost");
	}

}
