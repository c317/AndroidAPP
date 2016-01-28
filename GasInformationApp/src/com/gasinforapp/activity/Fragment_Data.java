package com.gasinforapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.downloadtest.DownLoadTest;

public class Fragment_Data extends Fragment{
	private LinearLayout layout;
	
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.t0001, container, false);
		 layout = (LinearLayout) getActivity().findViewById(R.id.turn_to_download_test);
		 layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),DownLoadTest.class);
				startActivity(intent);
			}
		});
		return view;
	}

}