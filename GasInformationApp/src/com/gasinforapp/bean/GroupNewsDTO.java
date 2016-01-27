package com.gasinforapp.bean;
/**
 * 群消息的数据访问对象
 * @author 刘挺
 *
 */
public class GroupNewsDTO {
	//群消息的唯一标识
	private int id;
	//群名
	private int groupId;
	//发送该消息的用户
	private String userName;
	//消息正文
	private String content;
	//时间
	private String time;
	//消息类型（文字、图片、文件）
	private int kind;
	//是否已读
	private boolean isRead;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
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
