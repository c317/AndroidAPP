package com.gasinforapp.activity;

public class Contacts_SortModel {

	private String name; // 显示的数据
	private String sortLetters; // 显示数据拼音的首字母
	private String dept; 
	private String number; 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public String getNum() {
		return number;
	}

	public void setNum(String number) {
		this.number = number;
	}
}