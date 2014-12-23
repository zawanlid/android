package com.vu.managephonecall.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ManageAutoAnswerDao {

	public static final String TABLE_NAME = "autoanswerlist";
	public static final String COLUMN_ID = "id";

	public static final String COLUMN_AUDIO_FILE = "audiofile";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_TITLE = "title";
	
	private static final String DATABASE_NAME = "manageautoanswer.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_CREATE = "create table " + TABLE_NAME
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_TITLE+ " text," + COLUMN_DESCRIPTION + " text, "
			 + COLUMN_AUDIO_FILE + "  text);";

	private SQLiteDatabase database;
	private SQLiteOpenHelper helper;

	public ManageAutoAnswerDao(Context context) {
		helper = new DatabaseUtility(context, DATABASE_NAME, DATABASE_VERSION,
				TABLE_CREATE, TABLE_NAME);
	}

	public boolean savesAutoAnswer(String title,
			String descrption, String audioFile) {

		database = helper.getWritableDatabase();

		try {
			

				ContentValues contentValues = new ContentValues();
				contentValues.put(COLUMN_TITLE, title);
				contentValues.put(COLUMN_DESCRIPTION, descrption);
				contentValues.put(COLUMN_AUDIO_FILE, audioFile);
				
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
	
	public String[] getEditPhoneNumberDetails(String title) {
		String[] blockPhoneNumbers = null;

		Log.d("CHECK", "" + "FINE");
		database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME+ " where "  +   COLUMN_TITLE+"=?", new String[]{title});

		Log.d("CUSRSOR SIZE", "" + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0) {
			blockPhoneNumbers = new String[3];

			if (cursor.moveToFirst()) {
				do {
					blockPhoneNumbers[0] = cursor.getString(1);
					blockPhoneNumbers[1] = cursor.getString(2);
					blockPhoneNumbers[2] = cursor.getString(3);
					Log.d("phone number", cursor.getString(3));
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		return blockPhoneNumbers;
	}
	public boolean update(String previousTitle,String title,String decription,String adudiFile) {
		Log.d("TAG UPDATE", previousTitle);
		database = helper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_TITLE, title);
		contentValues.put(COLUMN_DESCRIPTION, decription);
		contentValues.put(COLUMN_AUDIO_FILE, adudiFile);
		
		long effectedrows = database.update(TABLE_NAME, contentValues,
				COLUMN_TITLE + "=?", new String[] { previousTitle });
		Log.d("TAG UPDATE", ""+effectedrows);
		database.close();
		return true;

	}
	public boolean delete(String title) {
		Log.d("TAG", title);
		try {
			database = helper.getWritableDatabase();
			int effectedRecords = database.delete(TABLE_NAME,
					COLUMN_TITLE + "=?", new String[] { title });
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
	public String[] getListOfAutoAnswer() {
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
					Log.d("phone number", cursor.getString(3));
					i++;
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		return blockPhoneNumbers;
	}
	
	public String getMsgBody(){
		String msgBody = null;
		database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME,null);

		Log.d("CUSRSOR SIZE", "" + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0) {
			
			if (cursor.moveToFirst()) {
				do {
				  msgBody= cursor.getString(3);
					Log.d("Msg Body", cursor.getString(3));
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		
		
		return msgBody;
		
		
		
	}

	public int update() {
		return 0;
	}

}
