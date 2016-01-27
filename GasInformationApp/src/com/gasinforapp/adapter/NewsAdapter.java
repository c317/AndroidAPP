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
import com.gasinforapp.bean.HotNewsDTO;

public class NewsAdapter extends BaseAdapter {
	private Context context;
	private List<HotNewsDTO> newsList;

	public NewsAdapter(Context context, List<HotNewsDTO> newsList) {
		this.context = context;
		this.newsList = newsList;
	}


	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public HotNewsDTO getItem(int position) {
		return newsList.get(position);
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
					R.layout.news_item, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvPic = (ImageView) convertView.findViewById(R.id.tvPic);
			holder.tvsrc = (TextView) convertView.findViewById(R.id.tvsrc);
			holder.tvpubtime = (TextView) convertView.findViewById(R.id.tvpubtime);
			holder.tvIsRead = (TextView) convertView.findViewById(R.id.tvisread);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		HotNewsDTO news = newsList.get(position);
		holder.tvTitle.setText(news.getTitle());
		holder.tvsrc.setText(news.getSource());
		holder.tvpubtime.setText(news.getPubTime());
		setPic(holder,news);
		setIsRead(holder,news);
		return convertView;
	}

	public void addAll(List<HotNewsDTO> data){
		this.newsList.addAll(data);
		notifyDataSetChanged();
	}
	
	public void clear(){
		newsList.clear();
		notifyDataSetChanged();
	}
	private void setIsRead(Holder holder,HotNewsDTO news){
		if(news.isRead()){
			holder.tvIsRead.setText("");
		}else{
			holder.tvIsRead.setText("●");
		}
	}
	private void setPic(Holder holder,HotNewsDTO news){
		if(news.getSource().equals("中国石油新闻中心")){
			holder.tvPic.setImageResource(R.drawable.a_zgsyxwzx);
		}else if(news.getSource().equals("中国海油新闻网")){
			holder.tvPic.setImageResource(R.drawable.a_zghyxww);
		}else if(news.getSource().equals("中国非常规油气网")){
			holder.tvPic.setImageResource(R.drawable.a_zgfcgyqw);
		}else if(news.getSource().equals("国家能源网")){
			holder.tvPic.setImageResource(R.drawable.a_gjnyw);
		}else if(news.getSource().equals("国家石油和化工网")){
			holder.tvPic.setImageResource(R.drawable.a_gjsyhhgw);
		}else if(news.getSource().equals("中国石化新闻网")){
			holder.tvPic.setImageResource(R.drawable.a_zgshxww);
		}else{
			holder.tvPic.setImageResource(R.drawable.a_else);
		}
	}
	private class Holder {
		private TextView tvTitle;
		private TextView tvsrc;
		private TextView tvpubtime;
		private ImageView tvPic;
		private TextView tvIsRead;
	}

}
