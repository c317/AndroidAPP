package com.gasinforapp.datebase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private static final String NOTICE_TABLENAME = "notice";
	private static final String NOTICE_ID = "id";
	private static final String NOTICE_TITLE = "title";
	private static final String NOTICE_CONTENT = "content";
	private static final String NOTICE_SOURCE = "source";
	private static final String NOTICE_PUBLISHER = "publisher";
	private static final String NOTICE_TIME = "time";
	private static final String NOTICE_ISREAD = "isread";

	/**
	 * 热点新闻表及字段
	 */
	private static final String HOTNEWS_TABLENAME = "hotnews";
	private static final String HOTNEWS_ID = "id";
	private static final String HOTNEWS_TITLE = "title";
	private static final String HOTNEWS_CONTENT = "content";
	private static final String HOTNEWS_SOURCE = "source";
	private static final String HOTNEWS_TIME = "time";
	private static final String HOTNEWS_LASTTIME = "lastTime";
	private static final String HOTNEWS_ISREAD = "isread";

	/**
	 * 群消息表及字段
	 */
	private static final String GROUPMESSAGE_TABLENAME = "groupmessage";
	private static final String GROUPMESSAGE_GROUPID = "groupId";
	private static final String GROUPMESSAGE_USERNAME = "userName";
	private static final String GROUPMESSAGE_CONTENT = "content";
	private static final String GROUPMESSAGE_TIME = "time";
	private static final String GROUPMESSAGE_KIND = "kind";
	private static final String GROUPMESSAGE_ISREAD = "isread";

	private GasInforDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, new LoggingCursorFactory(),
				DATABASE_VIRSON);
		db = getWritableDatabase();// 如果数据库不存在则会调用onCreate函数
	}

	public int getDBVersion() {
		return db.getVersion();
	}

	public void setDBVersion(int version) {
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

	public void createTables(SQLiteDatabase db) {
		/**
		 * 通知公告表
		 */
		String sql = "create table " + NOTICE_TABLENAME + " ( " + ORDER
				+ " integer primary key autoincrement, " + NOTICE_ID
				+ " integer, " + NOTICE_TITLE + " text, " + NOTICE_CONTENT
				+ " text, " + NOTICE_SOURCE + " text, " + NOTICE_PUBLISHER
				+ " text, " + NOTICE_TIME + " text, " + NOTICE_ISREAD
				+ " integer)";
		db.execSQL(sql);
		/**
		 * 热点新闻表
		 */
		sql = "create table " + HOTNEWS_TABLENAME + " ( " + ORDER
				+ " integer primary key autoincrement, " + HOTNEWS_ID
				+ " integer unique, " + HOTNEWS_TITLE + " text, "
				+ HOTNEWS_CONTENT + " text, " + HOTNEWS_SOURCE + " text, "
				+ HOTNEWS_TIME + " text, " + HOTNEWS_LASTTIME + " text, "
				+ HOTNEWS_ISREAD + " integer )";
		db.execSQL(sql);
		/**
		 * 群消息表
		 */
		sql = "create table " + GROUPMESSAGE_TABLENAME + " ( " + ORDER
				+ " integer primary key autoincrement, " + GROUPMESSAGE_GROUPID
				+ " text, " + GROUPMESSAGE_USERNAME + " text, "
				+ GROUPMESSAGE_CONTENT + " text, " + GROUPMESSAGE_TIME
				+ " text, " + GROUPMESSAGE_KIND + " integer, "
				+ GROUPMESSAGE_ISREAD + " integer )";
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
		Cursor cursor = db.query(NOTICE_TABLENAME, null, null, null, null,
				null, ORDER + " desc limit 0," + counts);
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
		Cursor cursor = db.query(NOTICE_TABLENAME, null, where, whereValue,
				null, null, null);
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
	public String getLastTimeOfNotice() {
		String lastTime;
		String sql = "select " + NOTICE_TIME + " from " + NOTICE_TABLENAME
				+ " order by " + ORDER + " desc limit 0,1";
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
	 * 
	 * @return
	 */
	public int getUnReadNumOfNotice() {
		String sql = "select count(*) from " + NOTICE_TABLENAME + " where "
				+ NOTICE_ISREAD + "=0 ";
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
		Cursor cursor = db.query(HOTNEWS_TABLENAME, null, null, null, null,
				null, ORDER + " desc limit 0," + counts);
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
		Cursor cursor = db.query(HOTNEWS_TABLENAME, null, where, whereValue,
				null, null, null);
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
	public String getLastTimeOfHotNews() {
		String lastTime;
		String sql = "select " + HOTNEWS_LASTTIME + " from "
				+ HOTNEWS_TABLENAME + " order by " + ORDER + " desc limit 0,1";
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
	 * 
	 * @return
	 */
	public int getUnReadNumOfHotNews() {
		String sql = "select count(*) from " + HOTNEWS_TABLENAME + " where "
				+ HOTNEWS_ISREAD + "=0 ";
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
				cv.put(GROUPMESSAGE_GROUPID, newmessage.getGroupId());
				cv.put(GROUPMESSAGE_USERNAME, newmessage.getUserName());
				cv.put(GROUPMESSAGE_CONTENT, newmessage.getContent());
				cv.put(GROUPMESSAGE_TIME, newmessage.getTime());
				cv.put(GROUPMESSAGE_KIND, newmessage.getKind());
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
	 * 查询多条已读群消息
	 */
	public List<GroupNewsDTO> queryMultiGroupNewsRead(int groupId,int counts) {
		ArrayList<GroupNewsDTO> messages = new ArrayList<GroupNewsDTO>();
		String where = GROUPMESSAGE_GROUPID + " =? and "+ GROUPMESSAGE_ISREAD + " =?";
		String[] whereValue = { groupId+"","1" };
		Cursor cursor = db.query(GROUPMESSAGE_TABLENAME, null, where, whereValue,null, null, ORDER + " ASC limit 0," + counts);
		if (cursor.getCount() == 0) {
			cursor.close();
			return messages;
		}
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GroupNewsDTO message = new GroupNewsDTO();
			message.setId(cursor.getInt(0));
			message.setGroupId(cursor.getInt(1));
			message.setUserName(cursor.getString(2));
			message.setContent(cursor.getString(3));
			message.setTime(cursor.getString(4));
			message.setKind(cursor.getInt(5));
			message.setRead(cursor.getInt(6));
			cursor.moveToNext();
			messages.add(message);
		}
		cursor.close();
		return messages;
	}
	/**
	 * 查询所有未读群消息
	 */
	public List<GroupNewsDTO> queryMultiGroupNewsUnRead(int groupId) {
		ArrayList<GroupNewsDTO> messages = new ArrayList<GroupNewsDTO>();
		String where = GROUPMESSAGE_GROUPID + " =? and "+ GROUPMESSAGE_ISREAD + " =?";
		String[] whereValue = { groupId+"", "0" };
		Cursor cursor = db.query(GROUPMESSAGE_TABLENAME, null, where, whereValue,
				null, null, ORDER + " ASC");
		if (cursor.getCount() == 0) {
			cursor.close();
			return messages;
		}
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GroupNewsDTO message = new GroupNewsDTO();
			message.setId(cursor.getInt(0));
			message.setGroupId(cursor.getInt(1));
			message.setUserName(cursor.getString(2));
			message.setContent(cursor.getString(3));
			message.setTime(cursor.getString(4));
			message.setKind(cursor.getInt(5));
			message.setRead(cursor.getInt(6));
			cursor.moveToNext();
			messages.add(message);
		}
		cursor.close();
		return messages;
	}

	/**
	 * 更新群消息为已读,不论哪个群，ORDER可以唯一标识
	 */
	public int updataGroupNews(int[] id) {
		String ids = id[0] + "";
		String where = ORDER + " in (?";
		for (int i = 1; i < id.length; i++) {
			where = where + ",?";
			ids = ids + "," + id[i];
		}
		where = where + ")";
		String[] whereValue = { ids };
		ContentValues cv = new ContentValues();
		cv.put(GROUPMESSAGE_ISREAD, "1");
		return db.update(GROUPMESSAGE_TABLENAME, cv, where, whereValue);
	}
	
	/**
	 * 更新群所有未读消息为已读
	 */
	public int updataGroupNews(int groupId) {
		String where = GROUPMESSAGE_GROUPID + " =? and "+GROUPMESSAGE_ISREAD+"=0";
		String[] whereValue = { groupId + "" };
		ContentValues cv = new ContentValues();
		cv.put(GROUPMESSAGE_ISREAD, "1");
		return db.update(GROUPMESSAGE_TABLENAME, cv, where, whereValue);
	}

	/**
	 * 查询各个群消息的最近一条消息创建的时间
	 * 注意：因查询语句过于复杂，为了方便阅读，此处没有使用表和字段的常量代替
	 */
	public String getLastTimeOfGroupNews(int[] groupIds) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		String lastTimes="";
		String sql = "select groupmessage.groupId,groupmessage.time from groupmessage,(select groupId,max(_id)_id from groupmessage group by groupId)temp where groupmessage._id = temp._id";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				map.put(cursor.getInt(0), cursor.getString(1));
				cursor.moveToNext();
			}
			cursor.close();
		}
		for(int i=0;i<groupIds.length;i++){
			if(map.get(groupIds[i])==null){
				lastTimes = lastTimes +"2016-01-01 00:00:00,";
			}else{
				lastTimes = lastTimes +map.get(groupIds[i])+",";
			}
		}
		lastTimes = lastTimes.substring(0, lastTimes.length()-1);
		return lastTimes;
	}
	/**
	 * 获取所有群的未读消息数目
	 * 
	 * @return
	 */
	public Map<Integer, Integer> getUnReadNumOfGroupNews(int[] groupIds) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		String sql = "select groupId,count(*) from " + GROUPMESSAGE_TABLENAME
				+"where " + GROUPMESSAGE_ISREAD + "=0  group by groupId ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				map.put(cursor.getInt(0), cursor.getInt(1));
				cursor.moveToNext();
			}
			cursor.close();
		}
		for(int i=0;i<groupIds.length;i++){
			if(map.get(groupIds[i])==null){
				map.put(groupIds[i], 0);
			}
		}
		return map;
	}

}
