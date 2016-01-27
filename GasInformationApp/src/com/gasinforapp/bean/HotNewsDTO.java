package com.gasinforapp.bean;

public class HotNewsDTO {
	//唯一标识符
	private int id;
	//标题
	private String title;
	//正文
	private String content;
	//来源
	private String source;
	//消息要素中的时间
	private String pubTime;
	//系统产生该消息的时间
	private String lastTime;
	//是否已读
	private boolean isRead;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(int isRead) {
		if(isRead==1){
			this.isRead = true;
		}else{
			this.isRead = false;
		}
	}
	/**
	 * SQLite数据库存储boolean类型为整型,需要转换
	 */
	public int getReadStatus(){
		if(this.isRead){
			return 1;
		}else{
			return 0;
		}
	}
}
