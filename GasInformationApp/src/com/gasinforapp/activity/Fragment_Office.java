package com.gasinforapp.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.uploadtest.PostUploadActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment_Office extends Fragment {

	private ViewPager oViewPager;
	private FragmentPagerAdapter oAdapter;
	private List<Fragment> oDatas;

	private TextView oOneTextView;
	private TextView oTwoTextView;
	private TextView oThreeTextView;
	private TextView oFourTextView;

	private LinearLayout one;
	private LinearLayout two;
	private LinearLayout three;
	private LinearLayout four;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.office_fragment, container, false);
		// 得到控件

		initView();
		turn_add();
		turn_mymenu();
		return view;
	}

	private void initView() {
		oViewPager = (ViewPager) view.findViewById(R.id.id_viewpager02);

		oOneTextView = (TextView) view.findViewById(R.id.id_office_one);
		oTwoTextView = (TextView) view.findViewById(R.id.id_office_two);
		oThreeTextView = (TextView) view.findViewById(R.id.id_office_three);
		oFourTextView = (TextView) view.findViewById(R.id.id_office_four);

		one = (LinearLayout) view.findViewById(R.id.id_office_01);
		two = (LinearLayout) view.findViewById(R.id.id_office_02);
		three = (LinearLayout) view.findViewById(R.id.id_office_03);
		four = (LinearLayout) view.findViewById(R.id.id_office_04);

		oDatas = new ArrayList<Fragment>();

		Fragment_toDoAffairs tab001 = new Fragment_toDoAffairs();
		Fragment_haveDoneAffairs tab002 = new Fragment_haveDoneAffairs();
		Fragment_requestAffairs tab003 = new Fragment_requestAffairs();
		Fragment_replyAffairs tab004 = new Fragment_replyAffairs();

		oDatas.add(tab001);
		oDatas.add(tab002);
		oDatas.add(tab003);
		oDatas.add(tab004);
		
		one.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				resetTextView();
				oViewPager.setCurrentItem(0);
				oOneTextView.setTextColor(Color.parseColor("#FFCC00"));
				one.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		});
		
		two.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				resetTextView();
				oViewPager.setCurrentItem(1);
				oTwoTextView.setTextColor(Color.parseColor("#FFCC00"));
		    	two.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		});
		
		three.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				resetTextView();
				oViewPager.setCurrentItem(2);
				oThreeTextView.setTextColor(Color.parseColor("#FFCC00"));
		    	three.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		});
		
		four.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				resetTextView();
				oViewPager.setCurrentItem(3);
				oFourTextView.setTextColor(Color.parseColor("#FFCC00"));
		    	four.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		oAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

			@Override
			public int getCount() {
				return oDatas.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return oDatas.get(arg0);
			}
		};

		oViewPager.setAdapter(oAdapter);

		oViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
				case 0:
					oOneTextView.setTextColor(Color.parseColor("#FFCC00"));
					one.setBackgroundColor(Color.parseColor("#FFFFFF"));
					break;
				case 1:
					oTwoTextView.setTextColor(Color.parseColor("#FFCC00"));
					two.setBackgroundColor(Color.parseColor("#FFFFFF"));
					break;
				case 2:
					oThreeTextView.setTextColor(Color.parseColor("#FFCC00"));
					three.setBackgroundColor(Color.parseColor("#FFFFFF"));
					break;
				case 3:
					oFourTextView.setTextColor(Color.parseColor("#FFCC00"));
					four.setBackgroundColor(Color.parseColor("#FFFFFF"));
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		Intent getintent = getActivity().getIntent();
		Bundle bundle = getintent.getExtras();
		tab001.setArguments(bundle);
		tab002.setArguments(bundle);
		tab003.setArguments(bundle);
		tab004.setArguments(bundle);
	}

	protected void resetTextView() {
		oOneTextView.setTextColor(Color.parseColor("#BCBCBC"));
		oTwoTextView.setTextColor(Color.parseColor("#BCBCBC"));
		oThreeTextView.setTextColor(Color.parseColor("#BCBCBC"));
		oFourTextView.setTextColor(Color.parseColor("#BCBCBC"));

		one.setBackgroundColor(Color.parseColor("#F2F2F2"));
		two.setBackgroundColor(Color.parseColor("#F2F2F2"));
		three.setBackgroundColor(Color.parseColor("#F2F2F2"));
		four.setBackgroundColor(Color.parseColor("#F2F2F2"));

	}
	
	//跳转至添加新办公请求
	private Button btn_more;
	private void turn_add(){
		btn_more = (Button) view.findViewById(R.id.btn_more);
		btn_more.setText("新建");
		btn_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),NewAffairRequestAty.class);
				startActivity(intent);
			}
		});
	}
	private Button mymenu;
	private void turn_mymenu(){
		mymenu = (Button) view.findViewById(R.id.id_menu);
		mymenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),MyMenu.class);
				startActivity(intent);			
				
			}
		});
	}	
}
