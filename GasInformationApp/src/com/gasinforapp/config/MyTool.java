package com.gasinforapp.config;
/**
 * 重复使用的小工具
 */
public class MyTool {
	
/**
 * 用来判断办公事项的完成情况
 * @param status
 * @param isread
 * @return
 */
	public static String getStatus(int status,String isread){
		if(status == 0){
			if(isread.equals("true"))
			return "待审批";
			else
				return"待查看";
		}
		else if(status == 1){
			return "已批准";
		}else if(status == 2){
			return "需修改";
		}
		return "";
	}
}
