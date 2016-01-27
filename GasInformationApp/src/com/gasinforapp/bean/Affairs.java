package com.gasinforapp.bean;

public class Affairs {
	//事项编号
	private String itemId;
	//请求者职工编号
	private String requesterId;
	//审批者职工编号
	private String approverId;
	//请求事项标题
	private String requestTitle;
	//请求时间
	private String requestTime;
	//请求文本
	private String textContent;
	//请求图片URL
	private String pictureUrl;
	//审批角色
	private String approverRole;
	//回复时间
	private String replyTime;
	//意见,0表示“已阅，不同意”，1表示“已阅，同意”，2表示“已阅，需修改”
	private int opinion;
	//修改意见
	private String comment;
	//处理状态,0表示“已发送”，1表示“处理中”
	private int dealStatus;
	//是否推送，0表示“未推送”，1表示“已推送”
	private int isPush;
	//是否阅读,0表示“未阅读”，1表示“已阅读”
	private int isRead;
	//发出请求的部门（处、室）
	private String department;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getRequesterId() {
		return requesterId;
	}
	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}
	public String getApproverId() {
		return approverId;
	}
	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}
	public String getRequestTitle() {
		return requestTitle;
	}
	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getApproverRole() {
		return approverRole;
	}
	public void setApproverRole(String approverRole) {
		this.approverRole = approverRole;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public int getOpinion() {
		return opinion;
	}
	public void setOpinion(int opinion) {
		this.opinion = opinion;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(int dealStatus) {
		this.dealStatus = dealStatus;
	}
	public int getIsPush() {
		return isPush;
	}
	public void setIsPush(int isPush) {
		this.isPush = isPush;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}	
}
