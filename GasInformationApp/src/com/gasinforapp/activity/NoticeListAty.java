package com.gasinforapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.gasinformationapp_101.R;
import com.gasinforapp.adapter.NoticeAdapter;
import com.gasinforapp.bean.NoticeDTO;
import com.gasinforapp.config.MyConfig;
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

//public class NoticeListAty extends Activity{
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.notice_activity);
//	}

public class NoticeListAty extends Activity implements
		OnRefreshListener2<ListView> {
	/** the name of topbar */
	private TextView topname;
	/** pullview */
	private PullToRefreshListView lvNotice;
	/** adapter of listview */
	private NoticeAdapter adapter;
	/** data of list */
	private List<NoticeDTO> noticeList;
	/** param about the page numbers in request */
	private int pageNum = 1;
	/**
	 * param about the groupid numbers in request ,it get data from
	 * Fragment_group
	 */
	private int groupid;
	/** the button return to the previous view */
	private Button back;
	/** the button trun to members list activity */
	private Button getMembers;
	private int numsPerPage =10;
	private GasInforDataBaseHelper dataBaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_activity);
		// get data from Fragment_group
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		groupid = bundle.getInt(MyConfig.KEY_GROUPID);
		

		setTopbar();

		// 得到listview控件
		lvNotice = (PullToRefreshListView) findViewById(R.id.mynotice);
		lvNotice.setMode(Mode.BOTH);
		noticeList = new ArrayList<NoticeDTO>();
		adapter = new NoticeAdapter(this, noticeList);
		ListView actuaListView = lvNotice.getRefreshableView();
		actuaListView.setAdapter(adapter);

		loadNotice(pageNum*numsPerPage);
		setOnListener();

	}

	/** 配置topbar */
	private void setTopbar() {
		// 改topbar的名字
		topname = (TextView) findViewById(R.id.topname);
		topname.setText(R.string.noticetopname);

		//返回按钮
		back = (Button) findViewById(R.id.back01);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		//成员按钮
		getMembers = (Button) findViewById(R.id.getmembers);
		getMembers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent m_intent = new Intent(NoticeListAty.this,GroupMembersAty.class);
				Bundle bundle = new Bundle();
				bundle.putInt(MyConfig.KEY_GROUPID, groupid);
				m_intent.putExtras(bundle);
				startActivity(m_intent);
			}
		});
		
	}

	/**
	 * load notice list
	 */
	private void loadNotice(int Num) {
		noticeList = dataBaseHelper.queryMultiNotice(Num);
								adapter.clear();
								adapter.addAll(noticeList);
								lvNotice.onRefreshComplete();
							
	}

	/**
	 * set act listener like click and pull
	 */
	private void setOnListener() {
		// 设置点击某一通知事件监听器
		lvNotice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println("your click noticeTitle:"
						+ noticeList.get(arg2 - 1).getTitle());
				// 点击跳转至回复页面

			}
		});

		// 设置上拉下拉事件监听器
		lvNotice.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum = 1;
		loadNotice(pageNum*numsPerPage);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNum++;
		loadNotice(pageNum*numsPerPage);
	}
}
