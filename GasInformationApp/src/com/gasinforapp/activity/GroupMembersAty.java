package com.gasinforapp.activity;

/** 群成员列表页面*/
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.UserAdapter;
import com.gasinforapp.bean.User;
import com.gasinforapp.config.MApplication;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.net.ManageMembers;
import com.gasinforapp.net.MembersList;
import com.gasinforapp.net.MembersList.FailCallback;
import com.gasinforapp.net.MembersList.SuccessCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class GroupMembersAty extends Activity {
	private PullToRefreshListView lvUser;
	private UserAdapter adapter;
	private List<User> userList;
	/** the button return to the previous view */
	private Button back;
	/**
	 * param about the groupid numbers in request ,it get data from GroupChatAty
	 */
	private int groupid;
	/** the name of topbar */
	private TextView topname;
	/** the button turn to AddMemberAty */
	private Button addbtn;
	/** the user you click in the listview */
	private User temp;
	/** actrual listview in the pulltorefresh view */
	private ListView actruListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.members_activity);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		groupid = bundle.getInt(MyConfig.KEY_GROUPID);

		setTopbar();

		lvUser = (PullToRefreshListView) findViewById(R.id.mylv);
		userList = new ArrayList<User>();
		adapter = new UserAdapter(GroupMembersAty.this, userList,false);
		actruListView = lvUser.getRefreshableView();
		actruListView.setAdapter(adapter);

		loadMemberList();
		setOnListener();

	}

	/** 配置topbar */
	private void setTopbar() {
		// 改topbar的名字
		topname = (TextView) findViewById(R.id.topname);
		topname.setText("群成员");

		// 返回按钮
		back = (Button) findViewById(R.id.back01);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		// add按钮
		addbtn = (Button) findViewById(R.id.btn_more);
		addbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent m_intent = new Intent(GroupMembersAty.this,
						AddMemberAty.class);
				Bundle bundle = new Bundle();
				bundle.putInt(MyConfig.KEY_GROUPID, groupid);
				m_intent.putExtras(bundle);
				startActivity(m_intent);
			}
		});
	}

	/** 加载群成员列表 */
	private void loadMemberList() {
		new MembersList(MApplication.getAppUserName(), "abcd", groupid,
				new SuccessCallback() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(List<User> members) {
						new AsyncTask<List<User>, Void, List<User>>() {

							@Override
							protected List<User> doInBackground(
									List<User>... users) {
								return users[0];
							}

							@Override
							protected void onPostExecute(List<User> result) {
								super.onPostExecute(result);
								userList = result;
								adapter.clear();
								adapter.addAll(result);
								lvUser.onRefreshComplete();
							}

						}.execute(members);
					}
				}, new FailCallback() {

					@Override
					public void onFail(int ErrorCode) {
						new AsyncTask<Integer, Void, Integer>() {

							@Override
							protected Integer doInBackground(
									Integer... errorCode) {
								return errorCode[0];
							}

							@Override
							protected void onPostExecute(Integer result) {
								Log.e("tag", VolleyErrorHelper.getMessage(
										result, GroupMembersAty.this));
								Toast.makeText(GroupMembersAty.this,
										R.string.fail_to_load_members_data,
										Toast.LENGTH_LONG).show();
								super.onPostExecute(result);
							}

						}.execute(ErrorCode);
					}
				});
	}

	/** 设置listview监听器 */
	private void setOnListener() {
		// 单击
		lvUser.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/** 添加点击相应事件 */

			}
		});
		// 长按
		actruListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				temp = userList.get(arg2 - 1);
				lvUser.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					public void onCreateContextMenu(ContextMenu menu,
							View arg1, ContextMenuInfo arg2) {
						menu.setHeaderTitle(temp.getUserName());
						menu.add(0, 0, 0, "删除此人");
						menu.add(0, 1, 0, "查看资料");
					}
				});
				return false;
			}
		});
		// 下拉刷新
		lvUser.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadMemberList();
			}
		});
	}

	/** 长按后选择菜单项操作事件 */
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			// 删除
			System.out
					.println("delete" + temp.getUserName() + temp.getUserID());
			deleteMember();
			break;
		case 1:
			// 查看资料
			System.out.println("look info of" + temp.getUserName()
					+ temp.getUserID());
			break;

		}
		return super.onContextItemSelected(item);
	}

	/** delete the chosen member from the group */
	private void deleteMember() {
		new ManageMembers(MyConfig.getCachedAccount(GroupMembersAty.this),
				"abcd", groupid, temp.getUserID(), temp.getAccount(), 0,
				new ManageMembers.SuccessCallback() {
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
								Toast.makeText(GroupMembersAty.this,
										R.string.suc_to_delete,
										Toast.LENGTH_LONG).show();
								loadMemberList();
							}
						}.execute();
					}

				}, new ManageMembers.FailCallback() {

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
									Toast.makeText(GroupMembersAty.this,
											R.string.invalid_token,
											Toast.LENGTH_LONG).show();
									Intent intent = new Intent(
											GroupMembersAty.this,
											LoginAty.class);
									startActivity(intent);
									break;
								default:
									Log.e("tag",
											"deleteError"
													+ VolleyErrorHelper
															.getMessage(
																	result,
																	GroupMembersAty.this));
									Toast.makeText(GroupMembersAty.this,
											R.string.generic_error,
											Toast.LENGTH_LONG).show();
									break;
								}
							}

						}.execute(errorCode);
					}
				});
	}

	@Override
	protected void onStop() {
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll("ManageMembers");
		VolleyUtil.getRequestQueue().cancelAll("getMemberListPost");
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadMemberList();
	}
	
}
