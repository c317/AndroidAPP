package com.gasinforapp.datebase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gasinforapp.bean.GroupNewsDTO;
import com.gasinforapp.bean.HotNewsDTO;
import com.gasinforapp.bean.NoticeDTO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * SQLite Database Helper
 * 
 * @author 刘挺
 * 
 */
public class GasInforDataBaseHelper extends SQLiteOpenHelper {

	private static GasInforDataBaseHelper instance;
	
	public static synchronized GasInforDataBaseHelper getDatebaseHelper(
			Context context) {
		if (instance == null) {
			instance = new GasInforDataBaseHelper(context);
		}
		return instance;
	}

	private static final String TAG = "DatabaseHelper";
	public static final String DATABASE_NAME = "GasInfor_db";
	private static final int DATABASE_VIRSON = 1;
	private static SQLiteDatabase db;
	// 消息按时间先后顺序插入表中，ID最大的为最近插入的一条消息
	private static final String ORDER = "_id";
		
	/**
	 * 通知公告表及字段
	 */
	private static final String NOTICE_TABLENAME="notice";
	private static final String NOTICE_ID="id";
	private static final String NOTICE_TITLE="title";
	private static final String NOTICE_CONTENT="content";
	private static final String NOTICE_SOURCE="source";
	private static final String NOTICE_PUBLISHER="publisher";
	private static final String NOTICE_TIME="time";
	private static final String NOTICE_ISREAD="isread";
	
	/**
	 * 热点新闻表及字段
	 */
	private static final String HOTNEWS_TABLENAME="hotnews";
	private static final String HOTNEWS_ID="id";
	private static final String HOTNEWS_TITLE="title";
	private static final String HOTNEWS_CONTENT="content";
	private static final String HOTNEWS_SOURCE="source";
	private static final String HOTNEWS_TIME="time";
	private static final String HOTNEWS_LASTTIME="lastTime";
	private static final String HOTNEWS_ISREAD="isread";
	
	/**
	 * 群消息表及字段
	 */
	private static final String GROUPMESSAGE_TABLENAME="groupmessage";
	private static final String GROUPMESSAGE_GROUPNAME="groupName";
	private static final String GROUPMESSAGE_USERNAME="userName";
	private static final String GROUPMESSAGE_CONTENT="content";
	private static final String GROUPMESSAGE_TIME="time";
	private static final String GROUPMESSAGE_ISREAD="isread";
	
	
	private GasInforDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, new LoggingCursorFactory(),DATABASE_VIRSON);
		db = getWritableDatabase();// 如果数据库不存在则会调用onCreate函数
	}

	public int getDBVersion(){
		return db.getVersion();
	}
	
	public void setDBVersion(int version){
		db.setVersion(version);
	}
	
	/**
	 * 创建数据库并新建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate");
		createTables(db);
	}
	
	/**
	 * 数据库的结构有变化时，修改DATABASE_VIRSON的值，覆盖安装程序时会绕过oncreate方法调用此方法
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "upGrade");
	}
	
	public void createTables(SQLiteDatabase db){
		/**
		 * 通知公告表
		 */
		String sql="create table "+NOTICE_TABLENAME+" ( "+ 
				ORDER 					+" integer primary key autoincrement, " +
				NOTICE_ID				+" integer, "+
				NOTICE_TITLE     		+" text, "+
				NOTICE_CONTENT   		+" text, "+
				NOTICE_SOURCE       	+" text, "+
				NOTICE_PUBLISHER       	+" text, "+
				NOTICE_TIME      	 	+" text, "+
				NOTICE_ISREAD   		+" integer)";
		db.execSQL(sql);
		/**
		 * 	热点新闻表
		 */
		sql="create table "+HOTNEWS_TABLENAME+" ( "+ 
				ORDER + " integer primary key autoincrement, " +
				HOTNEWS_ID				+" integer unique, "+
				HOTNEWS_TITLE     		+" text, "+
				HOTNEWS_CONTENT   		+" text, "+
				HOTNEWS_SOURCE       	+" text, "+
				HOTNEWS_TIME      	 	+" text, "+
				HOTNEWS_LASTTIME      	+" text, "+
				HOTNEWS_ISREAD   		+" integer )";
		db.execSQL(sql);
		/**
		 * 	群消息表
		 */
		sql="create table "+GROUPMESSAGE_TABLENAME+" ( "+ 
				ORDER 						+" integer primary key autoincrement, " +
				GROUPMESSAGE_GROUPNAME		+" text, "+
				GROUPMESSAGE_USERNAME     	+" text, "+
				GROUPMESSAGE_CONTENT   		+" text, "+
				GROUPMESSAGE_TIME       	+" text, "+
				GROUPMESSAGE_ISREAD   		+" integer )";
		db.execSQL(sql);
		
	}
			
	/* 以下开始是操作 通知公告表 */
	/**
	 * 批量插入通知公告
	 */
	public void batchInsertNotice(Collection<NoticeDTO> newsDTOs) {
		Log.d(TAG, "METHOD BEGIN: batchInsertNotice()");
		try {
			db.beginTransaction();
			for (NoticeDTO newmessage : newsDTOs) {
				ContentValues cv = new ContentValues();
				cv.put(NOTICE_ID, newmessage.getId());
				cv.put(NOTICE_TITLE, newmessage.getTitle());
				cv.put(NOTICE_CONTENT, newmessage.getContent());
				cv.put(NOTICE_SOURCE, newmessage.getSource());
				cv.put(NOTICE_PUBLISHER, newmessage.getPublisher());
				cv.put(NOTICE_TIME, newmessage.getTime());
				cv.put(NOTICE_ISREAD, newmessage.getReadStatus());
				db.insert(NOTICE_TABLENAME, null, cv);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(TAG, "", e);
		} finally {
			db.endTransaction();
		}
	}
	/**
	 * 查询多条通知公告  
	 */
	public List<NoticeDTO> queryMultiNotice(int counts) {
		ArrayList<NoticeDTO> messages = new ArrayList<NoticeDTO>();
		Cursor cursor = db.query(NOTICE_TABLENAME, null, null, null, null, null,ORDER +" desc limit 0,"+counts);
		if (cursor.getCount() == 0) {
			cursor.close();
			return messages;
		}
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			NoticeDTO message = new NoticeDTO();
			message.setId(cursor.getInt(1));
			message.setTitle(cursor.getString(2));
			message.setContent(cursor.getString(3));
			message.setSource(cursor.getString(4));
			message.setPublisher(cursor.getString(5));
			message.setTime(cursor.getString(6));
			message.setRead(cursor.getInt(7));
			cursor.moveToNext();
			messages.add(message);
		}
		cursor.close();
		return messages;
	}
	/**
	 * 查询单条通知公告  
	 */
	public NoticeDTO queryOneNotice(int id) {
		NoticeDTO message = new NoticeDTO();
		String where = NOTICE_ID + " =? ";
		String[] whereValue = { id + "" };
		Cursor cursor = db.query(NOTICE_TABLENAME, null, where, whereValue, null, null,null);
		if (cursor.getCount() == 0) {
			cursor.close();
			return message;
		}
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			message.setId(cursor.getInt(1));
			message.setTitle(cursor.getString(2));
			message.setContent(cursor.getString(3));
			message.setSource(cursor.getString(4));
			message.setPublisher(cursor.getString(5));
			message.setTime(cursor.getString(6));
			message.setRead(cursor.getInt(7));
		}
		cursor.close();
		return message;
	}
	/**
	 * 更新通知公告为已读
	 */
	public int updataNotice(String id) {
		String where = NOTICE_ID + " =? ";
		String[] whereValue = { id + "" };
		ContentValues cv = new ContentValues();
		cv.put(NOTICE_ISREAD, "1");
		return db.update(NOTICE_TABLENAME, cv, where, whereValue);
	}
	/**
	 * 查询通知公告的最近一条消息创建的时间
	 */
	public String getLastTimeOfNotice(){
		String lastTime;
		String sql = "select "+NOTICE_TIME+" from "+NOTICE_TABLENAME+" order by "+ORDER+" desc limit 0,1";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			lastTime = "2016-01-01 00:00:00";
			return lastTime;
		}
		cursor.moveToFirst();
		lastTime = cursor.getString(0);
		cursor.close();
		return lastTime;
	}
	/**
	 * 获取通知公告中未读消息数目
	 * @return
	 */
	public int getUnReadNumOfNotice(){
		String sql = "select count(*) from "+NOTICE_TABLENAME+" where "+NOTICE_ISREAD+"=0 ";
		Cursor cursor = db.rawQuery(sql, null);
		return cursor.getCount();
	}
	
	/* 以下开始是操作 热点新闻表 */
	
	/**
	 * 批量插入新闻
	 */
	public void batchInsertHotNews(Collection<HotNewsDTO> newsDTOs) {
		Log.d(TAG, "METHOD BEGIN: batchInsertHotNews()");
		try {
			db.beginTransaction();
			for (HotNewsDTO newmessage : newsDTOs) {
				ContentValues cv = new ContentValues();
				cv.put(HOTNEWS_ID, newmessage.getId());
				cv.put(HOTNEWS_TITLE, newmessage.getTitle());
				cv.put(HOTNEWS_CONTENT, newmessage.getContent());
				cv.put(HOTNEWS_SOURCE, newmessage.getSource());
				cv.put(HOTNEWS_TIME, newmessage.getPubTime());
				cv.put(HOTNEWS_LASTTIME, newmessage.getLastTime());
				cv.put(HOTNEWS_ISREAD, newmessage.getReadStatus());
				db.insert(HOTNEWS_TABLENAME, null, cv);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(TAG, "", e);
		} finally {
			db.endTransaction();
		}
	}
	/**
	 * 查询多条新闻  
	 */
	public List<HotNewsDTO> queryMultiHotNews(int counts) {
		ArrayList<HotNewsDTO> messages = new ArrayList<HotNewsDTO>();
		Cursor cursor = db.query(HOTNEWS_TABLENAME, null, null, null, null, null,ORDER +" desc limit 0,"+counts);
		if (cursor.getCount() == 0) {
			cursor.close();
			return messages;
		}
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			HotNewsDTO message = new HotNewsDTO();
			message.setId(cursor.getInt(1));
			message.setTitle(cursor.getString(2));
			message.setContent(cursor.getString(3));
			message.setSource(cursor.getString(4));
			message.setPubTime(cursor.getString(5));
			message.setLastTime(cursor.getString(6));
			message.setRead(cursor.getInt(7));
			cursor.moveToNext();
			messages.add(message);
		}
		cursor.close();
		return messages;
	}
	/**
	 * 查询单条新闻  
	 */
	public HotNewsDTO queryOneHotNews(int id) {
		HotNewsDTO message = new HotNewsDTO();
		String where = HOTNEWS_ID + " =? ";
		String[] whereValue = { id + "" };
		Cursor cursor = db.query(HOTNEWS_TABLENAME, null, where, whereValue, null, null,null);
		if (cursor.getCount() == 0) {
			cursor.close();
			return message;
		}
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			message.setId(cursor.getInt(1));
			message.setTitle(cursor.getString(2));
			message.setContent(cursor.getString(3));
			message.setSource(cursor.getString(4));
			message.setPubTime(cursor.getString(5));
			message.setLastTime(cursor.getString(6));
			message.setRead(cursor.getInt(7));
		}
		cursor.close();
		return message;
	}
	/**
	 * 更新热点新闻为已读
	 */
	public int updataHotNews(String id) {
		String where = HOTNEWS_ID + " =? ";
		String[] whereValue = { id + "" };
		ContentValues cv = new ContentValues();
		cv.put(HOTNEWS_ISREAD, "1");
		return db.update(HOTNEWS_TABLENAME, cv, where, whereValue);
	}
	/**
	 * 查询热点新闻的最近一条消息创建的时间
	 */
	public String getLastTimeOfHotNews(){
		String lastTime;
		String sql = "select "+HOTNEWS_LASTTIME+" from "+HOTNEWS_TABLENAME+" order by "+ORDER+" desc limit 0,1";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			lastTime = "2016-01-01 00:00:00";
			return lastTime;
		}
		cursor.moveToFirst();
		lastTime = cursor.getString(0);
		cursor.close();
		return lastTime;
	}
	/**
	 * 获取热点新闻中未读消息数目
	 * @return
	 */
	public int getUnReadNumOfHotNews(){
		String sql = "select count(*) from "+HOTNEWS_TABLENAME+" where "+HOTNEWS_ISREAD+"=0 ";
		Cursor cursor = db.rawQuery(sql, null);
		return cursor.getCount();
	}

	/* 以下开始是操作 群消息表 */
	
	/**
	 * 批量插入群消息
	 */
	public void batchInsertGroupNews(Collection<GroupNewsDTO> newsDTOs) {
		Log.d(TAG, "METHOD BEGIN: batchInsertGroupNews()");
		try {
			db.beginTransaction();
			for (GroupNewsDTO newmessage : newsDTOs) {
				ContentValues cv = new ContentValues();
				cv.put(GROUPMESSAGE_GROUPNAME, newmessage.getGroupName());
				cv.put(GROUPMESSAGE_USERNAME, newmessage.getUserName());
				cv.put(GROUPMESSAGE_CONTENT, newmessage.getContent());
				cv.put(GROUPMESSAGE_TIME, newmessage.getTime());
				cv.put(GROUPMESSAGE_ISREAD, newmessage.getReadStatus());
				db.insert(GROUPMESSAGE_TABLENAME, null, cv);
				
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(TAG, "", e);
		} finally {
			db.endTransaction();
		}
	}
	/**
	 * 查询多条群消息  
	 */
	public List<GroupNewsDTO> queryMultiGroupNews(int counts) {
		ArrayList<GroupNewsDTO> messages = new ArrayList<GroupNewsDTO>();
		Cursor cursor = db.query(GROUPMESSAGE_TABLENAME, null, null, null, null, null,ORDER +" desc limit 0,"+counts);
		if (cursor.getCount() == 0) {
			cursor.close();
			return messages;
		}
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GroupNewsDTO message = new GroupNewsDTO();
			message.setId(cursor.getInt(1));
			message.setGroupName(cursor.getString(2));
			message.setUserName(cursor.getString(3));
			message.setContent(cursor.getString(4));
			message.setTime(cursor.getString(5));
			message.setRead(cursor.getInt(6));
			cursor.moveToNext();
			messages.add(message);
		}
		cursor.close();
		return messages;
	}
	/**
	 * 更新群消息为已读
	 */
	public int updataGroupNews(String id) {
		String where = ORDER + " =? ";
		String[] whereValue = { id + "" };
		ContentValues cv = new ContentValues();
		cv.put(GROUPMESSAGE_ISREAD, "1");
		return db.update(GROUPMESSAGE_TABLENAME, cv, where, whereValue);
	}
	/**
	 * 查询群消息的最近一条消息创建的时间
	 */
	public String getLastTimeOfGroupNews(){
		String lastTime;
		String sql = "select "+GROUPMESSAGE_TIME+" from "+GROUPMESSAGE_TABLENAME+" order by "+ORDER+" desc limit 0,1";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			lastTime = "2016-01-01 00:00:00";
			return lastTime;
		}
		cursor.moveToFirst();
		lastTime = cursor.getString(0);
		cursor.close();
		return lastTime;
	}
	/**
	 * 获取群消息中未读消息数目
	 * @return
	 */
	public int getUnReadNumOfGroupNews(){
		String sql = "select count(*) from "+GROUPMESSAGE_TABLENAME+" where "+GROUPMESSAGE_ISREAD+"=0 ";
		Cursor cursor = db.rawQuery(sql, null);
		return cursor.getCount();
	}

}
