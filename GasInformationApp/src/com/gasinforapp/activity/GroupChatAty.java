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
import com.gasinforapp.bean.ChatItem;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.net.ChatList;
import com.gasinforapp.net.ChatList.FailCallback;
import com.gasinforapp.net.ChatList.SuccessCallback;
import com.gasinforapp.net.SendWords;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class GroupChatAty extends Activity {
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
	private List<ChatItem> chatList;
	/** adapter to show data on the view */
	private ChatItemAdapter adapter;
	/** the page number of the list */
	private int pageNum = 1;
	/** the button of send word */
	private Button btn_sendword;
	/** get send word from the edittext */
	private EditText editText;
	private double currentTime = 0, oldTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_chat_activity);
		// get group id
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		groupid = bundle.getInt(MyConfig.KEY_GROUPID);
		// 初始化控件
		findAllView();
		chatList = new ArrayList<ChatItem>();
		// //////////////以下是测试数据////////////////
		ChatItem c1 = new ChatItem();
		c1.setContent("233333");
		c1.setSendtime("01:01");
		c1.setUserName("zz");
		chatList.add(c1);
		ChatItem c2 = new ChatItem();
		c2.setContent("233333");
		c2.setSendtime("01:01");
		c2.setUserName("zz");
		chatList.add(c2);
		ChatItem c3 = new ChatItem();
		c3.setContent("233333");
		c3.setSendtime("01:01");
		c3.setUserName("zz");
		chatList.add(c3);
		ChatItem c4 = new ChatItem();
		c4.setContent("233333");
		c4.setSendtime("01:01");
		c4.setUserName("zz");
		chatList.add(c4);
		ChatItem c5 = new ChatItem();
		c5.setContent("233333");
		c5.setSendtime("01:01");
		c5.setUserName("zz");
		chatList.add(c5);
		ChatItem c6 = new ChatItem();
		c6.setContent("233333");
		c6.setSendtime("01:01");
		c6.setUserName("zz");
		chatList.add(c6);
		// //////////////以上是测试数据////////////////
		adapter = new ChatItemAdapter(this, chatList);
		ListView actuaListView = listView.getRefreshableView();
		actuaListView.setAdapter(adapter);

		setTopbar();
		loadChats();
		setOnListener();
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
	private void loadChats() {
		new ChatList(MyConfig.getCachedAccount(GroupChatAty.this),
				MyConfig.getCachedToken(GroupChatAty.this), groupid, pageNum,
				new SuccessCallback() {
					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(List<ChatItem> chats) {
						new AsyncTask<List<ChatItem>, Void, List<ChatItem>>() {

							@Override
							protected List<ChatItem> doInBackground(
									List<ChatItem>... cLists) {
								return cLists[0];
							}

							@Override
							protected void onPostExecute(List<ChatItem> result) {
								super.onPostExecute(result);
								chatList = result;
								adapter.clear();
								adapter.addAll(result);
								listView.onRefreshComplete();
							}
						}.execute(chats);
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
										result, GroupChatAty.this));
								Toast.makeText(GroupChatAty.this,
										R.string.generic_error,
										Toast.LENGTH_LONG).show();
								super.onPostExecute(result);
							}

						}.execute(ErrorCode);
					}

				});

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
		final String time = getTime();

		ChatItem chat = new ChatItem();
		chat.setContent(content_str);
		chat.setIsme(true);
		chat.setUserName("aa");
		chat.setSendtime(time);
		chatList.add(chat);
		if (chatList.size() > 30) {
			for (int i = 0; i < chatList.size(); i++) {
				chatList.remove(i);
			}
		}
		adapter.notifyDataSetChanged();

		new SendWords(MyConfig.getCachedAccount(GroupChatAty.this),
				MyConfig.getCachedToken(GroupChatAty.this), groupid,
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

	// 得到系统时间
	@SuppressLint("SimpleDateFormat")
	private String getTime() {
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		if (currentTime - oldTime >= 5000) {
			oldTime = currentTime;
			return str;
		} else {
			return "";
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		VolleyUtil.getRequestQueue().cancelAll("getChatList");
		VolleyUtil.getRequestQueue().cancelAll("sendWordsPost");
	}

}
