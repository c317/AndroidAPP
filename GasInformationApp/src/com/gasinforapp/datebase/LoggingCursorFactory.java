package com.gasinforapp.datebase;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

public class LoggingCursorFactory implements CursorFactory {
	@Override
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
        Log.v("SQL", query.toString());
        return new SQLiteCursor(db, masterQuery, editTable, query);
    }

}
