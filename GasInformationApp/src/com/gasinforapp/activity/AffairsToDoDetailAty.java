package com.gasinforapp.activity;

/**
 * 代办事项详情页面
 */
import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.Affairs;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.MyIntent;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.net.BacklogDetail;
import com.gasinforapp.net.Download;
import com.gasinforapp.widget.ExamineDialog;

public class AffairsToDoDetailAty extends Activity {
	private static String TAG = "AffairsToDoDetailAty";

	private TextView tvTitle;
	private TextView tvDepartment;
	private TextView tvApplicant;
	private TextView tvpubtime;
	private TextView tvcontent;
	private LinearLayout back;
	private Button btn_examine;
	private String itemId;
	private String aftitle;
	private String afpubtime;
	private String afrequester;
	private String afdepartment;
	private String afpicurl;
	private String afpicname;
	private TextView myTextView;  
	private int flag=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affair_todo);
		myTextView = (TextView)findViewById(R.id.topname);        
	    myTextView.setText("待办事项"); 
		findView();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		itemId = bundle.getString(MyConfig.KEY_AFFAIRS_ITEMID);
		aftitle = bundle.getString(MyConfig.KEY_AFFAIRS_TITLE);
		afpubtime = bundle.getString(MyConfig.KEY_AFFAIRS_REQUESTTIME);
		afrequester = bundle.getString(MyConfig.KEY_AFFAIRS_REQUESTER);
		afdepartment = bundle.getString(MyConfig.KEY_AFFAIRS_DEPARTMENT);
		tvTitle.setText(aftitle);
		tvApplicant.setText(afrequester);
		tvDepartment.setText(afdepartment);
		tvpubtime.setText(afpubtime);
		clickBtnDown();
		btn_download.setVisibility(View.INVISIBLE);
		getNewsContent();
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		clickExamineBtn();
	}

	private void findView() {
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvApplicant = (TextView) findViewById(R.id.tv_applicant);
		tvDepartment = (TextView) findViewById(R.id.tv_department);
		tvpubtime = (TextView) findViewById(R.id.tv_pubtime);
		tvcontent = (TextView) findViewById(R.id.tv_content);
		back = (LinearLayout) findViewById(R.id.back00);
		btn_examine = (Button) findViewById(R.id.btn_more);
		btn_examine.setText("审批");
	}

	private void getNewsContent() {
		new BacklogDetail(MyConfig.getCachedUserid(this) + "",
				MyConfig.getCachedToken(this), "", itemId,
				new BacklogDetail.SuccessCallback() {

					@Override
					public void onSuccess(Affairs toDoContent) {
						new AsyncTask<Affairs, Void, Affairs>() {

							@Override
							protected Affairs doInBackground(Affairs... arg0) {
								return arg0[0];
							}

							@Override
							protected void onPostExecute(Affairs result) {
								super.onPostExecute(result);
								tvcontent.setText(result.getTextContent());
								afpicurl = result.getPicURL();
								afpicname = result.getPictures();
								if (!afpicurl.equals("") && afpicurl != null) {
									btn_download.setVisibility(View.VISIBLE);
								}  
							}

						}.execute(toDoContent);

					}
				}, new BacklogDetail.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						new AsyncTask<Integer, Void, Integer>() {

							@Override
							protected Integer doInBackground(Integer... arg0) {
								return arg0[0];
							}

							@Override
							protected void onPostExecute(Integer result) {
								super.onPostExecute(result);
								Log.e("tag", VolleyErrorHelper.getMessage(
										result, AffairsToDoDetailAty.this));
								Toast.makeText(AffairsToDoDetailAty.this,
										R.string.fail_to_load_news_data,
										Toast.LENGTH_LONG).show();
							}

						}.execute(errorCode);

					}
				});
	}

	private void clickExamineBtn() {
		btn_examine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ExamineDialog dialog = new  ExamineDialog(AffairsToDoDetailAty.this,itemId);  
                dialog.show();  
			}
		});
	}
	
	
	private Button btn_download;
	private int module = MyConfig.MODULEID_FILE;
	private static String downloadURL;
	private static String path = MyConfig.APP_DOWNPATH;

	private void clickBtnDown(){
		btn_download=(Button) findViewById(R.id.btn_download);
		downloadURL = MyConfig.SERVER_URL_WORK+"download?"+MyConfig.KEY_DATA_MODULE_ID+"="+module+"&fileName=";
		btn_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(flag==0){
				down(downloadURL + afpicurl);
				MediaScannerConnection.scanFile(AffairsToDoDetailAty.this,
						new String[] { path + afpicname }, null, null);
				flag = 1;
				btn_download.setText("打开附件");
				}else{
				Intent intent = MyIntent.getImageFileIntent(path+afpicname);
				startActivity(intent);}
			}
		});
	}
	private void down(final String url) {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... url) {
				Download dl = new Download(url[0]);

				/**
				 * 下载文件到sd卡，虚拟设备必须要开始设置sd卡容量
				 * downhandler是Download的内部类，作为回调接口实时显示下载数据
				 */
				int status = dl.down2sd("down/", afpicname,
						dl.new downhandler() {
							@Override
							public void setSize(int size) {
								System.out.println("size" + size);
								Log.d(TAG, Integer.toString(size));
							}
						});
				// log输出
				System.out.println("status" + status);
				Log.d(TAG, Integer.toString(status));
				return null;
			}

		}.execute(url);
	}
}
