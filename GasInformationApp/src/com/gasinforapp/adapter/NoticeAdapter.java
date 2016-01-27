package com.gasinforapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.bean.NoticeDTO;

public class NoticeAdapter extends BaseAdapter {
	private Context context;
	private List<NoticeDTO> noticeList;

	public NoticeAdapter(Context context, List<NoticeDTO> noticeList) {
		this.context = context;
		this.noticeList = noticeList;
	}
	
	@Override
	public int getCount() {
		return noticeList.size();
	}

	@Override
	public NoticeDTO getItem(int position) {
		return noticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.notice_item, null);
			holder.tvNoticeTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvSource = (TextView) convertView.findViewById(R.id.tvsrc);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvpubtime);
			holder.tvIsRead = (TextView) convertView.findViewById(R.id.tvisread);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		NoticeDTO notices = noticeList.get(position);
		holder.tvNoticeTitle.setText(notices.getTitle());
		holder.tvSource.setText(notices.getSource());
		holder.tvTime.setText(notices.getTime().split(" ")[0]);
		setIsRead( holder,notices);
		return convertView;
	}
	
	private void setIsRead(Holder holder,NoticeDTO notices){
		if(notices.isRead()){
			holder.tvIsRead.setText("");
		}else{
			holder.tvIsRead.setText("●");
		}
	}
	public void addAll(List<NoticeDTO> data){
		this.noticeList.addAll(data);
		notifyDataSetChanged();
	}

	public void clear(){
		noticeList.clear();
		notifyDataSetChanged();
	}
	private class Holder {
		private TextView tvNoticeTitle;
		private TextView tvSource;
		private TextView tvTime;
		private TextView tvIsRead;
	}

}
