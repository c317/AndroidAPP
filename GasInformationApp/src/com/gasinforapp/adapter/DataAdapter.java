package com.gasinforapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gasinformationapp_101.R;
import com.gasinforapp.bean.Data;

public class DataAdapter extends BaseAdapter {
	private Context context;
	private List<Data> dataList;

	public DataAdapter(Context context, List<Data> newsList) {
		this.context = context;
		this.dataList = newsList;
	}


	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Data getItem(int position) {
		return dataList.get(position);
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
					R.layout.data0test2, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvsrc = (TextView) convertView.findViewById(R.id.tv_src);
			holder.tvpubtime = (TextView) convertView.findViewById(R.id.tv_pubtime);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Data dt = dataList.get(position);
		holder.tvTitle.setText(dt.getFileName());
		holder.tvsrc.setText(dt.getSource());
		holder.tvpubtime.setText(dt.getPubTime());
		return convertView;
	}

	public void addAll(List<Data> data){
		this.dataList.addAll(data);
		notifyDataSetChanged();
	}
	
	public void clear(){
		dataList.clear();
		notifyDataSetChanged();
	}
	private class Holder {
		private TextView tvTitle;
		private TextView tvsrc;
		private TextView tvpubtime;
	}

}
