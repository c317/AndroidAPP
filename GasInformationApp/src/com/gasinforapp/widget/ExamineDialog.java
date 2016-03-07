package com.gasinforapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.config.VolleyErrorHelper;
import com.gasinforapp.net.BacklogReply;

public class ExamineDialog extends Dialog {
	private static String TAG; 
	private String select = "";
	private Context mContext;
	private RadioGroup radioGroup;
	private EditText etcomment;
	private Button enter;
	private Button cancel;
	private View layout;
	private String itemId;

	public ExamineDialog(Context context,String itemId) {
		super(context);
		mContext = context;
		this.itemId=itemId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.dialog_examine, null);
		this.setContentView(layout);
		findView();
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						int radioButtonId = arg0.getCheckedRadioButtonId();
						RadioButton rb = (RadioButton) findViewById(radioButtonId);
						select = getSelect(rb.getText().toString());
					}
				});

		enter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (select == "") {
					Toast.makeText(getContext(), "未选择", Toast.LENGTH_LONG)
							.show();
				} else {
					new BacklogReply(MyConfig.getCachedUserid(getContext())
							+ "", MyConfig.getCachedToken(getContext()), "",
							itemId, "", "", select, etcomment.getText()
									.toString(),
							new BacklogReply.SuccessCallback() {

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
											Log.e(TAG, VolleyErrorHelper.getMessage(
													result, getContext()));
											Toast.makeText(getContext(),
													"已提交",
													Toast.LENGTH_LONG).show();
										}
										
									}.execute();
								}
							}, new BacklogReply.FailCallback() {

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
											Log.e(TAG, VolleyErrorHelper.getMessage(
													result, getContext()));
											Toast.makeText(getContext(),
													R.string.generic_error,
													Toast.LENGTH_LONG).show();
										}

									}.execute(errorCode);
								}
							});
				}
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

	}

	private void findView() {
		radioGroup = (RadioGroup) layout.findViewById(R.id.rd_select);
		etcomment = (EditText) layout.findViewById(R.id.et_comment);
		enter = (Button) layout.findViewById(R.id.btn_enter);
		cancel = (Button) layout.findViewById(R.id.btn_cancel);
	}

	private String getSelect(String ss) {
		if (ss.equals("同意")) {
			return "1";
		} else if (ss.equals("需修改")) {
			return "2";
		} else if(ss.equals("不同意")){
			return "0";
		}
		return "";

	}
}
