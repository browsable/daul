package com.daemin.timetable.common;

import java.util.ArrayList;
import java.util.List;

import com.daemin.schooltimetable.*;
import com.daemin.timetable.model.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "TimeEntitysManager";

	// TimeEntitys table name
	private static final String TABLE_TIMEENTITYS = "TimeEntitys";

	// TimeEntitys Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_SHARE = "share"; 
	private static final String KEY_XYTH = "xyth"; 
	private static final String KEY_PLACE = "place"; 
	private static final String KEY_CONTENT = "content"; 
	private static final String KEY_SUBCONTENT = "subcontent"; 
	private static final String KEY_CONTENTCODE = "contentcode";
	private static final String KEY_DAYCODE = "daycode"; 
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TimeEntityS_TABLE = "CREATE TABLE " + TABLE_TIMEENTITYS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_SHARE + " INTEGER NOT NULL,"
				+ KEY_XYTH + " VARCHAR(10) NOT NULL," + KEY_PLACE + " VARCHAR(10) NOT NULL,"
				+ KEY_CONTENT + " TEXT NOT NULL," + KEY_SUBCONTENT + " TEXT NOT NULL,"
				+ KEY_CONTENTCODE + " VARCHAR(10) NOT NULL," + KEY_DAYCODE + " VARCHAR(10) NOT NULL" 
				+ ")";
		db.execSQL(CREATE_TimeEntityS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMEENTITYS);

		// Create tables again
		onCreate(db);
	}

	// Adding new TimeEntity
	public void addTimeEntity(TimeEntity TimeEntity) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SHARE, TimeEntity.getShare()); 
		values.put(KEY_XYTH, TimeEntity.getXyth()); 
		values.put(KEY_PLACE, TimeEntity.getPlace()); 
		values.put(KEY_CONTENT, TimeEntity.getContent()); 
		values.put(KEY_SUBCONTENT, TimeEntity.getSubcontent()); 
		values.put(KEY_CONTENTCODE, TimeEntity.getContentcode()); 
		values.put(KEY_DAYCODE, TimeEntity.getDaycode()); 

		// Inserting Row
		db.insert(TABLE_TIMEENTITYS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single TimeEntity
	TimeEntity getTimeEntity(String xyth) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TIMEENTITYS, new String[] { KEY_ID,
				KEY_SHARE, KEY_XYTH, KEY_PLACE, KEY_CONTENT, KEY_SUBCONTENT, KEY_CONTENTCODE, KEY_DAYCODE}, KEY_XYTH + "=?",
				new String[] { String.valueOf(xyth) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		TimeEntity TimeEntity = new TimeEntity(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)),
				cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5),
				cursor.getString(6), cursor.getString(7));
		// return TimeEntity
		return TimeEntity;
	}
	
	// Getting All TimeEntitys
	public List<TimeEntity> getAllTimeEntitys() {
		List<TimeEntity> TimeEntityList = new ArrayList<TimeEntity>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TIMEENTITYS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TimeEntity TimeEntity = new TimeEntity();
				TimeEntity.setid(Integer.parseInt(cursor.getString(0)));
				TimeEntity.setShare(Integer.parseInt(cursor.getString(1)));
				TimeEntity.setXyth(cursor.getString(2));
				TimeEntity.setPlace(cursor.getString(3));
				TimeEntity.setContent(cursor.getString(4));
				TimeEntity.setSubcontent(cursor.getString(5));
				TimeEntity.setContentcode(cursor.getString(6));
				TimeEntity.setDaycode(cursor.getString(7));
				// Adding TimeEntity to list
				TimeEntityList.add(TimeEntity);
			} while (cursor.moveToNext());
		}

		// return TimeEntity list
		return TimeEntityList;
	}

	// Updating single TimeEntity
	public int updateTimeEntity(TimeEntity TimeEntity) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SHARE, TimeEntity.getShare()); 
		values.put(KEY_XYTH, TimeEntity.getXyth()); 
		values.put(KEY_PLACE, TimeEntity.getPlace()); 
		values.put(KEY_CONTENT, TimeEntity.getContent()); 
		values.put(KEY_SUBCONTENT, TimeEntity.getSubcontent()); 
		values.put(KEY_CONTENTCODE, TimeEntity.getContentcode()); 
		values.put(KEY_DAYCODE, TimeEntity.getDaycode()); 

		// updating row
		return db.update(TABLE_TIMEENTITYS, values, KEY_XYTH + " = ?",
				new String[] { String.valueOf(TimeEntity.getXyth()) });
	}

	// Deleting single TimeEntity
	public void deleteTimeEntity(TimeEntity TimeEntity) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TIMEENTITYS, KEY_XYTH + " = ?",
				new String[] { String.valueOf(TimeEntity.getXyth()) });
		db.close();
	}


	// Getting TimeEntitys Count
	public int getTimeEntitysCount() {
		String countQuery = "SELECT  * FROM " + TABLE_TIMEENTITYS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
