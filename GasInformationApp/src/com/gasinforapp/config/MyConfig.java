package com.gasinforapp.config;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public  class MyConfig {

	public static final String SERVER_URL =  "http://192.168.0.115:8080/GasInformationAppS/home/";
	public static final String SERVER_URL_OA =  "http://192.168.0.115:8080/GasInformationAppS/oa/";
	public static final String SERVER_URL_GROUP =  "http://192.168.0.115:8080/GasInformationAppS/group/";
//	public static final String SERVER_URL =  "http://darmin.hicp.net:17504/GasInformationAppS/home/";
//	public static final String SERVER_URL_OA =  "http://darmin.hicp.net:17504/GasInformationAppS/oa/";
//	public static final String SERVER_URL_GROUP =  "http://darmin.hicp.net:17504/GasInformationAppS/group/";
	public static final int 	  MODULEID_HOT = 1;
	public static final int 	  MODULEID_ATTENTION= 11;
	public static final int 	  MODULEID_RECOMMEND= 12;
	
	public static final String CHARSET = "utf-8";
	public static final String APP_ID = "com.gasinforapp";
	
	//统一参数名
	public static final String KEY_TOKEN = "token";
	public static final String KEY_ACTION = "action";
	public static final String KEY_STATUS = "status";
	public static final String KEY_CONTACTS = "contatcs";
	public static final String KEY_PAGE = "pageNum";
	public static final String KEY_PERPAGE = "perpage";
	public static final String KEY_NEWS_ID = "id";
	public static final String KEY_NEWS_MODULE_ID = "module";
	public static final String KEY_NEWS_TITLE = "title";
	public static final String KEY_NEWS_CONTENT = "content";
	public static final String KEY_NEWS_PUBTIME = "pubTime";
	public static final String KEY_NEWS_SOURCE = "originSource";
	public static final String KEY_NEWS = "news";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_USER_ACCOUNT = "account";
	public static final String KEY_USERID = "userID";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_GROUPS = "group";
	public static final String KEY_GROUPNAME = "groupName";
	public static final String KEY_GROUPID = "groupID";
	public static final String KEY_NOREADNUM = "noreadnum";
	public static final String KEY_NOTICES = "notices";
	public static final String KEY_NOTICEID = "notiID";
	public static final String KEY_NOTICE_TITLE = "title";
	public static final String KEY_SENDGROUP = "sendGroup";
	public static final String KEY_GETGROUP = "getGroup";
	public static final String KEY_SENDTIME = "sendTime";
	public static final String KEY_COMMENTS = "comments"; 
	public static final String KEY_COMMENTID = "commentID"; 
	public static final String KEY_DEPARTMENT=  "department"; 
	public static final String KEY_JOB = "job"; 
	public static final String KEY_MEMBERS = "users"; 
	public static final String KEY_MEMBERSID = "memberid"; 
	public static final String KEY_MEMBERSIDS = "memberids"; 
	public static final String KEY_MEMBERSACCOUNT = "memberaccount"; 
	public static final String KEY_MEMBERS_MTYPE = "mtype"; 
	public static final String KEY_MESSAGE = "msg"; 
	public static final String KEY_TODOITEMS = "toDoItems";
	public static final String KEY_TODOCONTENT = "toDoContent";
	public static final String KEY_HAVEDONEITEMS = "haveDoneItems";	
	public static final String KEY_OFFICEREQUESTITEMS = "officeRequestItems";	
	public static final String KEY_OFFICEREPLYITEMS = "officeReplyItems";	
	public static final String KEY_AFFAIRS_ITEMID = "itemId";
	public static final String KEY_AFFAIRS_REQUESTERID = "requesterId";
	public static final String KEY_AFFAIRS_APPROVERID = "approverId";
	public static final String KEY_AFFAIRS_TITLE = "title";
	public static final String KEY_AFFAIRS_TEXTCONTENT = "textContent";
	public static final String KEY_AFFAIRS_PICTURES = "pictures";
	public static final String KEY_AFFAIRS_DEPARTMENT = "department";
	public static final String KEY_AFFAIRS_REQUESTTIME = "requestTime";
	public static final String KEY_AFFAIRS_REPLYTIME = "replyTime";
	public static final String KEY_AFFAIRS_COMMENT = "comment";
	public static final String KEY_AFFAIRS_TEXTCOMMENT = "textComment";
	public static final String KEY_CHATS = "chats";
	public static final String KEY_MSG_KIND = "kind";
	
	
	public static final String ACTION_LOGIN = "loginAction";
	public static final String ACTION_GETNEWSLIST = "getNewsListAction";
	public static final String ACTION_GETGROUPLIST = "getGroupListAction";
	public static final String ACTION_GETNOTICELIST = "getNoticeListAction";
	public static final String ACTION_GETCHATLIST = "getChatListAction";
	public static final String ACTION_GETCOMMENT = "getCommentListAction";
	public static final String ACTION_GETNEWS= "getNewsContentAction";
	public static final String ACTION_GETMEMBERS= "getMembersAction";
	public static final String ACTION_MANAGEMEMBERS= "manageMemberAction";
	public static final String ACTION_ADDMEMBERS = "addBatchMembersAction";
	public static final String ACTION_GETALLUSER = "getAllMembersAction";
	public static final String ACTION_PUSHNOTICE= "pushAction";
	public static final String ACTION_SENDWORD= "chatAction";
	public static final String ACTION_CHECKTODOLIST = "checkToDoList";
	public static final String ACTION_CHECKTODODETAIL = "checkToDoDetail";	
	public static final String ACTION_CHECKHAVEDONELIST = "checkHaveDoneList";
	public static final String ACTION_CHECKHAVEDONEDETAIL = "checkHaveDoneDetail";	
	public static final String ACTION_CHECKOFFICEREQUESTLIST = "checkOfficeRequestList";
	public static final String ACTION_CHECKOFFICEREQUESTDETAIL = "checkOfficeRequestDetail";
	public static final String ACTION_CHECKOFFICEREPLYLIST = "checkOfficeReplyList";
	public static final String ACTION_CHECKOFFICEREPLYDETAIL = "checkOfficeReplyDetail";
	public static final String ACTION_SENDAFFAIRSREQUEST = "sendAffairsRequest";
	public static final String ACTION_RESENDAFFAIRSREQUEST = "resendAffairsRequest";
	public static final String ACTION_SENDAFFAIRSREPLY = "sendAffairsReply";
	public static final String ACTION_GETRECENTNEWS = "getRecentNews";
	public static final String ACTION_FREGUENTQUERY = "frequentQuery";
	
	public static final int RESULT_STATUS_SUCCESS = 1;
	public static final int RESULT_STATUS_FAIL = 0;
	public static final int RESULT_STATUS_INVALID_TOKEN = 2;//token失效
	public static final int RESULT_STATUS_NOTFOUND= 3;//没找到被操做的人
	public static final int RESULT_STATUS_PASSWORD_ERROR= 2;//没找到被操做的人
	public static final int RESULT_STATUS_REPEATED = 4;//重复添加
	

	public static final int ACTIVITY_RESULT_NEED_REFRESH = 10000;
	//是否进入群聊界面标志
	public static Boolean CHATING_FLAG = false; 
	//消息类型
	public static final int MSG_TEXT = 1;
	public static final int MSG_PHOTO = 2;
	//轮询时请求获取消息的类别
	public static final int MODULEID_NOTICE = 1;
	public static final int MODULEID_HOTNEWS = 2;
	public static final int MODULEID_GROUPNEWS = 3;
	//轮询的参数名
	public static final String MOUDLEID = "moduleId";
	public static final String LASTTIME = "time";
	public static final String LASTTIMES = "times";
	public static final String USERID = "userId";
	public static final String GROUPIDS = "groupIds";
	//轮询时解析json的键
	public static final String NEWS_ID = "id";
	public static final String NEWS_TITLE = "title";
	public static final String NEWS_CONTENT = "content";
	public static final String NEWS_KIND = "kind";
	public static final String NEWS_PUBLISHER = "publisher";
	public static final String NEWS_SOURCE = "source";
	public static final String NEWS_PUBTIME = "pubTime";
	public static final String NEWS_TIME = "time";
	public static final String NEWS_LASTTIME = "lastTime";
	public static final String NEWS_GROUPNAME = "groupName";
	public static final String NEWS_GROUPID = "groupId";
	public static final String NEWS_USERNAME = "userName";
	public static final String MESSAGE_GROUPS = "messages";
	public static final String MESSAGE_GROUP = "message";
	
/**
 * @param context
 * @return 缓存的token
 */
	public static String getCachedToken(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_TOKEN, null);
	}
	/**
	 * 将token缓存
	 * @param context
	 * @param token
	 */
	public static void cacheToken(Context context,String token){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(KEY_TOKEN, token);
		e.commit();
	}
	/**
	 * @param context
	 * @return 缓存帐号
	 */
	public static String getCachedAccount(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_USER_ACCOUNT, null);
	}
	/**
	 * 将账号缓存
	 * @param context
	 * @param account
	 */
	public static void cacheAccount(Context context,String account){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(KEY_USER_ACCOUNT, account);
		e.commit();
	}
	/**
	 * @param context
	 * @return 缓存密码
	 */
	public static String getCachedPassword(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_PASSWORD, null);
	}
	/**
	 * 将密码缓存
	 * @param context
	 * @param password
	 */
	public static void cachePassword(Context context,String password){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(KEY_PASSWORD, password);
		e.commit();
	}
	/**
	 * @param context
	 * @return 缓存userId
	 */
	public static int getCachedUserid(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getInt(KEY_USERID, 0);
	}
	/**
	 * 将用户id缓存
	 * @param context
	 * @param userid
	 */
	public static void cacheUserid(Context context,int userId){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putInt(KEY_USERID, userId);
		e.commit();
	}
}
