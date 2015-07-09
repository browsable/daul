package com.daemin.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.daemin.data.SubjectData;
import com.daemin.enumclass.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "/sdcard/.TimeDAO/";

	// SubjectDatas table name
	private static final String TABLE_SCHEDULE = "schedule";

	// SubjectDatas Table Columns names
	private static final String KEY_ID = "_id";
	private String KEY_SUBNUM = "subnum";
	private String KEY_SUBTITLE = "subtitle";
	private String KEY_PROF = "prof";
	private String KEY_CREDIT = "credit";
	private String KEY_CLASSNUM ="classnum";
	private String KEY_LIMITNUM = "limitnum"; //제한수
	private String KEY_DEP = "";
	private String KEY_DEP_DETAIL = ""; //이수형태
	private String KEY_DEP_GRADE = "dep_grade"; //대상학년
	SQLiteDatabase db;
	private String filePath;
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME+ User.USER.getEngUnivName()+".sqlite", null, DATABASE_VERSION);
	}
	

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	// Getting All SubjectDatas
	public List<SubjectData> getAllSubjectDatas() {
		SQLiteDatabase db = this.getWritableDatabase();
		List<SubjectData> SubjectDataList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE;

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SubjectData SubjectData = new SubjectData();
				SubjectData.set_id(cursor.getInt(0));
				SubjectData.setSubnum(cursor.getString(1));
				SubjectData.setSubtitle(cursor.getString(2));
				SubjectData.setCredit(cursor.getString(3));
				SubjectData.setClassnum(cursor.getString(4));
				SubjectData.setLimitnum(cursor.getString(5));
				SubjectData.setDep_grade(cursor.getString(7));
				SubjectData.setDep_detail(cursor.getString(8));
				SubjectData.setTime(cursor.getString(30));
				SubjectData.setProf(cursor.getString(31));
				SubjectDataList.add(SubjectData);
			} while (cursor.moveToNext());
		}

		// return SubjectData list
		return SubjectDataList;
	}
	List<SubjectData> SubjectDataList = new ArrayList<>();
	public List<SubjectData> getRecommendSubjectDatas(String time) {
		SQLiteDatabase db = this.getWritableDatabase();
		SubjectDataList.clear();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE + " WHERE time LIKE '%"+time+"%'";
		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SubjectData SubjectData = new SubjectData();
				SubjectData.set_id(cursor.getInt(0));
				SubjectData.setSubnum(cursor.getString(1));
				SubjectData.setSubtitle(cursor.getString(2));
				SubjectData.setCredit(cursor.getString(3));
				SubjectData.setClassnum(cursor.getString(4));
				SubjectData.setLimitnum(cursor.getString(5));
				SubjectData.setDep_grade(cursor.getString(7));
				SubjectData.setDep_detail(cursor.getString(8));
				SubjectData.setTime(cursor.getString(30));
				SubjectData.setProf(cursor.getString(31));
				SubjectDataList.add(SubjectData);
			} while (cursor.moveToNext());
		}

		// return SubjectData list
		return SubjectDataList;
	}

/*
	// Adding new SubjectData
	public void addSubjectData(SubjectData SubjectData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SHARE, SubjectData.getShare()); 
		values.put(KEY_XYTH, SubjectData.getXyth()); 
		values.put(KEY_PLACE, SubjectData.getPlace()); 
		values.put(KEY_CONTENT, SubjectData.getContent()); 
		values.put(KEY_SUBCONTENT, SubjectData.getSubcontent()); 
		values.put(KEY_CONTENTCODE, SubjectData.getContentcode()); 
		values.put(KEY_DAYCODE, SubjectData.getDaycode()); 

		// Inserting Row
		db.insert(TABLE_SCHEDULE, null, values);
		db.close(); // Closing database connection
	}*/

	/*// Getting single SubjectData
	SubjectData getSubjectData() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SCHEDULE, new String[] { KEY_ID,
						KEY_SUBTITLE, KEY_PROF, KEY_CREDIT, KEY_CLASSNUM, KEY_LIMITNUM, KEY_DEP_DETAIL, KEY_DEP_GRADE}, KEY_XYTH + "=?",
				new String[] { String.valueOf(xyth) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		SubjectData SubjectData = new SubjectData(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)),
				cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5),
				cursor.getString(6), cursor.getString(7));
		// return SubjectData
		return SubjectData;
	}*/
	


	/*// Updating single SubjectData
	public int updateSubjectData(SubjectData SubjectData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SHARE, SubjectData.getShare()); 
		values.put(KEY_XYTH, SubjectData.getXyth()); 
		values.put(KEY_PLACE, SubjectData.getPlace()); 
		values.put(KEY_CONTENT, SubjectData.getContent()); 
		values.put(KEY_SUBCONTENT, SubjectData.getSubcontent()); 
		values.put(KEY_CONTENTCODE, SubjectData.getContentcode()); 
		values.put(KEY_DAYCODE, SubjectData.getDaycode()); 

		// updating row
		return db.update(TABLE_SCHEDULE, values, KEY_XYTH + " = ?",
				new String[] { String.valueOf(SubjectData.getXyth()) });
	}

	// Deleting single SubjectData
	public void deleteSubjectData(SubjectData SubjectData) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SCHEDULE, KEY_XYTH + " = ?",
				new String[] { String.valueOf(SubjectData.getXyth()) });
		db.close();
	}


	// Getting SubjectDatas Count
	public int getSubjectDatasCount() {
		String countQuery = "SELECT  * FROM " + TABLE_SCHEDULE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}*/

}
