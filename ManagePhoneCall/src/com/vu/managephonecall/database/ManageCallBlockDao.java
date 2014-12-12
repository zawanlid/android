package com.vu.managephonecall.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ManageCallBlockDao {

	public static final String TABLE_NAME = "block_call_list";
	public static final String COLUMN_ID = "id";

	public static final String COLUMN_PHONE_NUMBER = "phonenumber";

	private static final String DATABASE_NAME = "managephonecallblock.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_CREATE = "create table " + TABLE_NAME
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_PHONE_NUMBER + "  text);";

	private SQLiteDatabase database;
	private SQLiteOpenHelper helper;

	public ManageCallBlockDao(Context context) {
		helper = new DatabaseUtility(context, DATABASE_NAME, DATABASE_VERSION,
				TABLE_CREATE, TABLE_NAME);
	}

	public boolean delete(String phoneNumber) {
		Log.d("TAG", phoneNumber);
		try {
			database = helper.getWritableDatabase();
			int effectedRecords = database.delete(TABLE_NAME,
					COLUMN_PHONE_NUMBER + "=?", new String[] { phoneNumber });
			Log.d("TAG", "" + effectedRecords);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public boolean update(String phoneNumber,String previousNumber) {
		Log.d("TAG UPDATE", phoneNumber);
		database = helper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
		long effectedrows = database.update(TABLE_NAME, contentValues,
				COLUMN_PHONE_NUMBER + "=?", new String[] { previousNumber });
		Log.d("TAG UPDATE", ""+effectedrows);
		return true;

	}

	public boolean saveBlockedPhonenumber(String phoneNumber) {

		database = helper.getWritableDatabase();

		try {
			if (phoneNumber != null) {

				ContentValues contentValues = new ContentValues();
				contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
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

	public String[] getListOfBlockPhoneNumbers() {
		String[] blockPhoneNumbers = null;
		List<String> portValus = new ArrayList<String>();

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

	public int update() {
		return 0;
	}
}
