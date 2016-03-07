package com.gasinforapp.activity;

/**
 * 代办事项详情页面
 */
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.Affairs;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.net.BacklogDetail;
import com.gasinforapp.widget.ExamineDialog;

public class AffairsToDoDetailAty extends Activity {

	private TextView tvTitle;
	private TextView tvDepartment;
	private TextView tvApplicant;
	private TextView tvpubtime;
	private TextView tvcontent;
	private Button back;
	private Button btn_examine;
	private String itemId;
	private String aftitle;
	private String afpubtime;
	private String afrequester;
	private String afdepartment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affair_todo);
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
//		getNewsContent();
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
		back = (Button) findViewById(R.id.back01);
		btn_examine = (Button) findViewById(R.id.btn_more);
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
								tvcontent.setText(result.getTextContent());
								super.onPostExecute(result);
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
}
