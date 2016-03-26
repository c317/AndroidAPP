package com.gasinforapp.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.ChatItemAdapter;
import com.gasinforapp.bean.GroupNewsDTO;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.config.VolleyUtil;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.gasinforapp.net.GroupUploadFile;
import com.gasinforapp.net.SendWords;
import com.gasinforapp.uploadtest.ResponseListener;
import com.gasinforapp.uploadtest.UploadApi;
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
	private int numsPerPage = 10;
	/** the button of sending word */
	private Button btn_sendword;
	/** get send word from the edittext */
	private EditText editText;
	/** the imagebutton to select and send image or file */
	private ImageView iv_chatmore;
	/** the button of fileorphoto*/
	private Button btn_file;

//	private double currentTime = 0, oldTime = 0;
	private GasInforDataBaseHelper dataBaseHelper;
	private Handler myHandler;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 进入聊天页面
		MyConfig.CHATING_FLAG = true;
		dataBaseHelper = GasInforDataBaseHelper
				.getDatebaseHelper(GroupChatAty.this);
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
		loadChats(pageNum * numsPerPage);
	}

	private void findAllView() {
		listView = (PullToRefreshListView) findViewById(R.id.mylv);
		topname = (TextView) findViewById(R.id.topname);
		back = (Button) findViewById(R.id.back01);
		getMembers = (Button) findViewById(R.id.getmembers);
		editText = (EditText) findViewById(R.id.et_sendwords);
		btn_sendword = (Button) findViewById(R.id.button_sendchat);
		btn_file = (Button) findViewById(R.id.getnotice);
		iv_chatmore = (ImageView) findViewById(R.id.chat_more);
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
		// 加载已读信息
		chatList = dataBaseHelper.queryMultiGroupNewsRead(groupid, nums);
		if (chatList.size() > 0) {
			adapter.clear();
			adapter.addAll(chatList);
			listView.onRefreshComplete();
		}
		// 开启线程加载未读信息
		new Thread() {
			@Override
			public void run() {
				while (MyConfig.CHATING_FLAG) {
					List<GroupNewsDTO> list = dataBaseHelper
							.queryMultiGroupNewsUnRead(groupid);
					if (list.size() > 0) {
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

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			adapter.clear();
			adapter.addAll(chatList);
			listView.onRefreshComplete();
			// 更新未读为已读
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

		// 选择图片发送
		iv_chatmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 选择文件
				// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片

				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				getAlbum.setType(IMAGE_TYPE);
				startActivityForResult(getAlbum, IMAGE_CODE);
			}
		});

		//跳转至群内文件页面
		btn_file.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0){
				Intent fintent = new Intent(GroupChatAty.this,GroupFileAty.class);
				Bundle fbundle = new Bundle();
				fbundle.putString(MyConfig.KEY_GROUPID, groupid+"");
				fintent.putExtras(fbundle);
				startActivity(fintent);
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
				MyConfig.getCachedToken(GroupChatAty.this), groupid,
				MyConfig.MSG_TEXT, content_str,
				new SendWords.SuccessCallback() {

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

	// 重写onActivityResult以获得你需要的信息

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Log.e(TAG, "ActivityResult resultCode error");
			return;
		}

		@SuppressWarnings("unused")
		Bitmap bm = null;

		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
		ContentResolver resolver = getContentResolver();

		// 此处的用于判断接收的Activity是不是你想要的那个
		if (requestCode == IMAGE_CODE) {
			try {
				Uri originalUri = data.getData(); // 获得图片的uri
				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 得到bitmap图片
				String[] proj = { MediaStore.Images.Media.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);
				// 按我个人理解 这个是获得用户选择的图片的索引值
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
				// 最后根据索引值获取图片路径
				String path = cursor.getString(column_index);
				File file = new File(path);

				UploadApi.uploadImg(MyConfig.SERVER_URL_GROUP+MyConfig.ACTION_GROUP_FILEUPLOAD,file, new ResponseListener<String>() {
					@Override
					public void onErrorResponse(VolleyError error) {
						new AsyncTask<VolleyError, Void, VolleyError>() {

							@Override
							protected VolleyError doInBackground(
									VolleyError... arg0) {
								return arg0[0];
							}

							@Override
							protected void onPostExecute(VolleyError result) {
								super.onPostExecute(result);
								Log.v(TAG, "===========VolleyError========="
										+ result);
								Toast.makeText(GroupChatAty.this, "图片上传失败，请重试",
										Toast.LENGTH_SHORT).show();
							}
						}.execute(error);

					}

					@Override
					public void onResponse(String response) {
						new AsyncTask<String, Void, String>() {

							@Override
							protected String doInBackground(String... arg0) {
								return arg0[0];
							}

							@Override
							protected void onPostExecute(
									String response) {
								super.onPostExecute(response);
								Log.v(TAG,
										"===========onResponse========="
												+ response);
								try {
									JSONObject obj = new JSONObject(
											response);
									System.out
											.println(response);
									switch (obj
											.optInt(MyConfig.KEY_STATUS)) {
									case MyConfig.RESULT_STATUS_SUCCESS:
										String picurl = obj
												.optString(MyConfig.KEY_PIC_URL);
										//上传好文件后提交为聊天信息
										new GroupUploadFile(
												MyConfig.getCachedUserid(GroupChatAty.this)
														+ "",
												MyConfig.getCachedToken(GroupChatAty.this),
												groupid+"",
												picurl,
												new GroupUploadFile.SuccessCallback() {

													@Override
													public void onSuccess() {
														new AsyncTask<Void, Void, Void>() {
															@Override
															protected Void doInBackground(
																	Void... arg0) {
																return null;
															}

															@Override
															protected void onPostExecute(
																	Void result) {
																super.onPostExecute(result);
																Toast.makeText(
																		GroupChatAty.this,
																		"图片发送成功",
																		Toast.LENGTH_LONG)
																		.show();
															}
														}.execute();
													}
												},
												new GroupUploadFile.FailCallback() {

													@Override
													public void onFail(
															int errorCode) {
														new AsyncTask<Integer, Void, Integer>() {

															@Override
															protected Integer doInBackground(
																	Integer... arg0) {
																return arg0[0];
															}

															@Override
															protected void onPostExecute(
																	Integer result) {
																super.onPostExecute(result);
																Log.e(TAG,
																		VolleyErrorHelper
																				.getMessage(
																						result,
																						GroupChatAty.this));
																Toast.makeText(
																		GroupChatAty.this,
																		R.string.generic_error,
																		Toast.LENGTH_LONG)
																		.show();
															}
														}.execute(errorCode);
													}
												});
										break;
									default:
										Toast.makeText(
												GroupChatAty.this,
												"图片上传失败，请重试",
												Toast.LENGTH_SHORT)
												.show();
										break;
									}
								} catch (JSONException e) {
									Toast.makeText(
											GroupChatAty.this,
											"图片上传失败，请重试",
											Toast.LENGTH_SHORT)
											.show();
									e.printStackTrace();
								}

							}
						}.execute(response);
					}
				});

			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}
	}

}
