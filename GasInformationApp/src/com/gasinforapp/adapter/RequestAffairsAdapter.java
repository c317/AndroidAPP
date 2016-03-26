package com.gasinforapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.Affairs;
import com.gasinforapp.config.MyTool;

public class RequestAffairsAdapter extends BaseAdapter {
	private Context context;
	private List<Affairs> affairsList;

	public RequestAffairsAdapter(Context context, List<Affairs> affairsList) {
		this.context = context;
		this.affairsList = affairsList;
	}

	@Override
	public int getCount() {
		return affairsList.size();
	}

	@Override
	public Affairs getItem(int position) {
		return affairsList.get(position);
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
					R.layout.affairs_request_item, null);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tvAffairTitle);
			holder.tvApprover = (TextView) convertView
					.findViewById(R.id.tvAffairApprover);
			holder.tvRequsetTime = (TextView) convertView
					.findViewById(R.id.tvAffairPubTime);
			holder.tvStatus = (TextView) convertView
					.findViewById(R.id.tv_status);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Affairs affairs = affairsList.get(position);
		holder.tvTitle.setText(affairs.getRequestTitle());
		holder.tvApprover.setText(affairs.getApprover());
		holder.tvRequsetTime.setText(affairs.getRequestTime());
		holder.tvStatus.setText(MyTool.getStatus(affairs.getOpinion(),affairs.getIsRead()));
		return convertView;
	}

	public void addAll(List<Affairs> data) {
		this.affairsList.addAll(data);
		notifyDataSetChanged();
	}

	public void clear() {
		affairsList.clear();
		notifyDataSetChanged();
	}

	private class Holder {
		private TextView tvRequsetTime;
		private TextView tvApprover;
		private TextView tvTitle;
		private TextView tvStatus;
	}


}
