package com.gasinforapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.GroupNewsDTO;

public class ChatItemAdapter extends BaseAdapter {
	private Context context;
	private List<GroupNewsDTO> chatList;

	public ChatItemAdapter(Context context, List<GroupNewsDTO> chatList) {
		this.context = context;
		this.chatList = chatList;
	}

	@Override
	public int getCount() {
		return chatList.size();
	}

	@Override
	public GroupNewsDTO getItem(int position) {
		return chatList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		GroupNewsDTO chat = chatList.get(position);
		holder = new Holder();
		if (chat.getIsme() == true) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.chat_item_right, null);
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.chat_item, null);
		}
		holder.tvContent = (TextView) convertView.findViewById(R.id.tvcontent);
		holder.tvAccount = (TextView) convertView.findViewById(R.id.tvaccount);
		holder.tvSendtime = (TextView) convertView
				.findViewById(R.id.tvconttime);

		holder.tvContent.setText(chat.getContent());
		holder.tvAccount.setText(chat.getUserName());
		holder.tvSendtime.setText(chat.getTime());

		return convertView;
	}

	public void addAll(List<GroupNewsDTO> data) {
		this.chatList.addAll(data);
		notifyDataSetChanged();
	}

	public void clear() {
		chatList.clear();
		notifyDataSetChanged();
	}

	private class Holder {
		private TextView tvContent;
		private TextView tvAccount;
		private TextView tvSendtime;

	}
}
