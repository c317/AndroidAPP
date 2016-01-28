package com.gasinforapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.downloadtest.DownLoadTest;

public class Fragment_Data extends Fragment{
	private Button btn_d;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.data0test1, container, false);
		 btn_d = (Button) view.findViewById(R.id.turn_to_download_test);
		 btn_d.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("xxxxxxxxxxxxxx");
				Intent intent = new Intent(getActivity(),DownLoadTest.class);
				startActivity(intent);
				
			}
		});
		return view;
	}
}