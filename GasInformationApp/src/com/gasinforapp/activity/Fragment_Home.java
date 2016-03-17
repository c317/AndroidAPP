package com.gasinforapp.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.config.MyIntent;
import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Contacts.Intents;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;




public class Fragment_Home extends Fragment {

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mDatas;
	

	private TextView mOneTextView;
	private TextView mTwoTextView;
	private TextView mThreeTextView;
	private TextView mFourTextView;
	private TextView mFiveTextView;

	private LinearLayout mOneLinearLayout;
	private LinearLayout mTwoLinearLayout;
	private LinearLayout mThreeLinearLayout;
	private LinearLayout mFourLinearLayout;
	private LinearLayout mFiveLinearLayout;
	
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 view = inflater.inflate(R.layout.home_fragmentt, container, false);
		
		 
		initView();
		turn_mysearch();
		turn_mymenu();

		return view;
		
		
	}
	
	
	private void initView() {

		
        
	mViewPager = (ViewPager)view.findViewById(R.id.id_viewpager);
	
	mOneTextView=(TextView)view.findViewById(R.id.id_tv_one);
	mTwoTextView=(TextView)view.findViewById(R.id.id_tv_two);
	mThreeTextView=(TextView)view.findViewById(R.id.id_tv_three);
	mFourTextView=(TextView)view.findViewById(R.id.id_tv_four);
	mFiveTextView=(TextView)view.findViewById(R.id.id_tv_five);
	
	mOneLinearLayout=(LinearLayout)view.findViewById(R.id.id_tv_one01);
	mTwoLinearLayout=(LinearLayout)view.findViewById(R.id.id_tv_two01);
	mThreeLinearLayout=(LinearLayout)view.findViewById(R.id.id_tv_three01);
	mFourLinearLayout=(LinearLayout)view.findViewById(R.id.id_tv_four01);
	mFiveLinearLayout=(LinearLayout)view.findViewById(R.id.id_tv_five01);
	
	
	mDatas = new ArrayList<Fragment>();
	Fragment_News tab01 = new Fragment_News();
	Fragment_Notice tab02 = new Fragment_Notice();
	Fragment_Hotspot tab03 = new Fragment_Hotspot();
	Fragment_Topic tab04 = new Fragment_Topic();
	Fragment_Data tab05 = new Fragment_Data();
	
	
	mDatas.add(tab01);
	mDatas.add(tab02);
	mDatas.add(tab03);
	mDatas.add(tab04);
	mDatas.add(tab05);
	
	mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
		
		@Override
		public int getCount() {
			return mDatas.size();
		}
		
		@Override
		public Fragment getItem(int arg0) {
			return mDatas.get(arg0);
		}
	};
	
	mViewPager.setAdapter(mAdapter);

	
	mOneLinearLayout.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			resetTextView();
			mViewPager.setCurrentItem(0);
			mOneTextView.setTextColor(Color.parseColor("#0066FF"));	
		}
	});
	
	mTwoLinearLayout.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			resetTextView();
			mViewPager.setCurrentItem(1);
			mTwoTextView.setTextColor(Color.parseColor("#0066FF"));	
		}
	});
	
	mThreeLinearLayout.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			resetTextView();
			mViewPager.setCurrentItem(2);
			mThreeTextView.setTextColor(Color.parseColor("#0066FF"));	
		}
	});
	
	mFourLinearLayout.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			resetTextView();
			mViewPager.setCurrentItem(3);
			mFourTextView.setTextColor(Color.parseColor("#0066FF"));	
		}
	});
	
	mFiveLinearLayout.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			resetTextView();
			mViewPager.setCurrentItem(4);
			mFiveTextView.setTextColor(Color.parseColor("#0066FF"));	
		}
	});
	
	mViewPager.setOnPageChangeListener(
			
			new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			resetTextView();
			switch (position) {
			case 0:
				mOneTextView.setTextColor(Color.parseColor("#0066FF"));
				
				break;
		    case 1:
		    	mTwoTextView.setTextColor(Color.parseColor("#0066FF"));
				break;
		    case 2:
		    	mThreeTextView.setTextColor(Color.parseColor("#0066FF"));
				break;
		    case 3:
		    	mFourTextView.setTextColor(Color.parseColor("#0066FF"));
				break;
		    case 4:
		    	mFiveTextView.setTextColor(Color.parseColor("#0066FF"));
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
	tab01.setArguments(bundle);
	tab02.setArguments(bundle);
	tab03.setArguments(bundle);
	tab04.setArguments(bundle);
	tab05.setArguments(bundle);
}

	protected void resetTextView() {
		mOneTextView.setTextColor(Color.parseColor("#BCBCBC"));
		mTwoTextView.setTextColor(Color.parseColor("#BCBCBC"));
		mThreeTextView.setTextColor(Color.parseColor("#BCBCBC"));
		mFourTextView.setTextColor(Color.parseColor("#BCBCBC"));
		mFiveTextView.setTextColor(Color.parseColor("#BCBCBC"));

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
	
	
	
	
	
	private Button mysearch;
	private void turn_mysearch(){
		mysearch = (Button) view.findViewById(R.id.btn_search);
		mysearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),SearchAty.class);
				startActivity(intent);			
				
			}
		});
	}
}
