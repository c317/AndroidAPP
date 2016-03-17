package com.gasinforapp.bean;

public class Affairs {
	// 事项编号
	private String itemId;
	// 请求者职工编号
	private String requesterId;
	// 审批者职工编号
	private String approverId;
	// 请求事项标题
	private String requester;
	// 审批者职工编号
	private String approver;
	// 请求事项标题
	private String requestTitle;
	// 请求时间
	private String requestTime;
	// 请求文本
	private String textContent;
	// 请求图片
	private String pictures;
	// 请求图片URL
	private String picURL;
	// 审批角色
	private String approverRole;
	// 回复时间
	private String responseTime;
	// 意见,0表示“未处理”，1表示“已阅，不同意”，2表示“已阅，同意”，3表示“已阅，需修改”
	private int opinion;
	// 修改意见
	private String comment;
	// 是否推送，0表示“未推送”，1表示“已推送”
	private int isPush;
	// 是否阅读,0表示“未阅读”，1表示“已阅读”
	private int isRead;
	// 页数
	private int page;
	// 每页的数量
	private int perpage;
	// 发出请求的部门（处、室）
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
	public String getPicURL() {
		return picURL;
	}

	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}

	public String getApproverRole() {
		return approverRole;
	}

	public void setApproverRole(String approverRole) {
		this.approverRole = approverRole;
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

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerpage() {
		return perpage;
	}

	public void setPerpage(int perpage) {
		this.perpage = perpage;
	}
	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

}
