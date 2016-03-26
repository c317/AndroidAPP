package com.gasinforapp.bean;

public class Notice {
	private int notiID;
	private int sendGroup;
	private int getGroup;
	private String sendtime;
	private String content;
	private String title;

	public int getNotiID() {
		return notiID;
	}

	public void setNotiID(int notiID) {
		this.notiID = notiID;
	}

	public int getSendGroup() {
		return sendGroup;
	}

	public void setSendGroup(int sendGroup) {
		this.sendGroup = sendGroup;
	}

	public int getGetGroup() {
		return getGroup;
	}

	public void setGetGroup(int getGroup) {
		this.getGroup = getGroup;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
