package com.gasinforapp.bean;
/**
 * 通知公告类消息的数据访问对象
 * @author 刘挺
 *
 */
public class NoticeDTO {
	//唯一标识符
	private int id;
	//标题
	private String title;
	//正文
	private String content;
	//来源
	private String source;
	//发布人
	private String publisher;
	//时间
	private String time;
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
	
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
