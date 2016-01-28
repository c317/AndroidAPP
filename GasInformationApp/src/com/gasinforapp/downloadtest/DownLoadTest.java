package com.gasinforapp.downloadtest;


import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.gasinformationapp_101.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class DownLoadTest extends Activity {
	private Button btn_down;
	private Button btn_openfile;
	private Button btn_openphoto;
	private ImageView iv_test;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);
		btn_down = (Button) findViewById(R.id.btn_download);
		btn_openfile = (Button) findViewById(R.id.btn_openfile);
		btn_openphoto = (Button) findViewById(R.id.btn_openphoto);
		iv_test = (ImageView) findViewById(R.id.iv_test);

		// 获取传递的Intent的Bundle的url键值
		final String url = "http://192.168.0.115:8080/GasInformationAppS/work/download?fileName=a.png";
//		final String url = "http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg";
		btn_down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				down(url);
				MediaScannerConnection.scanFile(DownLoadTest.this, new String[]{"/storage/emulated/0/download/1.png"}, null, null);
			}
		});
		btn_openfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				iv_test.setImageBitmap(getImageThumbnail("/storage/emulated/0/download/1.png", 200, 200)); //下载到sd卡
//				byvolley();
			}
		});
		btn_openphoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
				albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(albumIntent,1);
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
				int status = dl.down2sd("download/", "1.png",
						dl.new downhandler() {
							@Override
							public void setSize(int size) {
								System.out.println("size"+size);
								Log.d("log", Integer.toString(size));
							}
						});
				// log输出
				System.out.println("status"+status);
				Log.d("log", Integer.toString(status));
				return null;
			}

		}.execute(url);
	}

	public Bitmap getImageThumbnail(String uri, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(uri, options);
		options.inJustDecodeBounds = false;
		int beWidth = options.outWidth / width;
		int beHeight = options.outHeight / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(uri, options);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	private void byvolley() {
		RequestQueue requestQueue = Volley.newRequestQueue(DownLoadTest.this);
	    ImageRequest imageRequest = new ImageRequest(  
	    		"http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg",  
	            new Response.Listener<Bitmap>() {  
	                @Override  
	                public void onResponse(Bitmap response) {  
	                	System.out.println(response.toString());
	                    iv_test.setImageBitmap(response);  
	                }  
	            }, 0, 0, Config.RGB_565, new Response.ErrorListener() {  
	                @Override  
	                public void onErrorResponse(VolleyError error) {  
	                	System.out.println(error+"/n"+error.getMessage());
	                    iv_test.setImageResource(R.drawable.ic_launcher);  
	                }  
	            }){
	    	 @Override
	         public Map<String, String> getHeaders() throws AuthFailureError {
	             HashMap<String, String> headers = new HashMap<String, String>();
	             headers.put("Content-Type", "image/jped");
	             return headers;
	         }
	    };  
		
		requestQueue.add(imageRequest);
	}
}
