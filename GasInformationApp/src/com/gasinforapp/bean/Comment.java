package com.gasinforapp.bean;

public class Comment {
	private int commentID;
	private int notiID;
	private String sendtime;
	private String content;
	private String userName;

	public int getCommentID() {
		return commentID;
	}

	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}

	public int getNotiID() {
		return notiID;
	}

	public void setNotiID(int notiID) {
		this.notiID = notiID;
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
