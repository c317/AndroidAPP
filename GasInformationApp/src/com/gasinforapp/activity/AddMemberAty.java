package com.gasinforapp.activity;

/**添加群成员页面*/
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.gasinforapp.net.ManageMembers.FailCallback;
import com.gasinforapp.net.ManageMembers.SuccessCallback;

public class AddMemberAty extends Activity {
	/** the button return to the previous view */
	private LinearLayout back;
	/** group id */
	private int groupid;
	/** edittext written account */
	private EditText eaccount;
	/** button to commit add action */
	private Button addbtn;
	/** listview of all staff*/
	private ListView lvcheck; 
	/** adapter of listview*/
	private UserAdapter adapter;
	/** list of user*/
	private List<User> userList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_member);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		groupid = bundle.getInt(MyConfig.KEY_GROUPID);

		eaccount = (EditText) findViewById(R.id.account);
		addbtn = (Button) findViewById(R.id.btn_add);
		back = (LinearLayout) findViewById(R.id.back00);
		setOnListener();
		
		lvcheck = (ListView) findViewById(R.id.listview);
		userList = new ArrayList<User>();
		adapter = new UserAdapter(AddMemberAty.this, userList,true);
		lvcheck.setAdapter(adapter);
		loadMemberList();
	}
/**
 * 设置添加按钮监听事件
 */
	private void setOnListener() {
		/**
		 * 点击添加按钮
		 */
		addbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				SparseBooleanArray checkedArray = lvcheck.getCheckedItemPositions(); 
				StringBuffer memberids = new StringBuffer();
				for (int i = 0; i < checkedArray.size(); i++) {  
				    if (checkedArray.valueAt(i)){  //i就是选中的行号  
				    	User us = userList.get(checkedArray.keyAt(i));
				        System.out.println("you have select "+us.getUserName());
				        memberids.append(us.getUserID()+",");
				        
				    }  
				}
				commitAdd(memberids.toString());
			}
		});
		/**
		 * 点击顶部返回按钮
		 */
				back.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						finish();
					}
				});
	}
/**
 * 发送添加请求并相应回复
 * @param memberids
 */
	private void commitAdd(String memberids) {
		if(memberids != null){
		new ManageMembers(MyConfig.getCachedAccount(AddMemberAty.this), "abcd",
				groupid, memberids,
				new SuccessCallback() {

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
								Toast.makeText(AddMemberAty.this,
										R.string.suc_to_add, Toast.LENGTH_LONG)
										.show();
								AddMemberAty.this.finish();
							}
						}.execute();
					}

				}, new FailCallback() {
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
								switch (result.intValue()) {
								case MyConfig.RESULT_STATUS_INVALID_TOKEN:
									Toast.makeText(AddMemberAty.this,
											R.string.invalid_token,
											Toast.LENGTH_LONG).show();
									Intent intent = new Intent(
											AddMemberAty.this, LoginAty.class);
									startActivity(intent);
									break;
								case MyConfig.RESULT_STATUS_NOTFOUND:
									Toast.makeText(AddMemberAty.this,
											R.string.fail_to_find,
											Toast.LENGTH_LONG).show();
									break;
								case MyConfig.RESULT_STATUS_REPEATED:
									Toast.makeText(AddMemberAty.this,
											R.string.re_jion, Toast.LENGTH_LONG)
											.show();
									break;
								default:
									Log.e("tag", VolleyErrorHelper.getMessage(
											result, AddMemberAty.this));
									Toast.makeText(AddMemberAty.this,
											R.string.generic_error,
											Toast.LENGTH_LONG).show();
									break;
								}
							}

						}.execute(errorCode);
					}

				});
		}else{
			
		}
	}

	
	/** 加载未进群成员列表 */
	private void loadMemberList() {
		new MembersList( groupid,MApplication.getAppUserName(), "abcd",
				new MembersList.SuccessCallback() {

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
								
							}

						}.execute(members);
					}
				}, new MembersList.FailCallback() {

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
										result, AddMemberAty.this));
								Toast.makeText(AddMemberAty.this,
										R.string.fail_to_load_members_data,
										Toast.LENGTH_LONG).show();
								super.onPostExecute(result);
							}

						}.execute(ErrorCode);
					}
				});
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll("ManageMembers");
		VolleyUtil.getRequestQueue().cancelAll("getMemberListPost");
	}
}
