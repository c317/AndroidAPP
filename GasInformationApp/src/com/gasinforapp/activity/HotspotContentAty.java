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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.MyIntent;
import com.gasinforapp.net.Download;
import com.gasinforapp.net.Download.downhandler;

public class HotspotContentAty extends Activity {
	private Button btn_down;		///
	private ImageView iv_down;		///
	private TextView tv_title;
	private TextView tv_size;
	private TextView tv_src;
	private TextView tv_pubtime;
	private LinearLayout back;
	private int flag=0;
	
	private String pubtime;
	private String source;
	private String module;
	private String fileName;
	private String fileurl;
	private static String downloadURL;
	private static String path = MyConfig.APP_DOWNPATH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_download);
		back=(LinearLayout) findViewById(R.id.back00);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		fileName = bundle.getString(MyConfig.KEY_DATA_FILENAME);
		pubtime = bundle.getString(MyConfig.KEY_DATA_PUBTIME);
		source = bundle.getString(MyConfig.KEY_DATA_SOURCE);
		module = bundle.getString(MyConfig.KEY_DATA_MODULE_ID);
		fileurl = bundle.getString(MyConfig.KEY_DATA_URL);
		downloadURL = MyConfig.SERVER_URL_WORK+"download?"+MyConfig.KEY_DATA_MODULE_ID+"="+module+"&fileName=";
		// 下载
//		down(downloadURL + fileurl);
//		MediaScannerConnection.scanFile(HotspotContentAty.this,
//				new String[] { path + fileName }, null, null);
		
		findView();
		fillData();
		loadImage();
		setListener();
		
			back.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void setListener() {
		/*// 下载按钮监听事件
		btn_down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				down(downloadURL + fileurl);
				MediaScannerConnection.scanFile(HotspotContentAty.this,
						new String[] { path + fileName }, null, null);
			}
		});*/
		// 点击图片打开文档,改为按钮
		
		btn_down.setText("下 载");	
		btn_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(flag==0){
					down(downloadURL + fileurl);
					MediaScannerConnection.scanFile(HotspotContentAty.this,
					new String[] { path + fileName }, null, null);
					flag=1;
					btn_down.setText("打 开");	
					}	else{
				Intent intent = MyIntent.getWordFileIntent(path + fileName);
				startActivity(intent);
					}
			}
		});
	}

	private void findView() {
		btn_down = (Button) findViewById(R.id.btn_download);
		iv_down = (ImageView) findViewById(R.id.iv_test);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_size = (TextView) findViewById(R.id.tv_size);
		tv_src = (TextView) findViewById(R.id.tv_src);
		tv_pubtime = (TextView) findViewById(R.id.tv_pubtime);
	}

	private void fillData() {
		tv_title.setText(fileName);
		tv_size.setText("");
		tv_src.setText(source);
		tv_pubtime.setText(pubtime);
	}

	private void loadImage() {

		iv_down.setImageResource(R.drawable.doc);

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
				int status = dl.down2sd("down/", fileName,
						dl.new downhandler() {
							@Override
							public void setSize(int size) {
								System.out.println("size" + size);
								Log.d("log", Integer.toString(size));
							}
						});
				// log输出
				System.out.println("status" + status);
				Log.d("log", Integer.toString(status));
				return null;
			}

		}.execute(url);
	}

}
