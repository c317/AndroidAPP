package com.gasinforapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.gasinforapp.datebase.GasInforDataBaseHelper;
import com.jauker.widget.BadgeView;

/*import android.print.PrintAttributes.Margins;*/

public class GroupAdapter extends BaseAdapter {
	private Context context;
	private List<Group> groupList;
	private GasInforDataBaseHelper dbhelper;

	/*
	 * layout = (LinearLayout) findViewById(R.id.redpoint01);
	 * 
	 * badgeView = new BadgeView(this); badgeView.setTargetView(layout);
	 * badgeView.setBadgeGravity(Gravity.CENTER | Gravity.CENTER);
	 * badgeView.setBadgeCount(10);
	 */

	public GroupAdapter(Context context, List<Group> groupList) {
		this.context = context;
		this.groupList = groupList;
		dbhelper = GasInforDataBaseHelper.getDatebaseHelper(context);
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.group_item, null);
			holder.tvGroupName = (TextView) convertView
					.findViewById(R.id.tvGroupName);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.redpoint01);

		} else {
			holder = (Holder) convertView.getTag();
		}
		Group group = groupList.get(position);
		BadgeView badgeView = new BadgeView(context);
		badgeView.setTargetView(holder.layout);
		badgeView.setBadgeGravity(Gravity.RIGHT | Gravity.CENTER);
		
		int numnoread = dbhelper.getUnReadNumOfGroupNews(groupList.get(position).getGroupID());
		badgeView.setBadgeCount(numnoread);
		convertView.setTag(holder);
		holder.tvGroupName.setText(group.getGroupName());

		return convertView;
	}

	public void addAll(List<Group> data) {
		this.groupList.addAll(data);
		notifyDataSetChanged();
	}

	public void clear() {
		groupList.clear();
		notifyDataSetChanged();
	}

	private class Holder {
		private TextView tvGroupName;
		private LinearLayout layout;

	}

}
