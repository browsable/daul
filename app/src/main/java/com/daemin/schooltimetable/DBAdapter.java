package com.daemin.schooltimetable;

import java.util.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract.Constants;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

   /////////////////////////////////////////////////////////////////////
   //   Constants & Data
   /////////////////////////////////////////////////////////////////////
   // For logging:
   private static final String TAG = "DBAdapter";
   
   // DB Fields
   public static final String KEY_ROWID = "_id";
   public static final int COL_ROWID = 0;
   /*
    * CHANGE 1:
    */
   // TODO: Setup your fields here:
 //  public static final String KEY_SUBTITLE = "subtitle";
  // public static final String KEY_DEP = "dep";
   //public static final String KEY_TIME1 = "time1";
   
   // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
   //public static final int COL_SUBTITLE = 1;
   //public static final int COL_DEP = 2;

   
 //  public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_SUBTITLE, KEY_DEP};
  //public static final String[] TIME_KEYS = new String[] {KEY_TIME1, KEY_SUBTITLE};
   
   // DB info: it's name, and the table we are using (just one).
   
   public static final String DATABASE_NAME = "/sdcard/koreatech.sqlite";
   public static final String DATABASE_TABLE = "schedule";
   // Track DB version if a new version of your app changes the format.
   public static final int DATABASE_VERSION = 1;    
   
   
   // Context of application who uses us.
   private final Context context;
   
   private DatabaseHelper myDBHelper;
   private SQLiteDatabase db;

   /////////////////////////////////////////////////////////////////////
   //   Public methods:
   /////////////////////////////////////////////////////////////////////
   
   public DBAdapter(Context ctx) {
      this.context = ctx;
      myDBHelper = new DatabaseHelper(context);
   }
   
   // Open the database connection.
   public DBAdapter open() {
      db = myDBHelper.getWritableDatabase();
      return this;
   }
   
   // Close the database connection.
   public void close() {
      myDBHelper.close();
   }	
   

   /*
   // Delete a row from the database, by rowId (primary key)
   public boolean deleteRow(long rowId) {
      String where = KEY_ROWID + "=" + rowId;
      return db.delete(DATABASE_TABLE, where, null) != 0;
   }
   
   
   
   // Return all data in the database.
   public Cursor getAllRows() {
      String where = null;
      Cursor c =    db.query(true, DATABASE_TABLE, ALL_KEYS, 
                     where, null, null, null, null, null);
      if (c != null) {
         c.moveToFirst();
      }
      return c;
   }

   // Get a specific row (by rowId)
   public Cursor getRow(long rowId) {
      String where = KEY_ROWID + "=" + rowId;
      Cursor c =    db.query(true, DATABASE_TABLE, ALL_KEYS, 
                  where, null, null, null, null, null);
      if (c != null) {
         c.moveToFirst();
      }
      return c;
   }
   public Cursor getDep(String d_name) {
	      String where = KEY_DEP + "=" + d_name;
	      Cursor c =    db.query(true, DATABASE_TABLE, ALL_KEYS, 
	                  where, null, null, null, null, null);
	      if (c != null) {
	         c.moveToFirst();
	      }
	      return c;
	   }
	*/
   public Cursor getDepName(String detaildep, String kordepname) {
	      
	   
	   
	      Cursor c = db.rawQuery("SELECT _id, subtitle,prof,credit,classnum,limitnum,"+detaildep+",dep_grade FROM schedule WHERE dep_grade LIKE '%"+kordepname+"%' or dep_grade LIKE ' 전체%'", null);
	    
	      
	      if (c != null) {
		         c.moveToFirst();
		      }
	      return c;
	   }
   
   public Cursor getAll(String detaildep) {
	   
	      Cursor c = db.rawQuery("SELECT _id, subtitle,prof,credit,classnum,limitnum,"+detaildep+",dep_grade FROM schedule", null);
	      if (c != null) {
		         c.moveToFirst();
		      }
	      
	      return c;
	   }
   
   public Cursor getSubtitle(String kordepname){
   
	
   	Cursor c = db.rawQuery("SELECT _id, subtitle FROM schedule WHERE dep_grade LIKE '%"+kordepname+"%' or dep_grade LIKE ' 전체%'", null);
    if (c != null) {
        c.moveToFirst();
     }
 
    return c;
   	

   }
   
   //학부검색 이후 과목검색시검색한 과목만 리스트에 뿌림
   public Cursor SearchSubtitle(String detailsubtitle, String detaildep){
	   
		
	   	Cursor c = db.rawQuery("SELECT _id, subtitle,prof,credit,classnum,limitnum,"+detaildep+",dep_grade FROM schedule WHERE subtitle LIKE '%"+detailsubtitle+"%'", null);
	    if (c != null) {
	        c.moveToFirst();
	     }
	 
	    return c;
	   	

	   }
   
   //학년별검색
   
   public Cursor SearchDepgrade(String depgradenum, String detaildep, String kordepname){
	   
		
	   	Cursor c = db.rawQuery("SELECT _id, subtitle,prof,credit,classnum,limitnum,"+detaildep+",dep_grade FROM schedule where (dep_grade LIKE '%"+depgradenum+"%' and dep_grade LIKE '%"+kordepname+"%') or dep_grade LIKE ' 전체%'", null);
	    
	   	//Cursor c = db.rawQuery("SELECT _id, subtitle,prof,credit,classnum,limitnum,"+detaildep+",dep_grade FROM schedule where dep_grade LIKE '%"+depgradenum+" %' or dep_grade LIKE ' 전체%'", null);
	    
	   	if (c != null) {
	        c.moveToFirst();
	     }
	 
	    return c;
	   	

	   }
   
   public Cursor SearchTime(String _id){
	   
		
	   	Cursor c = db.rawQuery("SELECT _id, subtitle, prof,credit, time1, time2, time3, time4, time5, time6, time7, time8, time9, time10, time11, time12, time13, time14, time15, time16 FROM schedule WHERE _id='"+_id+"'", null);
	    if (c != null) {
	        c.moveToFirst();
	     }
	 
	    return c;
	   	

	   }
   
   public Cursor SearchYourScheduleTime(){
	   
		
	   	Cursor c = db.rawQuery("SELECT _id, subtitle, prof,credit, time1, time2, time3, time4, time5, time6, time7, time8, time9, time10, time11, time12, time13, time14, time15,time16 FROM yourschedule", null);
	    if (c != null) {
	        c.moveToFirst();
	     }
	 
	    return c;
	   	

	   }
 
   public void AddTimes(String subtitle, String prof,String credit, String[] times){ //add 버튼 눌렀을 때 쿼리
	   
		
	   String AddTimes = "INSERT INTO yourschedule (subtitle,prof,credit,time1,time2,time3,time4,time5,"
	   		+ "time6,time7,time8,time9,time10,"
	   		+ "time11,time12,time13,time14,time15,time16) VALUES ('"+subtitle+"','"+prof+"','"+credit+"','"+times[0]+"','"+times[1]+"','"+times[2]
	   		+"','"+times[3]+"','"+times[4]+"','"+times[5]+"','"+times[6]+"','"+times[7]+"','"+times[8]+"','"+times[9]+"','"
	   		+times[10]+"','"+times[11]+"','"+times[12]+"','"+times[13]+"','"+times[14]+"','"+times[15]+"')";
         db.execSQL(AddTimes); 
         

   }
   
   public void SubTimes(){
	   
		
	   String SubTimes = "delete from yourschedule where _id=(select MAX(_id) from yourschedule)";
         db.execSQL(SubTimes); 
	 

   }
   
   
   
   public void RefreshTimes(){
	   
		
	   String RefreshTimes = "DELETE from yourschedule;";
         db.execSQL(RefreshTimes); 
	 

   }
   /*
    * 
    * public void deleteAll() {
      Cursor c = getAllRows();
      long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
      if (c.moveToFirst()) {
         do {
            deleteRow(c.getLong((int) rowId));            
         } while (c.moveToNext());
      }
      c.close();
   }
   
   
   // Change an existing row to be equal to new data.
   public boolean updateRow(long rowId, String subnum) {
      String where = KEY_ROWID + "=" + rowId;

   // TODO: Update data in the row with new fields.
      // TODO: Also change the function's arguments to be what you need!
      // Create row's data:
      ContentValues newValues = new ContentValues();
      newValues.put(KEY_SUBNUM, subnum);
      
      // Insert it into the database.
      return db.update(DATABASE_TABLE, newValues, where, null) != 0;
   }
   */
   
   
   /////////////////////////////////////////////////////////////////////
   //   Private Helper Classes:
   /////////////////////////////////////////////////////////////////////
   
   /**
    * Private class which handles database creation and upgrading.
    * Used to handle low-level database access.
    */
   private static class DatabaseHelper extends SQLiteOpenHelper
   {
      DatabaseHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase _db) {
    	
      }

      @Override
      public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
       /* Log.w(TAG, "Upgrading application's database from version " + oldVersion
               + " to " + newVersion + ", which will destroy all old data!");
         
         // Destroy old database:
         _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
         
         // Recreate new database:
         onCreate(_db);*/
      }
   }
}