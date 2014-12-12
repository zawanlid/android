package com.vu.managephonecall.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUtility extends SQLiteOpenHelper {

	private String table_create;

	public DatabaseUtility(Context context, String databaseName,
			 int databaseVersion, String cratetable,
			String tableName) {
		 

		super(context, databaseName, null, databaseVersion);
		table_create=cratetable;
		
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

		try {
			database.execSQL(table_create);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVesrion,
			int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + ManageCallBlockDao.TABLE_NAME);
		onCreate(database);

	}
	

}
