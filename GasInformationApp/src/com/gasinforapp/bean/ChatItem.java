package com.gasinforapp.bean;

public class ChatItem {
	private int groupID;
	private String sendtime;
	private String content;
	private String userName;
	private Boolean isme=false;
	public Boolean getIsme() {
		return isme;
	}
	public void setIsme(Boolean isme) {
		this.isme = isme;
	}
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
