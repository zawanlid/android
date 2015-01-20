package com.vu.managephonecall.database;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ManageMeetingsDao {


	public static final String TABLE_NAME = "managemeetingsist";
	public static final String COLUMN_ID = "id";

	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_START_TIME = "startTime";
	public static final String COLUMN_END_TIME = "endtime";
	public static final String COLUMN_DAYS = "days";
	public static final String COLUMN_AUTO_ANSWER = "autoanswer";
	
	
	private static final String DATABASE_NAME = "managemeetings.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_CREATE = "create table " + TABLE_NAME
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_DESCRIPTION+ " text," + COLUMN_START_TIME + " text, "+ COLUMN_END_TIME + " text, " 
			 + COLUMN_DAYS + "  text, "+ COLUMN_AUTO_ANSWER + "  text);";

	private SQLiteDatabase database;
	private SQLiteOpenHelper helper;

	public ManageMeetingsDao(Context context) {
		helper = new DatabaseUtility(context, DATABASE_NAME, DATABASE_VERSION,
				TABLE_CREATE, TABLE_NAME);
	}

	public boolean savesMeetings(String descrption,
			String startTime, String endtime,String days,String autoAnswer) {

		database = helper.getWritableDatabase();

		try {
			

				ContentValues contentValues = new ContentValues();
				contentValues.put(COLUMN_DESCRIPTION, descrption);
				contentValues.put(COLUMN_START_TIME, startTime);
				contentValues.put(COLUMN_END_TIME, endtime);
				contentValues.put(COLUMN_DAYS, days);
				contentValues.put(COLUMN_AUTO_ANSWER, autoAnswer);
				
				
				long effectedrows = database.insert(TABLE_NAME, null,
						contentValues);
				Log.d("insert", "" + effectedrows + " --");

			
			database.close();
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		return true;
	}
	public String[] getEditDetails(String title) {
		String[] blockPhoneNumbers = null;

		Log.d("CHECK", "" + "FINE");
		database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME+ " where "  +   COLUMN_DESCRIPTION+"=?", new String[]{title});

		Log.d("CUSRSOR SIZE", "" + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0) {
			blockPhoneNumbers = new String[5];

			if (cursor.moveToFirst()) {
				do {
					blockPhoneNumbers[0] = cursor.getString(1);
					blockPhoneNumbers[1] = cursor.getString(2);
					blockPhoneNumbers[2] = cursor.getString(3);
					blockPhoneNumbers[3] = cursor.getString(4);
					blockPhoneNumbers[4] = cursor.getString(5);
					
					Log.d(" Manage Meeting: ", cursor.getString(3));
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		return blockPhoneNumbers;
	}
	public boolean update(String previousDescription,String description,String startDate,String endDate,String days,String autoAnswer) {
		Log.d("TAG UPDATE - Decsription :", previousDescription);
		Log.d("TAG UPDATE - Days :", days);
		Log.d("TAG UPDATE - AutoAnswer :", autoAnswer);
		database = helper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_DESCRIPTION, description);
		contentValues.put(COLUMN_START_TIME, startDate);
		contentValues.put(COLUMN_END_TIME, endDate);		
		contentValues.put(COLUMN_DAYS, days);		
		contentValues.put(COLUMN_AUTO_ANSWER, autoAnswer);
		
		long effectedrows = database.update(TABLE_NAME, contentValues,
				COLUMN_DESCRIPTION + "=?", new String[] { previousDescription });
		Log.d("TAG UPDATE", ""+effectedrows);
		database.close();
		return true;

	}
	public boolean delete(String description) {
		Log.d("TAG", description);
		try {
			database = helper.getWritableDatabase();
			int effectedRecords = database.delete(TABLE_NAME,
					COLUMN_DESCRIPTION + "=?", new String[] { description });
			Log.d("TAG", "" + effectedRecords);
			//cursor.close();
			database.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//cursor.close();
			database.close();
			return false;
		}
	}

	public String[] getListOfMeetings() {
		String[] blockPhoneNumbers = null;

		Log.d("CHECK", "" + "FINE");
		database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);

		Log.d("CUSRSOR SIZE", "" + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0) {
			blockPhoneNumbers = new String[cursor.getCount()];

			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					blockPhoneNumbers[i] = cursor.getString(1);
					Log.d("phone number", cursor.getString(1));
					i++;
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		return blockPhoneNumbers;
	}
	public Map<String,String> getListOfMeetingsTimings() {
		Map<String, String> mapMeetingTimes = null;

		Log.d("CHECK", "" + "FINE MAP");
		database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);

		Log.d("CUSRSOR SIZE", "" + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0) {
			mapMeetingTimes=new HashMap<String, String>();

			if (cursor.moveToFirst()) {
			
				do {
					mapMeetingTimes.put(cursor.getString(2), cursor.getString(3));
					Log.d("phone number", cursor.getString(1));
			
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		return mapMeetingTimes;
	}
	public String getDay(String startDate ,String endDate) {
		String day=null;
		String sdate=startDate.substring(0, startDate.length()-2);

		String edate=endDate.substring(0, endDate.length()-2);
		
		Log.d("CHECK", "" + "FINE DAY  "+sdate  +edate);
		database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from "+ TABLE_NAME +" where "+ COLUMN_START_TIME+"=?"+ "AND " +COLUMN_END_TIME+"=?", new String[]{sdate,edate});

		Log.d("CUSRSOR SIZE", "" + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0) {
			

			if (cursor.moveToFirst()) {
			
				do {
					day=cursor.getString(4);
					Log.d("Day", day);
				
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		return day;
	}
	public String getCallBackMsgID(String startDate ,String endDate) {
		String day=null;
		String sdate=startDate.substring(0, startDate.length()-2);

		String edate=endDate.substring(0, endDate.length()-2);
		
		database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from "+ TABLE_NAME +" where "+ COLUMN_START_TIME+"=?"+ "AND " +COLUMN_END_TIME+"=?", new String[]{sdate,edate});

		Log.d("CUSRSOR SIZE", "" + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0) {
			

			if (cursor.moveToFirst()) {
			
				do {
					day=cursor.getString(5);
					Log.d("Msg ID: ", day);
				
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		return day;
	}
	public int update() {
		return 0;
	}



}
