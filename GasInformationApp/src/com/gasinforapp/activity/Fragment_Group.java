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
import com.gasinforapp.adapter.GroupAdapter;
import com.gasinforapp.bean.Group;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.net.GroupList;
import com.gasinforapp.net.GroupList.FailCallback;
import com.gasinforapp.net.GroupList.SuccessCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Fragment_Group extends Fragment implements
		OnRefreshListener2<ListView> {
	private static final String TAG = "Fragment_Group";
	private PullToRefreshListView lvGroup;
	private GroupAdapter adapter;
	private List<Group> groupList;
	private int pageNum = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.group_fragment, container, false);
		// get view from layout
		lvGroup = (PullToRefreshListView) view.findViewById(R.id.mylv);

		groupList = new ArrayList<Group>();
		adapter = new GroupAdapter(getActivity(), groupList);

		ListView actualListView = lvGroup.getRefreshableView();
		actualListView.setAdapter(adapter);

		loadGroup();
		setOnListener();
		return view;
	}

	/**
	 * 加载群组列表
	 */
	private void loadGroup() {
		new GroupList(MyConfig.getCachedAccount(getActivity()), MyConfig.getCachedToken(getActivity()), pageNum, 0,
				new SuccessCallback() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(int page, int perpage,
							List<Group> groups) {
						new AsyncTask<List<Group>, Void, List<Group>>() {

							@Override
							protected List<Group> doInBackground(
									List<Group>... groups) {
								return groups[0];
							}

							@Override
							protected void onPostExecute(List<Group> result) {
								groupList = result;
								adapter.clear();
								adapter.addAll(result);
								lvGroup.onRefreshComplete();
								super.onPostExecute(result);
							}

						}.execute(groups);
					}
				}, new FailCallback() {

					@Override
					public void onFail(int errorCode) {
						new AsyncTask<Integer, Void, Integer>() {

							@Override
							protected Integer doInBackground(
									Integer... errorCode) {
								return errorCode[0];
							}

							@Override
							protected void onPostExecute(Integer result) {
								Log.e(TAG, VolleyErrorHelper.getMessage(
										result, getActivity()));
								Toast.makeText(getActivity(),
										R.string.fail_to_load_groups_data,
										Toast.LENGTH_LONG).show();
								super.onPostExecute(result);
							}
						}.execute(errorCode);
					}
				});
	}

	private void setOnListener() {
		/**
		 * 点击每个群组的跳转至通知列表页面事件
		 */
		lvGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/**
				 * intent 跳转
				 */
				System.out.println("you click  groupId:"+groupList.get(arg2-1).getGroupID());
				Intent intent = new Intent(getActivity(),GroupChatAty.class );
				Bundle bundle = new Bundle();
				bundle.putInt(MyConfig.KEY_GROUPID, groupList.get(arg2-1).getGroupID());
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});
		
		/**
		 * 上拉下拉事件
		 */
		lvGroup.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum = 2;
		loadGroup();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum++;
		loadGroup();
	}

	@Override
	public void onStop() {
		super.onStop();

		VolleyUtil.getRequestQueue().cancelAll("getGroupListpost");
	}

}
