package com.gasinforapp.activity;

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
import com.gasinforapp.config.MyTool;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.net.BacklogAlreadyDetail;
import com.gasinforapp.net.Download;

public class AffairsHaveDoneDetailAty extends Activity {
	private static String TAG = "AffairsHaveDoneDetailAty";
	
	private TextView tvTitle;
	private TextView tvDepartment;
	private TextView tvStatus;
	private TextView tvApplicant;
	private TextView tvpubtime;
	private TextView tvcontent;
	private TextView tvComment;
	private LinearLayout back;
	private Button one;
	private String itemId;
	private String aftitle;
	private String afpubtime;
	private String afstatus;
	private String afrequester;
	private String afdepartment;
	private String afpicurl;
	private String afpicname;
	private TextView myTextView;  
	private int flag=0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affair_havadone);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvApplicant = (TextView) findViewById(R.id.tv_applicant);
		tvDepartment = (TextView) findViewById(R.id.tv_department);
		tvStatus = (TextView) findViewById(R.id.tv_status);
		tvpubtime = (TextView) findViewById(R.id.tv_pubtime);
		tvcontent = (TextView) findViewById(R.id.tv_content);
		tvComment = (TextView) findViewById(R.id.tv_reply);
		back = (LinearLayout) findViewById(R.id.back00);
		one=(Button) findViewById(R.id.btn_more);
		one.setText(" ");
		myTextView = (TextView)findViewById(R.id.topname);        
	    myTextView.setText("已办事项"); 
	
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		itemId = bundle.getString(MyConfig.KEY_AFFAIRS_ITEMID);
		aftitle = bundle.getString(MyConfig.KEY_AFFAIRS_TITLE);
		afpubtime = bundle.getString(MyConfig.KEY_AFFAIRS_REQUESTTIME);
		afrequester = bundle.getString(MyConfig.KEY_AFFAIRS_REQUESTER);
		afstatus = MyTool.getStatus(bundle.getInt(MyConfig.KEY_AFFAIRS_STATUS),"true");
		afdepartment = bundle.getString(MyConfig.KEY_AFFAIRS_DEPARTMENT);
		tvTitle.setText(aftitle);
		tvApplicant.setText(afrequester);
		tvDepartment.setText(afdepartment);
		tvpubtime.setText(afpubtime);
		tvStatus.setText(afstatus);
		clickBtnDown();
		btn_download.setVisibility(View.INVISIBLE);
		getNewsContent();
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void getNewsContent() {
		new BacklogAlreadyDetail(MyConfig.getCachedUserid(this)+"", MyConfig.getCachedToken(this), "", itemId, new BacklogAlreadyDetail.SuccessCallback() {
			
			@Override
			public void onSuccess(Affairs content) {
				tvComment.setText(content.getComment());
				tvcontent.setText(content.getTextContent());
				afpicurl = content.getPicURL();
				afpicname = content.getPictures();
				if (!afpicurl.equals("") && afpicurl != null) {
					btn_download.setVisibility(View.VISIBLE);
				} 
			}
		}, new BacklogAlreadyDetail.FailCallback() {
			
			@Override
			public void onFail(int errorCode) {
				Log.e("tag", VolleyErrorHelper.getMessage(errorCode,
						AffairsHaveDoneDetailAty.this));
				Toast.makeText(AffairsHaveDoneDetailAty.this,
						R.string.fail_to_load_news_data,
						Toast.LENGTH_LONG).show();
			}
		
		});
	}
	
	
	private Button btn_download;
	private int module = MyConfig.MODULEID_FILE;
	private static String downloadURL;
	private static String path = MyConfig.APP_DOWNPATH;

	private void clickBtnDown(){
		downloadURL = MyConfig.SERVER_URL_WORK+"download?"+MyConfig.KEY_DATA_MODULE_ID+"="+module+"&fileName=";
		afpicurl = "";
		btn_download=(Button) findViewById(R.id.btn_download);
		btn_download.setText("下载附件");
		btn_download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(flag==0){
					down(downloadURL + afpicurl);
					MediaScannerConnection.scanFile(AffairsHaveDoneDetailAty.this,
							new String[] { path + afpicname }, null, null);
					btn_download.setText("打开附件");
					flag=1;
				}
				else{
					
					Intent intent = MyIntent.getImageFileIntent(path+afpicname);
					startActivity(intent);
				}
				

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
