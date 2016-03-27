package com.gasinforapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.net.CreateGroup;
import com.gasinforapp.net.CreateGroup.FailCallback;
import com.gasinforapp.net.CreateGroup.SuccessCallback;

public class CreateGroupAty extends Activity {
	private LinearLayout back;
	private Button button;
	private EditText et_groupName;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creategroup);
		button = (Button) findViewById(R.id.button1);
		back = (LinearLayout) findViewById(R.id.back00);
		et_groupName = (EditText) findViewById(R.id.groupName);
		
		
		back.setOnClickListener(new OnClickListener() {
				@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!TextUtils.isEmpty(et_groupName.getText())){										
					new CreateGroup(MyConfig.getCachedUserid(CreateGroupAty.this)
						+ "", MyConfig.getCachedToken(CreateGroupAty.this),
						et_groupName.getText().toString(), new SuccessCallback() {
                         
					      
							@Override
							public void onSuccess() {
                                
									Toast.makeText(CreateGroupAty.this,
											R.string.success_to_create_groups_data,
											Toast.LENGTH_LONG).show();
									         finish();
							
							}
						}, new FailCallback() {

							@Override
							public void onFail(int errorCode) {

								Toast.makeText(CreateGroupAty.this,
										R.string.fail_to_create_groups_data,
										Toast.LENGTH_LONG).show();
							}
						});
				}else {
					Toast.makeText(CreateGroupAty.this,
							R.string.fail_to_create_groups_data1,
							Toast.LENGTH_LONG).show();
				 };
				
			}

		});

	}
}
