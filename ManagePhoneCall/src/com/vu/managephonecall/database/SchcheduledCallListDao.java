package com.vu.managephonecall.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SchcheduledCallListDao {

	public static final String TABLE_NAME = "scheduledlist";
	public static final String COLUMN_ID = "id";

	public static final String COLUMN_PHONE_NUMBER = "phonenumber";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_DATE_TIME = "datetime";
	//public static final String COLUMN_ALERT_TYPE = "alerttype";

	private static final String DATABASE_NAME = "scheduledcall.db";
	private static final int DATABASE_VERSION = 1;

	/*
	 * public static final String TABLE_CREATE = "create table " + TABLE_NAME +
	 * "(" + COLUMN_ID + " integer primary key autoincrement, " +
	 * COLUMN_DESCRIPTION + "  text, " + COLUMN_DATE_TIME + "  text," +
	 * COLUMN_ALERT_TYPE + "  text, " + COLUMN_PHONE_NUMBER + "  text);";
	 */
	public static final String TABLE_CREATE = "create table " + TABLE_NAME
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_DESCRIPTION + " text," + COLUMN_DATE_TIME + " text, "
            + COLUMN_PHONE_NUMBER + "  text);";

	private SQLiteDatabase database;
	private SQLiteOpenHelper helper;

	public SchcheduledCallListDao(Context context) {
		helper = new DatabaseUtility(context, DATABASE_NAME, DATABASE_VERSION,
				TABLE_CREATE, TABLE_NAME);
	}

	public boolean savescheduledPhonenumber(String phoneNumber,
			String descrption, String dateTime) {

		database = helper.getWritableDatabase();

		try {
			if (phoneNumber != null) {

				ContentValues contentValues = new ContentValues();
				contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
				contentValues.put(COLUMN_DESCRIPTION, descrption);
				contentValues.put(COLUMN_DATE_TIME, dateTime);
				

				long effectedrows = database.insert(TABLE_NAME, null,
						contentValues);
				Log.d("insert", "" + effectedrows + " --");

			}
			database.close();
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String[] getListOfScheduledPhoneNumbers() {
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
					blockPhoneNumbers[i] = cursor.getString(3);
					Log.d("phone number", cursor.getString(3));
					i++;
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		return blockPhoneNumbers;
	}
	public String[] getEditPhoneNumberDetails(String Phonenumber) {
		String[] blockPhoneNumbers = null;

		Log.d("CHECK", "" + "FINE");
		database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME+ " where "  +   COLUMN_PHONE_NUMBER+"=?", new String[]{Phonenumber});

		Log.d("CUSRSOR SIZE", "" + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0) {
			blockPhoneNumbers = new String[3];

			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					blockPhoneNumbers[0] = cursor.getString(1);
					blockPhoneNumbers[1] = cursor.getString(2);
					blockPhoneNumbers[2] = cursor.getString(3);
					Log.d("phone number", cursor.getString(3));
					i++;
				} while (cursor.moveToNext());
			}
			cursor.close();
			database.close();
		}
		return blockPhoneNumbers;
	}
	public boolean update(String previousNumber,String phoneNumber,String decription,String dateandtime) {
		Log.d("TAG UPDATE", phoneNumber);
		database = helper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
		contentValues.put(COLUMN_DESCRIPTION, decription);
		contentValues.put(COLUMN_DATE_TIME, dateandtime);
		
		long effectedrows = database.update(TABLE_NAME, contentValues,
				COLUMN_PHONE_NUMBER + "=?", new String[] { previousNumber });
		Log.d("TAG UPDATE", ""+effectedrows);
		database.close();
		return true;

	}
	public boolean delete(String phoneNumber) {
		Log.d("TAG", phoneNumber);
		try {
			database = helper.getWritableDatabase();
			int effectedRecords = database.delete(TABLE_NAME,
					COLUMN_PHONE_NUMBER + "=?", new String[] { phoneNumber });
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


	public int update() {
		return 0;
	}

}
