package com.gasinforapp.activity;

/**
 * 填写办公申请页面
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.User;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.net.BacklogRequest;
import com.gasinforapp.net.MembersList;
import com.gasinforapp.uploadtest.PostUploadActivity;
import com.gasinforapp.uploadtest.ResponseListener;
import com.gasinforapp.uploadtest.UploadApi;
import com.gasinforapp.widget.MultiSpinner;
import com.gasinforapp.widget.MultiSpinner.MultispinnerListener;

public class NewAffairRequestAty extends Activity {
	private static String TAG = "NewAffairRequestAty";
	// 控件
	private EditText et_title;
	private EditText et_content;
	private MultiSpinner sn_approver;
	private Button btn_addfile;
	private Button btn_send;
	private Button back;
	private TextView tv_path;
	private ImageView iv_file;
	// 审批者列表
	private List<User> list_approver;
	private List<String> list_aname;
	// 数据
	private Bitmap bmp;
	private String approverId = "";
	private String path = "";
	private Boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_office);
		findView();
		list_aname = new ArrayList<String>();
		list_approver = new ArrayList<User>();
		getApproverList();
		clickAddFileBtn();
		clickSendBtn();
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void clickSendBtn() {

		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (approverId == "") {
					Toast.makeText(NewAffairRequestAty.this, "请选择审批人",
							Toast.LENGTH_LONG).show();
				} else if ((TextUtils.isEmpty(et_title.getText()))) {
					Toast.makeText(NewAffairRequestAty.this, "请填写标题",
							Toast.LENGTH_LONG).show();
				} else if ((TextUtils.isEmpty(et_content.getText()))) {
					Toast.makeText(NewAffairRequestAty.this, "请填写内容",
							Toast.LENGTH_LONG).show();
				} else {
					// 主要信息和文件分开提交 1、不包括图片文件的部分
					new BacklogRequest(MyConfig
							.getCachedUserid(NewAffairRequestAty.this) + "",
							MyConfig.getCachedToken(NewAffairRequestAty.this),
							approverId, et_title.getText().toString(), "",
							et_content.getText().toString(), "0",
							new BacklogRequest.SuccessCallback() {

								@Override
								public void onSuccess() {
									new AsyncTask<Void, Void, Void>() {
										@Override
										protected Void doInBackground(
												Void... arg0) {
											return null;
										}

										@Override
										protected void onPostExecute(Void result) {
											super.onPostExecute(result);
											Toast.makeText(
													NewAffairRequestAty.this,
													"申请已成功提交",
													Toast.LENGTH_LONG).show();
										}
									}.execute();
								}
							}, new BacklogRequest.FailCallback() {

								@Override
								public void onFail(int errorCode) {
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
																	NewAffairRequestAty.this));
											Toast.makeText(
													NewAffairRequestAty.this,
													R.string.generic_error,
													Toast.LENGTH_LONG).show();
										}
									}.execute(errorCode);
								}
							});
					// 2、上传文件
					if (bmp != null) {
						UploadApi.uploadImg(bmp,
								new ResponseListener<String>() {
									@Override
									public void onErrorResponse(
											VolleyError error) {
										Log.v(TAG,
												"===========VolleyError========="
														+ error);
										Toast.makeText(
												NewAffairRequestAty.this,
												"附件上传失败", Toast.LENGTH_SHORT)
												.show();
									}

									@Override
									public void onResponse(String response) {
										response = response.substring(response
												.indexOf("img src="));
										response = response.substring(8,
												response.indexOf("/>"));
										Log.v(TAG,
												"===========onResponse========="
														+ response);
										Toast.makeText(
												NewAffairRequestAty.this,
												"附件上传成功", Toast.LENGTH_SHORT)
												.show();
									}
								});
					}
				}

			}
		});
	}

	private void findView() {
		et_title = (EditText) findViewById(R.id.et_title);
		et_content = (EditText) findViewById(R.id.et_content);
		sn_approver = (MultiSpinner) findViewById(R.id.sn_approver);
		btn_addfile = (Button) findViewById(R.id.btn_addfile);
		btn_send = (Button) findViewById(R.id.btn_more);
		back = (Button) findViewById(R.id.back01);
		tv_path = (TextView) findViewById(R.id.tv_path);
		iv_file = (ImageView) findViewById(R.id.iv_file);
	}

	private void getApproverList() {

		new MembersList(
				MyConfig.getCachedUserid(NewAffairRequestAty.this) + "",
				MyConfig.getCachedToken(NewAffairRequestAty.this),
				new MembersList.SuccessCallback() {

					@Override
					public void onSuccess(List<User> members) {
						if (members != null)
							list_approver = members;
						for (int i = 0; i < list_approver.size(); i++) {
							list_aname.add(list_approver.get(i).getUserName());
						}
						sn_approver.setItems(list_aname, "请选择审批者",
								new MultispinnerListener() {

									@Override
									public void onItemschecked(boolean[] checked) {
										approverId = "";
										for (int i = 0; i < checked.length; i++) {
											if (checked[i])
												approverId = list_approver.get(
														i).getUserID()
														+ "," + approverId;
										}
									}
								});
					}
				}, new MembersList.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						list_aname.add(" ");
						sn_approver.setItems(list_aname, "请选择审批者",
								new MultispinnerListener() {

									@Override
									public void onItemschecked(boolean[] checked) {
										approverId = "";
										for (int i = 0; i < checked.length; i++) {
											if (checked[i])
												approverId = list_approver.get(
														i).getUserID()
														+ "," + approverId;
										}
									}
								});
						Log.e(TAG, VolleyErrorHelper.getMessage(errorCode,
								NewAffairRequestAty.this));
						Toast.makeText(NewAffairRequestAty.this, "获取领导列表失败",
								Toast.LENGTH_LONG).show();
					}
				});

	}

	// /获取图片及路径**********************

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的

	private void clickAddFileBtn() {
		btn_addfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 选择文件
				// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片

				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				getAlbum.setType(IMAGE_TYPE);
				startActivityForResult(getAlbum, IMAGE_CODE);
			}
		});
	}

	// 重写onActivityResult以获得你需要的信息

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Log.e(TAG, "ActivityResult resultCode error");
			return;
		}

		Bitmap bm = null;

		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
		ContentResolver resolver = getContentResolver();

		// 此处的用于判断接收的Activity是不是你想要的那个
		if (requestCode == IMAGE_CODE) {
			try {
				Uri originalUri = data.getData(); // 获得图片的uri

				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 得到bitmap图片
				iv_file.setImageBitmap(bm);
				this.bmp = bm;

				String[] proj = { MediaStore.Images.Media.DATA };

				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);
				// 按我个人理解 这个是获得用户选择的图片的索引值
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
				// 最后根据索引值获取图片路径
				String path = cursor.getString(column_index);
				this.path = path;
				tv_path.setText(path);
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}
	}
}
