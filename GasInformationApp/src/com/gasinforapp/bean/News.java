package com.gasinforapp.bean;

public class   News{
    //分页
    private int rownumber;
    //当前模块
	private int module;
	//to模块
	private int toModule;
	//新闻ID
	private int id;
	//新闻标题
	private String title;
	//当前网站
	private String siteSource;
	//第一来源
	private String originSource;
	//发布时间
	private String pubTime;
	//正文
	private String content;
	//总访问量量
	private int totalVisits;
	//油田
	private String oilField;
	//总权重，管理员能够设置总权重
	private int totalWeight;
	//网站权重
	private int webWeight;
	//visible=0，该新闻前台不可见;=1,可见
	private int visible;
	//网站编码方式
	private String charset;
	//图片路径
	private String imagePath;
	//常规或者非常规
	private int type;
	//上移下移置顶的标识符
	private int updownId;
	
	 private String basin;
	 public String getBasin() {
		return basin;
	}
	public void setBasin(String basin) {
		this.basin = basin;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getOil() {
		return oil;
	}
	public void setOil(String oil) {
		this.oil = oil;
	}
	public int getOutput() {
		return Output;
	}
	public void setOutput(int output) {
		Output = output;
	}
	private String company;
	 private String oil;
	 private int Output;
	
	public int getUpdownId() {
		return updownId;
	}
	public void setUpdownId(int updownId) {
		this.updownId = updownId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getModule() {
		return module;
	}
	public void setModule(int module) {
		this.module = module;
	}
	
	public int getToModule() {
		return toModule;
	}
	public void setToModule(int toModule) {
		this.toModule = toModule;
	}
	
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
	
	public String getSiteSource() {
		return siteSource;
	}
	public void setSiteSource(String siteSource) {
		this.siteSource = siteSource;
	}
	
	public String getOriginSource() {
		return originSource;
	}
	public void setOriginSource(String originSource) {
		this.originSource = originSource;
	}
	
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getTotalVisits() {
		return totalVisits;
	}
	public void setTotalVisits(int totalVisits) {
		this.totalVisits = totalVisits;
	}
	
	public String getOilField() {
		return oilField;
	}
	public void setOilField(String oilField) {
		this.oilField = oilField;
	}
	
	public int getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(int totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	public int getWebWeight() {
		return webWeight;
	}
	public void setWebWeight(int webWeight) {
		this.webWeight = webWeight;
	}
	
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public int getRownumber() {
		return rownumber;
	}
	public void setRownumber(int rownumber) {
		this.rownumber = rownumber;
	}
}