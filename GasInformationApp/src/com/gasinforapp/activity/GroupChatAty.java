package com.gasinforapp.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.ChatItemAdapter;
import com.gasinforapp.bean.GroupNewsDTO;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.gasinforapp.net.SendWords;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class GroupChatAty extends Activity {
	private static final String TAG = "GroupChatAty";
	/** the button trun to members list activity */
	private Button getMembers;
	/** the button return to the previous view */
	private Button back;
	/**
	 * param about the groupid numbers in request ,it get data from
	 * Fragment_group
	 */
	private int groupid;
	/** the name of topbar */
	private TextView topname;
	/** the listview */
	private PullToRefreshListView listView;
	/** the data of chat list */
	private List<GroupNewsDTO> chatList;
	/** adapter to show data on the view */
	private ChatItemAdapter adapter;
	/** the page number of the list */
	private int pageNum = 1;
	/** the numsPerPage in one page */
	private int numsPerPage =10;
	/** the button of send word */
	private Button btn_sendword;
	/** get send word from the edittext */
	private EditText editText;
	private double currentTime = 0, oldTime = 0;
	private GasInforDataBaseHelper dataBaseHelper;
	private Handler myHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//进入聊天页面
		MyConfig.CHATING_FLAG = true;
		dataBaseHelper = GasInforDataBaseHelper.getDatebaseHelper(GroupChatAty.this);
		setContentView(R.layout.group_chat_activity);
		// get group id
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		groupid = bundle.getInt(MyConfig.KEY_GROUPID);
		// 初始化控件
		findAllView();
		chatList = new ArrayList<GroupNewsDTO>();
		adapter = new ChatItemAdapter(this, chatList);
		ListView actuaListView = listView.getRefreshableView();
		actuaListView.setAdapter(adapter);
		setTopbar();		
		setOnListener();
		myHandler = new MyHandler();
		loadChats(pageNum*numsPerPage);
	}

	private void findAllView() {
		listView = (PullToRefreshListView) findViewById(R.id.mylv);
		topname = (TextView) findViewById(R.id.topname);
		back = (Button) findViewById(R.id.back01);
		getMembers = (Button) findViewById(R.id.getmembers);
		editText = (EditText) findViewById(R.id.et_sendwords);
		btn_sendword = (Button) findViewById(R.id.button_sendchat);
	}
	

	/** 配置topbar */
	private void setTopbar() {
		// 改topbar的名字
		topname.setText("群聊");

		// 返回按钮
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		// 成员按钮
		getMembers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent m_intent = new Intent(GroupChatAty.this,
						GroupMembersAty.class);
				Bundle bundle = new Bundle();
				bundle.putInt(MyConfig.KEY_GROUPID, groupid);
				m_intent.putExtras(bundle);
				startActivity(m_intent);
			}
		});
	}

	/** 加载聊天列表 */
	private void loadChats(int nums) {
		//加载已读信息
		chatList = dataBaseHelper.queryMultiGroupNewsRead(groupid,nums);
		if(chatList.size()>0){
			adapter.clear();
			adapter.addAll(chatList);
			listView.onRefreshComplete();
		}
		//开启线程加载未读信息
		new Thread(){
			@Override
			public void run() {
				while(MyConfig.CHATING_FLAG){
					List<GroupNewsDTO> list = dataBaseHelper.queryMultiGroupNewsUnRead(groupid);
					if(list.size()>0){
						chatList.addAll(list);
						myHandler.sendEmptyMessage(1);
						try {
							sleep(500);
						} catch (InterruptedException e) {
							Log.e(TAG, "sleep error!");
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}
	
	class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			adapter.clear();
			adapter.addAll(chatList);
			listView.onRefreshComplete();
			//更新未读为已读
			dataBaseHelper.updataGroupNews(groupid);
		}
		
	}
	/** 监听事件 */
	private void setOnListener() {
		// 发送聊天内容
		btn_sendword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(editText.getText())) {
					Toast.makeText(GroupChatAty.this, "no words to send",
							Toast.LENGTH_LONG).show();
				} else {
					String content_str = editText.getText().toString();
					editText.setText("");
					sendWords(content_str);
				}
			}

		});

	}

	/** 发送聊天消息 */
	private void sendWords(String content_str) {
		if (chatList.size() > 30) {
			for (int i = 0; i < chatList.size(); i++) {
				chatList.remove(i);
			}
		}
		adapter.notifyDataSetChanged();

		new SendWords(MyConfig.getCachedAccount(GroupChatAty.this),
				MyConfig.getCachedToken(GroupChatAty.this), groupid,MyConfig.MSG_TEXT,
				content_str, new SendWords.SuccessCallback() {

					@Override
					public void onSuccess() {
						// 发送成功
					}
				}, new SendWords.FailCallback() {

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
								// /
								chatList.get(chatList.size() - 1).setContent(
										chatList.get(chatList.size() - 1)
												.getContent()
												+ "error to send ");
								adapter.notifyDataSetChanged();
								// /
								Log.e("tag_chatAty", VolleyErrorHelper
										.getMessage(result, GroupChatAty.this));
								Toast.makeText(GroupChatAty.this,
										R.string.generic_error,
										Toast.LENGTH_LONG).show();
								super.onPostExecute(result);
							}

						}.execute(errorCode);
					}
				});
	}
	@Override
	protected void onStop() {
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll("sendWordsPost");
		MyConfig.CHATING_FLAG = false;
	}
	
     

}
