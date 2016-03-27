package com.gasinforapp.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gasinforapp.activity.Contacts_SideBar;
import com.gasinforapp.activity.Contacts_SideBar.OnTouchingLetterChangedListener;

import com.example.gasinformationapp_101.R;

public class Contacts_MainActivity extends Activity {
	private ListView sortListView;
	private Contacts_SideBar sideBar;
	private TextView dialog;
	private Contacts_SortAdapter adapter;
	private Contacts_ClearEditText mClearEditText;
	private LinearLayout back;

	/**
	 * 汉字转换成拼音的类
	 */
	private Contacts_CharacterParser characterParser;
	private List<Contacts_SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private Contacts_PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_contacts);
		initViews();
		back = (LinearLayout) findViewById(R.id.back00);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = Contacts_CharacterParser.getInstance();

		pinyinComparator = new Contacts_PinyinComparator();

		sideBar = (Contacts_SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(getApplication(),
						((Contacts_SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
			}
		});

		SourceDateList = filledData(getResources().getStringArray(R.array.name),getResources().getStringArray(R.array.dept),getResources().getStringArray(R.array.number));
		
		

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new Contacts_SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);

		mClearEditText = (Contacts_ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 为ListView填充数据
	 */
	private List<Contacts_SortModel> filledData(String[] name,String[] dept,String[] number) {
		List<Contacts_SortModel> mSortList = new ArrayList<Contacts_SortModel>();

		for (int i = 0; i < name.length; i++) {
			Contacts_SortModel sortModel = new Contacts_SortModel();
			sortModel.setName(name[i]);
			sortModel.setDept(dept[i]);
			sortModel.setNum(number[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(dept[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 */
	private void filterData(String filterStr) {
		List<Contacts_SortModel> filterDateList = new ArrayList<Contacts_SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (Contacts_SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
