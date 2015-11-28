package com.daemin.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.daemin.data.SubjectData;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "/sdcard/.TimeDAO/";

	// SubjectDatas table name
	private static final String TABLE_SCHEDULE = "schedule";//subject로 바꾸자
	List<SubjectData> subjectDataList = new ArrayList<>();
	List<String> stringList = new ArrayList<>();
	public DatabaseHandler(Context context) {
		//super(context, DATABASE_NAME+ User.USER.getEngUnivName()+".sqlite", null, DATABASE_VERSION);
		super(context, DATABASE_NAME+"subject.sqlite", null, DATABASE_VERSION);
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
		// Select All Query
		subjectDataList.clear();
		String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE;

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SubjectData subjectData = new SubjectData();
				subjectData.set_id(cursor.getInt(0));
				subjectData.setSubnum(cursor.getString(1));
				subjectData.setSubtitle(cursor.getString(2));
				subjectData.setCredit(cursor.getString(3));
				subjectData.setClassnum(cursor.getString(4));
				subjectData.setLimitnum(cursor.getString(5));
				subjectData.setDep(cursor.getString(6));
				subjectData.setDep_grade(cursor.getString(7));
				subjectData.setDep_detail(cursor.getString(8));
				subjectData.setTime(cursor.getString(9));
				subjectData.setProf(cursor.getString(10));
				subjectDataList.add(subjectData);
			} while (cursor.moveToNext());
		}

		// return SubjectData list
		return subjectDataList;
	}
	public SubjectData getSubjectData(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE + " WHERE _id='"+id+"'";
		Cursor cursor = db.rawQuery(selectQuery, null);
		SubjectData subjectData = new SubjectData();
		if (cursor.moveToFirst()) {
			do {
				subjectData.set_id(cursor.getInt(0));
				subjectData.setSubnum(cursor.getString(1));
				subjectData.setSubtitle(cursor.getString(2));
				subjectData.setCredit(cursor.getString(3));
				subjectData.setClassnum(cursor.getString(4));
				subjectData.setLimitnum(cursor.getString(5));
				subjectData.setDep(cursor.getString(6));
				subjectData.setDep_grade(cursor.getString(7));
				subjectData.setDep_detail(cursor.getString(8));
				subjectData.setTime(cursor.getString(9));
				subjectData.setProf(cursor.getString(10));
			} while (cursor.moveToNext());
		}
		return subjectData;
	}
	public List<SubjectData> getAllWithDepAndGrade(String depName,String depgrade) {
		SQLiteDatabase db = this.getWritableDatabase();
		subjectDataList.clear();
		// Select All Query
		String selectQuery;
		if(depName.equals("")||depName.equals("전체")) {
			if(depgrade.equals("0")) {
				selectQuery = "SELECT DISTINCT * FROM " + TABLE_SCHEDULE;
			}else{
				selectQuery = "SELECT DISTINCT * FROM " + TABLE_SCHEDULE + " WHERE dep_grade LIKE '%"+depgrade+"%'";
			}
		}else{
			if(depgrade.equals("0")) {
				selectQuery = "SELECT DISTINCT * FROM " + TABLE_SCHEDULE + " WHERE dep LIKE '%" + depName + "%'";
			}else{
				selectQuery = "SELECT DISTINCT * FROM " + TABLE_SCHEDULE + " WHERE (dep LIKE '%"+depName+"%' and dep_grade LIKE '%"+depgrade+"%')";
			}
		}
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
				SubjectData.setDep(cursor.getString(6));
				SubjectData.setDep_grade(cursor.getString(7));
				SubjectData.setDep_detail(cursor.getString(8));
				SubjectData.setTime(cursor.getString(9));
				SubjectData.setProf(cursor.getString(10));
				subjectDataList.add(SubjectData);
			} while (cursor.moveToNext());
		}

		return subjectDataList;
	}
	public List<String> getDepList() {
		SQLiteDatabase db = this.getWritableDatabase();
		stringList.clear();
		stringList.add("전체");
		String selectSub = "SELECT DISTINCT dep FROM " + TABLE_SCHEDULE;
		Cursor subCursor = db.rawQuery(selectSub, null);
		if (subCursor.moveToFirst()) {
			do {
				stringList.add(subCursor.getString(0));
			} while (subCursor.moveToNext());
		}
		return stringList;
	}
	public List<String> getSubOrProfList() {
		SQLiteDatabase db = this.getWritableDatabase();
		stringList.clear();
		String selectSub = "SELECT DISTINCT subtitle FROM " + TABLE_SCHEDULE;
		Cursor subCursor = db.rawQuery(selectSub, null);
		if (subCursor.moveToFirst()) {
			do {
				stringList.add(subCursor.getString(0));
			} while (subCursor.moveToNext());
		}
		String selectProf = "SELECT DISTINCT prof FROM " + TABLE_SCHEDULE;
		Cursor profCursor = db.rawQuery(selectProf, null);
		if (profCursor.moveToFirst()) {
			do {
				stringList.add(profCursor.getString(0));
			} while (profCursor.moveToNext());
		}
		return stringList;
	}
	public List<SubjectData> getAllWithSubOrProf(String subOrProf) {
		SQLiteDatabase db = this.getWritableDatabase();
		subjectDataList.clear();
		// Select All Query
		String selectQuery = "SELECT DISTINCT * FROM " + TABLE_SCHEDULE + " WHERE (subtitle LIKE '%"+subOrProf+"%' or prof LIKE '%"+subOrProf+"%')";
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
				SubjectData.setDep(cursor.getString(6));
				SubjectData.setDep_grade(cursor.getString(7));
				SubjectData.setDep_detail(cursor.getString(8));
				SubjectData.setTime(cursor.getString(9));
				SubjectData.setProf(cursor.getString(10));
				Log.i("test", SubjectData.getSubtitle());
				subjectDataList.add(SubjectData);
			} while (cursor.moveToNext());
		}


		// return SubjectData list
		return subjectDataList;
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
