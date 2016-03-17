package com.gasinforapp.activity;

import java.util.ArrayList;
import java.util.List;




import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.GroupAdapter;
import com.gasinforapp.bean.Group;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.net.DeleteGroup;
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
	private int pageNum =9;
	private Button button;
	private Group temp;
	private LinearLayout turn_contacts;
	View view;
	
	private ListView actualListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.group_fragment, container, false);
		// get view from layout
		lvGroup = (PullToRefreshListView) view.findViewById(R.id.mylv);
        button = (Button) view.findViewById(R.id.btn_more);
        button.setText("新建");
		groupList = new ArrayList<Group>();
		adapter = new GroupAdapter(getActivity(), groupList);

		actualListView = lvGroup.getRefreshableView();
		actualListView.setAdapter(adapter);
		
		turn_contacts = (LinearLayout) view.findViewById(R.id.turn_contacts);
		turn_contacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),Contacts_MainActivity.class);
				startActivity(intent);	
				
			}
		});
		
		
		

		
		loadGroup();
		setOnListener();
		turn_mymenu();
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
		
		button.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
						 
			    if (v == button) {  
					            Intent intent = new Intent(getActivity(), CreateGroupAty.class);  
					            
					             startActivity(intent);  
					   
					          }  
				}
			
		});
	
		actualListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0,
	
					View arg1, int arg2, long arg3) {
	
				temp = groupList.get(arg2 - 1);

				lvGroup.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
	
					public void onCreateContextMenu(ContextMenu menu,
						
							View arg1, ContextMenuInfo arg2) {

						menu.setHeaderTitle(temp.getGroupName());

						menu.add(0, 0, 0, "删除该群组");

						menu.add(0, 1, 0, "查看资料");

					}


				});

				return false;

			}


		
		

		});
}



	public boolean onContextItemSelected(MenuItem item) {

			switch (item.getItemId()) {

					case 0:deleteGroup();break;	
					case 1:	break;

			}

			return super.onContextItemSelected(item);

   }





	private void deleteGroup() {

			new DeleteGroup(MyConfig.getCachedAccount(getActivity()),MyConfig.getCachedToken(getActivity()),

					temp.getGroupID(),new DeleteGroup.SuccessCallback() {				
		@Override
			public void onSuccess() {

				new AsyncTask<Void, Void, Void>() {

					@Override

					protected Void doInBackground(Void... arg0) {

						return null;
					}

					@Override

					protected void onPostExecute(Void result) {

						super.onPostExecute(result);

                         Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_LONG).show();

					}

				}.execute();
			}

		}, new DeleteGroup.FailCallback() {

			@Override

			public void onFail(int errorCode) {

				new AsyncTask<Integer, Integer, Integer>() {

					@Override

					protected Integer doInBackground(Integer... error) {

						return error[0];

					}


					@Override

					protected void onPostExecute(Integer result) {

						super.onPostExecute(result);

						switch (result) {

						    case MyConfig.RESULT_STATUS_INVALID_TOKEN:

							   Toast.makeText(getActivity(),

									  R.string.invalid_token,

									     Toast.LENGTH_LONG).show();

							                break;

						default: Log.e("tag","deleteError"+ VolleyErrorHelper.getMessage(result,getActivity()));								

							Toast.makeText(getActivity(),R.string.generic_error,	Toast.LENGTH_LONG).show();

							break;

						}

					}

				}.execute(errorCode);

			}

		});

//
		

	
	}


	
	protected EditText findViewById(Object groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum = 9;
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

	private Button mymenu;
	private void turn_mymenu(){
		mymenu = (Button) view.findViewById(R.id.id_menu);
		mymenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),MyMenu.class);
				startActivity(intent);			
				
			}
		});
	}
}
