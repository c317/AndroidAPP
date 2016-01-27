package com.gasinforapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.User;

public class UserAdapter extends BaseAdapter {
	private Context context;
	private List<User> userList;
	private Boolean ischeckbox;

	public UserAdapter(Context context, List<User> userList, Boolean ischeckbox) {
		this.context = context;
		this.userList = userList;
		this.ischeckbox = ischeckbox;
	}

	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public User getItem(int position) {
		return userList.get(position);
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
			if (!ischeckbox) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.user_item, null);
			}else{
				convertView = LayoutInflater.from(context).inflate(
						R.layout.check_member_item, null);
			}
			holder.tvUserName = (TextView) convertView
					.findViewById(R.id.tvName);
			holder.tvJob = (TextView) convertView.findViewById(R.id.job);
			holder.tvDepartment = (TextView) convertView
					.findViewById(R.id.department);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		User user = userList.get(position);
		holder.tvUserName.setText(user.getUserName());
		holder.tvJob.setText(user.getJob());
		holder.tvDepartment.setText(user.getDepartment());
		return convertView;
	}

	public void addAll(List<User> data) {
		this.userList.addAll(data);
		notifyDataSetChanged();
	}

	public void clear() {
		userList.clear();
		notifyDataSetChanged();
	}

	private class Holder {
		private TextView tvUserName;
		private TextView tvJob;
		private TextView tvDepartment;
	}

}
