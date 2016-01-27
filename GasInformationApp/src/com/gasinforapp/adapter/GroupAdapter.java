package com.gasinforapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.Group;
import com.jauker.widget.BadgeView;
/*import android.print.PrintAttributes.Margins;*/

public class GroupAdapter extends BaseAdapter {
	private Context context;
	private List<Group> groupList;

	
	
/*	layout = (LinearLayout) findViewById(R.id.redpoint01);
	
	badgeView = new BadgeView(this);
	badgeView.setTargetView(layout);
	badgeView.setBadgeGravity(Gravity.CENTER | Gravity.CENTER);
	badgeView.setBadgeCount(10);*/

	

	

	public GroupAdapter(Context context, List<Group> groupList) {
		this.context = context;
		this.groupList = groupList;
	}


	@Override
	public int getCount() {
		return groupList.size();
	}

	@Override
	public Group getItem(int position) {
		return groupList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.group_item, null);
			holder.tvGroupName = (TextView) convertView.findViewById(R.id.tvGroupName);	
			holder.layout = (LinearLayout) convertView.findViewById(R.id.redpoint01);
			
		} else {
			holder = (Holder) convertView.getTag();
		}
		BadgeView badgeView = new BadgeView(context);
		badgeView.setTargetView(holder.layout);
		badgeView.setBadgeGravity(Gravity.RIGHT | Gravity.CENTER);
		badgeView.setBadgeCount(10);
		convertView.setTag(holder);
		Group group = groupList.get(position);
		holder.tvGroupName.setText(group.getGroupName());

		return convertView;
	}

	public void addAll(List<Group> data){
		this.groupList.addAll(data);
		notifyDataSetChanged();
	}
	
	public void clear(){
		groupList.clear();
		notifyDataSetChanged();
	}
	private class Holder {
		private TextView tvGroupName;
		private LinearLayout layout;
		
	}
	


}
